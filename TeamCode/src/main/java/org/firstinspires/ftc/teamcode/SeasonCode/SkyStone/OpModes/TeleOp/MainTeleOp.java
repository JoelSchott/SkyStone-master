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

    private int debugClampCount = 0;

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
                    debugClampCount++;
                }
            }
            if (!gamepad2.x){
                clampButtonHeld = false;
            }
            //---------LIFT----------------------

            if (Math.abs(gamepad2.right_stick_y) > 0.1){
                base.output.lift.setPower(-gamepad2.right_stick_y);
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

            telemetry.addData("Gyro Angle is ", base.gyro.heading());
            telemetry.addData("used angle is ", base.drivetrain.getProcessedAngle());
            telemetry.addData("Drive state is ", driveState);
            telemetry.addLine();
            telemetry.addData("Front Left drivetrain ", base.drivetrain.frontLeft.getCurrentPosition());
            telemetry.addData("Front Right drivetrain ", base.drivetrain.frontRight.getCurrentPosition());
            telemetry.addData("Back Left drivetrain ", base.drivetrain.backLeft.getCurrentPosition());
            telemetry.addData("Back Right drivetrain ", base.drivetrain.backRight.getCurrentPosition());
            telemetry.addLine();
            telemetry.addData("marker position is trying to be ", base.output.marker.getPosition());
            telemetry.addData("clamp position is trying to be ", base.output.clamp.getPosition());
            telemetry.addData("debug clamp count is ", debugClampCount);

            telemetry.update();

        }
    }
}
