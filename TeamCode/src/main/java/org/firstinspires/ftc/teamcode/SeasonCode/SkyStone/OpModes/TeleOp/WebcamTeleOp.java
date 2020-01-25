package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomPhoneCameraSkyStone;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomSounds;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase1Webcam;


@TeleOp(name = "2 Webcam TeleOp", group = "TeleOp")
public class WebcamTeleOp extends LinearOpMode {

    MainBase1Webcam base;

    DrivetrainState driveState = DrivetrainState.ROBOT_RELATIVE;

    private boolean clamped = false;
    private boolean clampButtonHeld = false;

    private boolean showLeft = true;

    private int debugClampCount = 0;

    enum DrivetrainState{
        ROBOT_RELATIVE,FIELD_RELATIVE
    }


    @Override
    public void runOpMode(){

        base = new MainBase1Webcam(hardwareMap, telemetry, this);
        base.init();

        telemetry.clear();
        telemetry.addLine("All Systems Go");
        telemetry.update();

        new CustomSounds(hardwareMap).playSound(CustomSounds.ROGER_ROGER);

        waitForStart();


        while (opModeIsActive()){

            //------------------------------------DRIVING-----------------------------------------------------

            switch(driveState){
                case ROBOT_RELATIVE:
                    base.drivetrain.robotRelativeDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
                    break;

                case FIELD_RELATIVE:
                    base.drivetrain.fieldRelativeDrive(gamepad1.left_stick_y, -gamepad1.left_stick_x, gamepad1.right_stick_x);
                    break;
            }

            if (gamepad1.a){
                driveState = DrivetrainState.ROBOT_RELATIVE;
            }
            else if (gamepad1.b){
                driveState = DrivetrainState.FIELD_RELATIVE;
            }

            //--------------------------------------FOUNDATION MOVING----------------------------------------------
            base.foundation.moveServo(gamepad2.left_stick_y);



            //------------------------------------COLLECTING-------------------------------------------------------------
            if (gamepad2.a){
                base.collector.collect(0.7);
            }
            else if (gamepad2.b){
                base.collector.spew(1);
            }
            else{
                base.collector.stop();
            }


            //------------------------------------OUTPUT-------------------------------------------------------------
            //-----------CLAMP------------------
            if (gamepad2.x && !clampButtonHeld){
                clampButtonHeld = true;
                if (clamped){
                    base.output.clampRelease();
                    clamped = false;
                }
                else{
                    base.output.clampGrab();
                    clamped = true;
                    debugClampCount++;
                }
            }
            if (!gamepad2.x){
                clampButtonHeld = false;
            }
            //---------LIFT----------------------

            if (Math.abs(gamepad2.right_stick_y) > 0.1){
                if (gamepad2.right_stick_y > 0){
                    base.output.lift.setPower(-gamepad2.right_stick_y);
                }
                else {
                    base.output.lift.setPower(-gamepad2.right_stick_y);
                }
            }
            else{
                base.output.lift.setPower(0);
            }

            //----------ROTATION------------------
            if (gamepad2.left_trigger > 0.2){
                base.output.inRotate();
            }
            else if (gamepad2.right_trigger > 0.2){
                base.output.inRotate();
            }


            //----------CAPSTONE-----------------
            if (gamepad2.right_bumper){
                base.output.placeMarker();
            }
            else if (gamepad2.left_bumper){
                base.output.retractMarker();
            }


            //------------------------------------TELEMETRY--------------------------------------------------------
            if (gamepad1.left_bumper){
                showLeft = true;
            }
            else if (gamepad1.right_bumper){
                showLeft = false;
            }
            telemetry.addData("Show left is ", showLeft);
            telemetry.addData("conclusion is ", CustomPhoneCameraSkyStone.REDTwoStonesGetPosition(base.webcam.getObjects()));

            if (showLeft){
                for (Recognition stone : base.webcam.getObjects()){
                    telemetry.addData("Stone with label ", stone.getLabel());
                    telemetry.addData("confidence is ", stone.getConfidence());
                    telemetry.addLine("top, left is " + stone.getTop() + " , " + stone.getLeft());
                    telemetry.addLine("bottom, right is " + stone.getBottom() + " , " + stone.getRight());
                    telemetry.addData("MIDPOINT IS ", (stone.getLeft() + stone.getRight())/2.0);
                    telemetry.addLine();
                }
            }
            else{
                for (Recognition stone : base.webcam.getObjects()){
                    telemetry.addData("Stone with label ", stone.getLabel());
                    telemetry.addData("confidence is ", stone.getConfidence());
                    telemetry.addLine("top, left is " + stone.getTop() + " , " + stone.getLeft());
                    telemetry.addLine("bottom, right is " + stone.getBottom() + " , " + stone.getRight());
                    telemetry.addData("MIDPOINT IS ", (stone.getLeft() + stone.getRight())/2.0);
                    telemetry.addLine();
                }
            }


            telemetry.update();


            /*telemetry.addData("Gyro Angle is ", base.gyro.heading());
            telemetry.addData("Processed angle is", base.drivetrain.getProcessedAngle());
            telemetry.addData("front distance is ", base.frontRange.customDistanceInInches());
            telemetry.addData("Drive state is ", driveState);
            telemetry.addLine();
            telemetry.addData("Front Left drivetrain ", base.drivetrain.frontLeft.getCurrentPosition());
            telemetry.addData("Front Right drivetrain ", base.drivetrain.frontRight.getCurrentPosition());
            telemetry.addData("Back Left drivetrain ", base.drivetrain.backLeft.getCurrentPosition());
            telemetry.addData("Back Right drivetrain ", base.drivetrain.backRight.getCurrentPosition());
            telemetry.addLine();
            telemetry.addData("marker position is trying to be ", base.output.marker.getPosition());
            telemetry.addData("clamp position is trying to be ", base.output.clamp.getPosition());
            telemetry.addData("debug clamp count is ", debugClampCount);*/

            telemetry.update();

        }
    }
}
