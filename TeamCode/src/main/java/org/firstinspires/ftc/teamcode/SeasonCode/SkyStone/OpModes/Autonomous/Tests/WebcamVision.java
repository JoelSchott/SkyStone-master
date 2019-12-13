package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomPhoneCameraSkyStone;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomWebcamSkyStone;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

@Autonomous(name = "Webcam Vision Test")
public class WebcamVision extends LinearOpMode {

    private CustomWebcamSkyStone vision;

    private List<Recognition> objects;

    @Override
    public void runOpMode(){
        vision = new CustomWebcamSkyStone(hardwareMap);
        vision.init();

        waitForStart();

        while(opModeIsActive()){
            objects = vision.getObjects();
            if (objects != null){
                telemetry.addData("Stones visible are ", objects.size());
                int i = 0;
                for (Recognition object : objects){
                    if (object.getLabel().equals("Skystone")){
                        telemetry.addData("Label is ", object.getLabel());
                        telemetry.addData("Confidence is ", object.getConfidence());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                object.getLeft(), object.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                object.getRight(), object.getBottom());
                    }
                }
                for (Recognition object : objects){
                    if (!object.getLabel().equals("Skystone")){
                        telemetry.addData("Label is ", object.getLabel());
                        telemetry.addData("Confidence is ", object.getConfidence());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                object.getLeft(), object.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                object.getRight(), object.getBottom());
                    }
                }
                telemetry.addData("Conclusion is ", REDTwoStonesGetPosition(objects));

            }
            telemetry.update();
        }

    }

    private CustomPhoneCameraSkyStone.SkyStonePosition REDTwoStonesGetPosition(List<Recognition> stones){
        if (stones == null){
            return CustomPhoneCameraSkyStone.SkyStonePosition.UNKNOWN;
        }
        if (stones.size() < 2){
            return CustomPhoneCameraSkyStone.SkyStonePosition.UNKNOWN;
        }
        telemetry.addLine();
        telemetry.addLine("Got this far");

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
        telemetry.addData("First midpoint is ", firstMidpoint);
        double secondMidpoint = (stones.get(1).getLeft() + stones.get(1).getRight())/(2.0);
        telemetry.addData("Second midpoint is ", secondMidpoint);

        if (firstMidpoint < secondMidpoint){
            leftStone = stones.get(0);
            rightStone = stones.get(1);
        }
        else{
            rightStone = stones.get(0);
            leftStone = stones.get(1);
        }

        telemetry.addData("leftStone label is ", leftStone.getLabel());
        telemetry.addData("rightStone label is ", rightStone.getLabel());

        if (leftStone.getLabel().equals("Skystone") && rightStone.getLabel().equals("Stone")){
            position = CustomPhoneCameraSkyStone.SkyStonePosition.MIDDLE;
        }
        else if (leftStone.getLabel().equals("Stone") && rightStone.getLabel().equals("Skystone")){
            position = CustomPhoneCameraSkyStone.SkyStonePosition.RIGHT;
        }
        else{
            position = CustomPhoneCameraSkyStone.SkyStonePosition.LEFT;
        }
        return position;
    }


}
