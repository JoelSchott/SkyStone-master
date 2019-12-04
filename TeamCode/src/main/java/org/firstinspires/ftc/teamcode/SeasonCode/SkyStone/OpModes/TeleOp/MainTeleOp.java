package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

@TeleOp(name = "Main TeleOp", group = "TeleOp")
public class MainTeleOp extends LinearOpMode {

    MainBase base;

    DrivetrainState driveState = DrivetrainState.ROBOT_RELATIVE;

    private boolean clamped = false;
    private boolean clampButtonHeld = false;

    enum DrivetrainState{
        ROBOT_RELATIVE,FIELD_RELATIVE
    }


    @Override
    public void runOpMode(){

        base = new MainBase(hardwareMap, telemetry, this);
        base.init();

        telemetry.clear();
        telemetry.addLine("All Systems Go");
        telemetry.update();

        waitForStart();


        while (opModeIsActive()){

            //------------------------------------DRIVING-----------------------------------------------------

            switch(driveState){
                case ROBOT_RELATIVE:
                    base.drivetrain.robotRelativeDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
                    break;

                case FIELD_RELATIVE:
                    base.drivetrain.fieldRelativeDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
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
                base.collector.collect(1);
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
                }
            }
            if (!gamepad2.x){
                clampButtonHeld = false;
            }
            //---------LIFT----------------------

            if (gamepad2.dpad_up){
                base.output.liftUp(1);
            }
            else if (gamepad2.dpad_down){
                base.output.liftDown(1);
            }
            else{
                base.output.lift.setPower(0);
            }

            //----------ROTATION------------------
            base.output.blockRotator.setPower(gamepad2.right_stick_x);

            //----------CAPSTONE-----------------
            if (gamepad2.right_bumper){
                base.output.placeMarker();
            }
            else if (gamepad2.left_bumper){
                base.output.retractMarker();
            }


            //------------------------------------TELEMETRY--------------------------------------------------------

            telemetry.addData("X angle is ", base.imu.xAngle());
            telemetry.addData("Y angle is ", base.imu.yAngle());
            telemetry.addData("Z angle is", base.imu.zAngle());

            telemetry.addData("Processed angle is ", base.drivetrain.getProcessedAngle(0));
            telemetry.addData("Drive state is ", driveState);

            telemetry.addData("Driving with power ", gamepad1.left_stick_y);

            telemetry.addData("Front Left drivetrain ", base.drivetrain.frontLeft.getCurrentPosition());
            telemetry.addData("Front Right drivetrain ", base.drivetrain.frontRight.getCurrentPosition());
            telemetry.addData("Back Left drivetrain ", base.drivetrain.backLeft.getCurrentPosition());
            telemetry.addData("Back Right drivetrain ", base.drivetrain.backRight.getCurrentPosition());


            telemetry.update();

        }
    }
}
