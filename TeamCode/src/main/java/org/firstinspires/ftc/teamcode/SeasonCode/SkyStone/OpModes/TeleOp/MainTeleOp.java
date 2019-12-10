package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

@TeleOp(name = "Main TeleOp", group = "TeleOp")
public class MainTeleOp extends LinearOpMode {

    MainBase base;

    DrivetrainState driveState = DrivetrainState.FIELD_RELATIVE;
    DrivetrainMode driveMode = DrivetrainMode.FULL_SPEED;

    private boolean clamped = false;
    private boolean gamepad2xHeld = false;

    enum DrivetrainState{
        ROBOT_RELATIVE,FIELD_RELATIVE
    }

    enum DrivetrainMode{
        FULL_SPEED, SLOW_MODE
    }


    @Override
    public void runOpMode(){

        base = new MainBase(hardwareMap, telemetry, this);
        base.init();

        telemetry.clear();
        telemetry.addLine("All Systems Go");
        telemetry.addLine("May be the Force be with us");
        telemetry.update();


        waitForStart();


        while (opModeIsActive()){

            //------------------------------------DRIVING-----------------------------------------------------

            if (gamepad1.a){
                driveState = DrivetrainState.FIELD_RELATIVE;
            }
            else if (gamepad1.b){
                driveState = DrivetrainState.ROBOT_RELATIVE;
            }

            if (gamepad1.x){
                driveMode = DrivetrainMode.FULL_SPEED;
            }
            else if (gamepad1.y){
                driveMode = DrivetrainMode.SLOW_MODE;
            }

            double forward = -gamepad1.left_stick_y;
            double right = gamepad1.left_stick_x;
            double turn = gamepad1.right_stick_x;

            double scale = 0.3;
            if (driveMode == DrivetrainMode.SLOW_MODE){
                forward = forward * scale;
                right = right * scale;
                turn = turn * scale;
            }

            switch(driveState){
                case ROBOT_RELATIVE:
                    base.drivetrain.robotRelativeDrive(forward, right, turn);
                    break;

                case FIELD_RELATIVE:
                    base.drivetrain.fieldRelativeDrive(forward, right, turn);
                    break;
            }


            //setting angles

            if (gamepad1.dpad_left){
                base.drivetrain.setCurrentAngleAs(180);
            }
            else if (gamepad1.dpad_down){
                base.drivetrain.setCurrentAngleAs(270);
            }
            else if (gamepad1.dpad_right){
                base.drivetrain.setCurrentAngleAs(0);
            }
            else if (gamepad1.dpad_up){
                base.drivetrain.setCurrentAngleAs(90);
            }

            //--------------------------------------FOUNDATION MOVING----------------------------------------------
            base.foundation.moveServo(gamepad2.left_stick_y);



            //------------------------------------COLLECTING-------------------------------------------------------------
            if (gamepad2.a){
                base.collector.collect(0.9);
            }
            else if (gamepad2.b){
                base.collector.spew(1);
            }
            else{
                base.collector.stop();
            }


            //------------------------------------OUTPUT-------------------------------------------------------------
            //-----------CLAMP------------------
            if (gamepad2.x && !gamepad2xHeld){
                gamepad2xHeld = true;
                if (clamped){
                    base.output.clampRelease();
                    clamped = false;
                }
                else{
                    base.output.clampGrab();
                    clamped = true;
                }
            }
            if (!gamepad2.x){
                gamepad2xHeld = false;
            }
            //---------LIFT----------------------

            if (Math.abs(gamepad2.right_stick_y) > 0.1){
                base.output.lift.setPower(gamepad2.right_stick_y);
            }
            else{
                base.output.lift.setPower(0);
            }

            //----------ROTATION------------------
            if (gamepad2.dpad_left){
                base.output.inRotate(0);
            }
            else if (gamepad2.dpad_right){
                base.output.outRotate(0);
            }


            //----------CAPSTONE-----------------
            if (gamepad2.right_bumper){
                base.output.placeMarker();
            }
            else if (gamepad2.left_bumper){
                base.output.retractMarker();
            }


            //------------------------------------TELEMETRY--------------------------------------------------------


            telemetry.addData("DRIVE STATE is ", driveState);
            telemetry.addData("DRIVE MODE is ", driveMode);
            telemetry.addLine();
            telemetry.addLine("angle is " + base.drivetrain.getProcessedAngle() + " degrees");
            telemetry.addLine();

            telemetry.update();

        }
    }
}
