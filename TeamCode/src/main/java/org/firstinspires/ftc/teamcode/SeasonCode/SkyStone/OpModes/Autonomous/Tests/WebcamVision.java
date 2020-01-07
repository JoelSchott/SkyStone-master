package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomPhoneCameraSkyStone;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomWebcamSkyStone;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase1Webcam;

import java.util.List;
import java.util.Timer;

@Autonomous(name = "Cropping manipulation")
public class WebcamVision extends LinearOpMode {

    private MainBase1Webcam base;

    private List<Recognition> objects;

    private int leftMargin = 0;
    private int topMargin = 0;
    private int rightMargin = 0;
    private int bottomMargin = 0;

    private boolean leftHeld = false;
    private boolean topHeld = false;
    private boolean rightHeld = false;
    private boolean bottomHeld = false;
    private boolean xHeld =  false;

    private boolean hideMore = true;

    private int intervalIndex = 0;
    private int[] intervals = {1,5,50,100,200};

    @Override
    public void runOpMode(){
        base = new MainBase1Webcam(hardwareMap, telemetry, this);
        base.init();

        waitForStart();

        while(opModeIsActive()){
            if (gamepad1.a){
                hideMore = true;
            }
            else if (gamepad1.b){
                hideMore = false;
            }

            if (gamepad1.x && !xHeld){
                xHeld = true;
                intervalIndex = (intervalIndex + 1) % intervals.length;
            }
            else if (!gamepad1.x){
                xHeld = false;
            }

            int change = intervals[intervalIndex];
            if (gamepad1.dpad_up && !topHeld){
                topHeld = true;
                if (hideMore){
                    topMargin += change;
                }
                else if (topMargin > 0){
                    topMargin -= change;
                }
            }
            else if (!gamepad1.dpad_up){
                topHeld = false;
            }
            if (gamepad1.dpad_left && !leftHeld){
                leftHeld = true;
                if (hideMore){
                    leftMargin += change;
                }
                else if (leftMargin > 0){
                    leftMargin -= change;
                }
            }
            else if (!gamepad1.dpad_left){
                leftHeld = false;
            }
            if (gamepad1.dpad_down && !bottomHeld){
                bottomHeld = true;
                if (hideMore){
                    bottomMargin += change;
                }
                else if (bottomMargin > 0){
                    bottomMargin -= change;
                }
            }
            else if (!gamepad1.dpad_down){
                bottomHeld = false;
            }
            if (gamepad1.dpad_right && !rightHeld){
                rightHeld = true;
                if (hideMore){
                    rightMargin += change;
                }
                else if (rightMargin > 0){
                    rightMargin -= change;
                }
            }
            else if (!gamepad1.dpad_right){
                rightHeld = false;
            }
            telemetry.addLine("dpad for borders in or out");
            telemetry.addLine("a button for in");
            telemetry.addLine("b button for out");
            telemetry.addData("hide more is ", hideMore);
            telemetry.addData("interval is ", intervals[intervalIndex]);
            telemetry.addLine();
            telemetry.addData("left margin is ", leftMargin);
            telemetry.addData("top margin is ", topMargin);
            telemetry.addData("right margin is ", rightMargin);
            telemetry.addData("bottom margin is ", bottomMargin);

            objects = base.webcam.getObjects(leftMargin,topMargin,rightMargin,bottomMargin);

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
                for (Recognition object : objects) {
                    if (!object.getLabel().equals("Skystone")) {
                        telemetry.addData("Label is ", object.getLabel());
                        telemetry.addData("Confidence is ", object.getConfidence());
                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                object.getLeft(), object.getTop());
                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                object.getRight(), object.getBottom());
                    }
                }

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
