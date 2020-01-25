package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.Image;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase1Webcam;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBaseWebcam;

//@Autonomous(name = "Webcam Image Analysis ", group = "Autonomous")
public class WebcamImageAnalysis extends LinearOpMode {

    private static final String VUFORIA_KEY =
            "AfMlGNH/////AAABmSa3gXBnC0isoB6lbpdqh+ZpQ5x6bhx9h1x1DElrGnq+v/2YOuaFpugQxIG7LoveU7vW9MigQz/" +
                    "qBL7oKt0eWjouuqmFOewiup+NUSwmUnnsdYzF2Ofy/4Yh2Hn1es0nC2i/oOyIq/ii4aA60fRINMqkYmyvYp3g5tU+e2A76Biux6LWr/ctNRJ4KLZZeeSOV0wbJ6bb" +
                    "EIEtgYULHwXLyev7lz+w6TgFCYS++2BcTSmJNsLSnIMuqGOXIL8+hLbX6Lt+" +
                    "WGPSAlVpxr8CgTF1h+jNk6/JUB9VQuK83uUcIAT61xEr3iScylPWyF3XBQl5oTCsBDMhJ+a96DBa9DMs0LF/HlSBsm3I22CuvzX0w8eB";

    private VuforiaLocalizer vuforia;


    MainBaseWebcam base;

    CustomConsumer consumer = new CustomConsumer();
    CustomContinuation continuation = new CustomContinuation(consumer);


    public void runOpMode(){

        //base = new MainBase1Webcam(hardwareMap, telemetry, this);
        initVuforia();

        telemetry.addLine("waiting for start");
        telemetry.update();
        waitForStart();

        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        vuforia.setFrameQueueCapacity(1);

        while (opModeIsActive()){
            VuforiaLocalizer.CloseableFrame vuFrame = null;

            if (!vuforia.getFrameQueue().isEmpty()) {
                try {
                    vuFrame = vuforia.getFrameQueue().take();
                } catch (InterruptedException e) {
                    //Thread.currentThread().interrupt();
                    telemetry.addLine("interrupted error");
                    telemetry.update();
                }

                if (vuFrame == null) continue;

                for (int i = 0; i < vuFrame.getNumImages(); i++) {
                    Image img = vuFrame.getImage(i);
                    if (img.getFormat() == PIXEL_FORMAT.RGB565) {
                        telemetry.addData("capacity is ", img.getPixels().capacity());
                        double sum = 0;
                        telemetry.update();
//                        Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.RGB_565);
//                        bm.copyPixelsFromBuffer(img.getPixels());
//                        Mat mat = bitmapToMat(bm, CvType.CV_8UC3);
//                        Mat ret = p.processFrame(mat);
//                        Bitmap displayBitmap = Bitmap.createBitmap(ret.width(), ret.height(), Bitmap.Config.RGB_565);
//                        Utils.matToBitmap(ret, displayBitmap);
//                        dashboard.sendImage(displayBitmap);

                    }
                }
            }
        }


    }

    private void initVuforiaWebcam(){
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam");
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initVuforia(){
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private class CustomConsumer implements Consumer<com.vuforia.Frame> {
        public void accept(com.vuforia.Frame frame){
            telemetry.addData("Number of images is ", frame.getNumImages());
            telemetry.update();

        }
    }

    private class CustomContinuation extends Continuation<CustomConsumer> {
        protected CustomContinuation(CustomConsumer target) {
            super(target);
        }
    }


}
