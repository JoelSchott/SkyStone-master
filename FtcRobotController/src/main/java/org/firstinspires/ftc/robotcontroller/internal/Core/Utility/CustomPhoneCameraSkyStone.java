package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CustomPhoneCameraSkyStone {

    public CustomPhoneCameraSkyStone(HardwareMap map){
        this.hardwareMap = map;
    }

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private HardwareMap hardwareMap;
    /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
    private static final String VUFORIA_KEY =
            "AfMlGNH/////AAABmSa3gXBnC0isoB6lbpdqh+ZpQ5x6bhx9h1x1DElrGnq+v/2YOuaFpugQxIG7LoveU7vW9MigQz/" +
                    "qBL7oKt0eWjouuqmFOewiup+NUSwmUnnsdYzF2Ofy/4Yh2Hn1es0nC2i/oOyIq/ii4aA60fRINMqkYmyvYp3g5tU+e2A76Biux6LWr/ctNRJ4KLZZeeSOV0wbJ6bb" +
                    "EIEtgYULHwXLyev7lz+w6TgFCYS++2BcTSmJNsLSnIMuqGOXIL8+hLbX6Lt+" +
                    "WGPSAlVpxr8CgTF1h+jNk6/JUB9VQuK83uUcIAT61xEr3iScylPWyF3XBQl5oTCsBDMhJ+a96DBa9DMs0LF/HlSBsm3I22CuvzX0w8eB";

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    public enum SkyStonePosition{
        LEFT,MIDDLE,RIGHT,UNKNOWN
    }

    public void init(){

        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        }

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();
        }
    }

    public List<Recognition> getObjects(){
        tfod.setClippingMargins(0,0,0,0);
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> recognitions = tfod.getRecognitions();
            if (recognitions != null) {
                return recognitions;
            }
        }
        return null;
    }
    public List<Recognition> getObjects(int left, int top, int right, int bottom){
        tfod.setClippingMargins(left,top,right,bottom);
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> recognitions = tfod.getRecognitions();
            if (recognitions != null) {
                return recognitions;
            }
        }
        return null;
    }

    public static SkyStonePosition REDTwoStonesGetPosition(List<Recognition> stones){
        if (stones == null){
            return SkyStonePosition.UNKNOWN;
        }
        for (Recognition stone : stones){
            double midPoint = (stone.getLeft() + stone.getRight())/2.0;
            if (stone.getLabel().equals("Skystone") && stone.getConfidence() > 0.73 && midPoint > 320){
                return SkyStonePosition.RIGHT;
            }
            else if (stone.getLabel().equals("Skystone") && stone.getConfidence() > 0.73 && midPoint < 320){
                return SkyStonePosition.MIDDLE;
            }
        }
        if (stones.size() < 2){
            return SkyStonePosition.UNKNOWN;
        }
        SkyStonePosition position;
        Recognition leftStone = null;
        Recognition rightStone = null;

        while(stones.size() > 2){
            int indexToRemove = 0;
            double lowestConfidence = 1;
            for (int i = 0; i < stones.size(); i++){
                if (stones.get(i).getConfidence() < lowestConfidence){
                    lowestConfidence = stones.get(i).getConfidence();
                    indexToRemove = i;
                }
            }
            stones.remove(indexToRemove);
        }

        double firstMidpoint = (stones.get(0).getLeft() + stones.get(0).getRight())/(2.0);
        double secondMidpoint = (stones.get(1).getLeft() + stones.get(1).getRight())/(2.0);

        if (firstMidpoint < secondMidpoint){
            leftStone = stones.get(0);
            rightStone = stones.get(1);
        }
        else{
            rightStone = stones.get(0);
            leftStone = stones.get(1);
        }

        if (leftStone.getLabel().equals("Skystone") && rightStone.getLabel().equals("Stone")){
            position = SkyStonePosition.MIDDLE;
        }
        else if (leftStone.getLabel().equals("Stone") && rightStone.getLabel().equals("Skystone")){
            position = SkyStonePosition.RIGHT;
        }
        else{
            position = SkyStonePosition.LEFT;
        }
        return position;
    }

    public static SkyStonePosition REDTwoStonesGetPosition(List<Recognition> stones, int middleImage){
        if (stones == null){
            return SkyStonePosition.UNKNOWN;
        }
        if (stones.size() < 2){
            if (stones.size() == 1){
                if (stones.get(0).getLabel().equals("Skystone") && stones.get(0).getConfidence() > 0.7){
                    double midPoint = (stones.get(0).getRight() + stones.get(0).getLeft())/2.0;
                    if (midPoint > middleImage){
                        return SkyStonePosition.RIGHT;
                    }
                    else{
                        return SkyStonePosition.MIDDLE;
                    }
                }
            }
            return SkyStonePosition.UNKNOWN;
        }
        SkyStonePosition position;
        Recognition leftStone = null;
        Recognition rightStone = null;

        while(stones.size() > 2){
            int indexToRemove = 0;
            double lowestConfidence = 1;
            for (int i = 0; i < stones.size(); i++){
                if (stones.get(i).getConfidence() < lowestConfidence){
                    lowestConfidence = stones.get(i).getConfidence();
                    indexToRemove = i;
                }
            }
            stones.remove(indexToRemove);
        }

        double firstMidpoint = (stones.get(0).getLeft() + stones.get(0).getRight())/(2.0);
        double secondMidpoint = (stones.get(1).getLeft() + stones.get(1).getRight())/(2.0);

        if (firstMidpoint < secondMidpoint){
            leftStone = stones.get(0);
            rightStone = stones.get(1);
        }
        else{
            rightStone = stones.get(0);
            leftStone = stones.get(1);
        }

        if (leftStone.getLabel().equals("Skystone") && rightStone.getLabel().equals("Stone")){
            position = SkyStonePosition.MIDDLE;
        }
        else if (leftStone.getLabel().equals("Stone") && rightStone.getLabel().equals("Skystone")){
            position = SkyStonePosition.RIGHT;
        }
        else{
            position = SkyStonePosition.LEFT;
        }
        return position;
    }

    public static CustomPhoneCameraSkyStone.SkyStonePosition BLUETwoStonesGetPosition(List<Recognition> stones){
        if (stones == null){
            return SkyStonePosition.UNKNOWN;
        }
        if (stones.size() < 2){
            return SkyStonePosition.UNKNOWN;
        }

        CustomPhoneCameraSkyStone.SkyStonePosition position;
        Recognition leftStone = null;
        Recognition rightStone = null;

        while(stones.size() > 2){
            int indexToRemove = 0;
            double lowestConfidence = 1;
            for (int i = 0; i < stones.size(); i++){
                if (stones.get(i).getConfidence() < lowestConfidence){
                    lowestConfidence = stones.get(i).getConfidence();
                    indexToRemove = i;
                }
            }
            stones.remove(indexToRemove);
        }

        double firstMidpoint = (stones.get(0).getLeft() + stones.get(0).getRight())/(2.0);
        double secondMidpoint = (stones.get(1).getLeft() + stones.get(1).getRight())/(2.0);

        if (firstMidpoint < secondMidpoint){
            leftStone = stones.get(0);
            rightStone = stones.get(1);
        }
        else{
            rightStone = stones.get(0);
            leftStone = stones.get(1);
        }

        if (leftStone.getLabel().equals("Skystone") && rightStone.getLabel().equals("Stone")){
            position = CustomPhoneCameraSkyStone.SkyStonePosition.LEFT;
        }
        else if (leftStone.getLabel().equals("Stone") && rightStone.getLabel().equals("Skystone")){
            position = CustomPhoneCameraSkyStone.SkyStonePosition.MIDDLE;
        }
        else{
            position = CustomPhoneCameraSkyStone.SkyStonePosition.RIGHT;
        }
        return position;
    }

    //array of 8 numbers for how much to crop for the following
    //top, bottom, left for left stone, right for left stone, left for middle stone, right for middle stone
    // left for right stone, right for right stone
    public static int[] REDCroppingPositions(List<Recognition> stones){
        int[] crops = new int[8];
        if (stones == null){
            return null;
        }
        int indexToKeep = 0;
        double greatestConfidence = 0;
        for (int i = 0; i < stones.size(); i++){
            if (stones.get(i).getConfidence() > greatestConfidence){
                greatestConfidence = stones.get(i).getConfidence();
                indexToKeep = i;
            }
        }
        Recognition stone = stones.get(indexToKeep);
        int width = (int) (stone.getWidth() / 3.0);
        int buffer = 10;
        crops[0] = (int) stone.getBottom() - buffer;
        crops[1] = (int)(stone.getImageHeight() - (stone.getTop() + buffer));

        crops[2] = (int) stone.getLeft() - buffer;
        crops[3] = (int) (stone.getImageWidth() - (stone.getLeft() + width + buffer));

        crops[4] = (int) (stone.getLeft() + width - buffer);
        crops[5] = (int) (stone.getImageWidth() - (stone.getRight() - width + buffer));

        crops[6] = (int) (stone.getRight() - width - buffer);
        crops[7] = (int) (stone.getImageWidth() - (stone.getRight() + buffer));

        return crops;
    }

    public static SkyStonePosition REDThreeStonesGetPosition(List<Recognition> stones){
        if (stones == null){
            return SkyStonePosition.UNKNOWN;
        }
        SkyStonePosition position = SkyStonePosition.UNKNOWN;
        ArrayList<Recognition> skystones = new ArrayList<>();

        ArrayList<Integer> indexesToRemove = new ArrayList<>();
        for (int i = 0; i< stones.size(); i++){
            if (stones.get(i).getLabel().equals("Skystone")){
                skystones.add(stones.get(i));
                indexesToRemove.add(i);
            }
        }
        for (int g = indexesToRemove.size() -1; g > -1; g--){
            stones.remove(indexesToRemove.get(g));
        }

        if (skystones.size() == 1 && stones.size() == 2){
            if (skystones.get(0).getRight() > stones.get(0).getRight() && skystones.get(0).getRight() > stones.get(1).getRight()){
                position = SkyStonePosition.RIGHT;
            }
            else if (skystones.get(0).getRight() < stones.get(0).getRight() && skystones.get(0).getRight() < stones.get(1).getRight()){
                position = SkyStonePosition.LEFT;
            }
            else{
                position = SkyStonePosition.MIDDLE;
            }
        }
        else if (skystones.size() == 1){
            if (skystones.get(0).getLeft() > 293 || skystones.get(0).getRight() > 540){
                position = SkyStonePosition.RIGHT;
            }
            else if (skystones.get(0).getLeft() < 102 || skystones.get(0).getRight() <  344){
                position = SkyStonePosition.LEFT;
            }
            else{
                position = SkyStonePosition.MIDDLE;
            }
        }


        return position;
    }

    public void shutdown(){
        if (tfod != null) {
            tfod.shutdown();
        }
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.3;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }
}
