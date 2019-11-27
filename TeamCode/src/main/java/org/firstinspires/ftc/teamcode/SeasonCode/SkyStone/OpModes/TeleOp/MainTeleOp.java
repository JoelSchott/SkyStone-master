package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

@TeleOp(name = "Main TeleOp", group = "TeleOp")
public class MainTeleOp extends LinearOpMode {

    MainBase base;

    DrivetrainState driveState = DrivetrainState.ROBOT_RELATIVE;

    private boolean clamped = false;

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
            base.foundation.setLeftPower(gamepad2.left_stick_y);
            base.foundation.setRightPower(gamepad2.right_stick_y);


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


            //------------------------------------OUTTAKE-------------------------------------------------------------

            if (gamepad2.x){
                base.output.clampGrab();
            }
            if (gamepad2.x){
                base.output.clampRelease();
            }

            //------------------------------------TELEMETRY--------------------------------------------------------

            telemetry.addData("X angle is ", base.imu.xAngle());
            telemetry.addData("Y angle is ", base.imu.yAngle());
            telemetry.addData("Z angle is", base.imu.zAngle());

            telemetry.addData("Processed angle is ", base.drivetrain.getProcessedAngle(base.imu.zAngle()));
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
