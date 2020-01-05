package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.internal.tfod.TfodParameters;

import java.util.List;

public class CustomWebcamSkyStone {

    private HardwareMap hardwareMap;

    private static final String TFOD_MODEL_ASSET = "Skystone.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Stone";
    private static final String LABEL_SECOND_ELEMENT = "Skystone";

    private static final String VUFORIA_KEY =
            "AfMlGNH/////AAABmSa3gXBnC0isoB6lbpdqh+ZpQ5x6bhx9h1x1DElrGnq+v/2YOuaFpugQxIG7LoveU7vW9MigQz/" +
                    "qBL7oKt0eWjouuqmFOewiup+NUSwmUnnsdYzF2Ofy/4Yh2Hn1es0nC2i/oOyIq/ii4aA60fRINMqkYmyvYp3g5tU+e2A76Biux6LWr/ctNRJ4KLZZeeSOV0wbJ6bb" +
                    "EIEtgYULHwXLyev7lz+w6TgFCYS++2BcTSmJNsLSnIMuqGOXIL8+hLbX6Lt+" +
                    "WGPSAlVpxr8CgTF1h+jNk6/JUB9VQuK83uUcIAT61xEr3iScylPWyF3XBQl5oTCsBDMhJ+a96DBa9DMs0LF/HlSBsm3I22CuvzX0w8eB";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    private String name;

    public CustomWebcamSkyStone(HardwareMap map, String name){
        hardwareMap = map;
        this.name = name;
    }

    public VuforiaLocalizer getVuforia(){
        return vuforia;
    }

    public void init(){
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()){
            initTfod();
        }
        if (tfod != null){
            tfod.activate();
        }
    }

    public List<Recognition> getObjects(){
        tfod.setClippingMargins(0,0,0,0);
        if (tfod != null){
            List<Recognition> recognitions = tfod.getRecognitions();
            return recognitions;
        }
        return null;
    }
    public List<Recognition> getObjects(int left, int top, int right, int bottom){
        tfod.setClippingMargins(left, top, right, bottom);
        if (tfod != null){
            List<Recognition> recognitions = tfod.getRecognitions();
            return recognitions;
        }
        return null;
    }



    public void shutdown(){
        if (tfod != null){
            tfod.shutdown();
        }
    }

    private void initVuforia(){
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, name);

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod(){
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minimumConfidence = 0.5;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

}
