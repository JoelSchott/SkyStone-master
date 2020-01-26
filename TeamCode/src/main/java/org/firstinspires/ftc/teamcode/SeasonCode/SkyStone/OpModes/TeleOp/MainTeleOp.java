package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.TeleOp;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@TeleOp(name = "Main TeleOp", group = "TeleOp")
public class MainTeleOp extends LinearOpMode {

    MainBase base;

    DrivetrainState driveState = DrivetrainState.FIELD_RELATIVE;
    DrivetrainMode driveMode = DrivetrainMode.FULL_SPEED;

    private boolean clamped = false;

    private boolean gamepad2xHeld = false;
    private boolean gamepad2upHeld = false;
    private boolean gamepad2downHeld = false;
    private boolean gamepad2leftHeld = false;
    private boolean gamepad2rightHeld = false;

    private static final int[] ENCODER_PLACING_POSITIONS = {-143, -756, -1231, -1745, -2235, -3016, -1785};
    private int towerLevel = 0;
    private boolean automateLift = false;

    private ElapsedTime rotatorTimer = new ElapsedTime();
    private static final int ROTATE_TIME = 1000;

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

        base.arms.rightArm.setPosition(0.1);

        setAngle();

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

            double scale = 0.5;
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
         //   if (gamepad)


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

            //------------------------------------ARMS FOR AUTONOMOUS--------------------------------------------
            if (gamepad1.left_bumper){
                base.arms.setLeftPower(-1);
            }
            else if (gamepad1.left_trigger > 0.5){
                base.arms.setLeftPower(1);
            }
            else{
                base.arms.setLeftPower(-0.1);
            }

            if (gamepad1.right_bumper) {
                base.arms.setRightPosition(0.1);
            }
            else if (gamepad1.right_trigger > 0.5){
                base.arms.setRightPosition(0.68);
            }



            //------------------------------------COLLECTING-------------------------------------------------------------
            if (gamepad2.a){
                base.collector.collect(0.78);
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
                automateLift = false;
            }
            else{
                base.output.lift.setPower(0);
            }

            if (gamepad2.y){
                automateLift = true;
            }

            if (gamepad2.dpad_up){
                if (!gamepad2upHeld){
                    gamepad2upHeld = true;
                    towerLevel++;
                }
            }
            else{
                gamepad2upHeld = false;
            }

            if (gamepad2.dpad_down){
                if (!gamepad2downHeld){
                    gamepad2downHeld = true;
                    towerLevel--;
                }
            }
            else{
                gamepad2downHeld = false;
            }
            if (towerLevel > 6){
                towerLevel = 6;
            }
            if (towerLevel < 0){
                towerLevel = 0;
            }

            if (automateLift){

                int targetPosition = ENCODER_PLACING_POSITIONS[towerLevel];
                int currentPosition = base.output.lift.getCurrentPosition();
                int error = Math.abs(targetPosition - currentPosition);

                double power = 1;
                if (error < 200){
                    power = 0.3;
                }

                //negative power to lift motor is up
                //if targetPosition is less than current, we should go up

                if (targetPosition < currentPosition){
                    base.output.lift.setPower(-power);
                }
                else{
                    base.output.lift.setPower(power);
                }

                if (Math.abs(targetPosition - base.output.lift.getCurrentPosition()) < 10){
                    automateLift = false;
                    base.output.lift.setPower(0);
                }
            }


            //----------ROTATION------------------
            if (gamepad2.dpad_left){
                if (!gamepad2leftHeld){
                    gamepad2leftHeld = true;
                    base.output.inRotate();
                }
            }
            else{
                gamepad2leftHeld = false;
            }
            if (gamepad2.dpad_right){
                if (!gamepad2rightHeld){
                    gamepad2rightHeld = true;
                    base.output.outRotate();
                }
            }
            else{
                gamepad2rightHeld = false;
            }


            if (gamepad2.left_trigger > 0.2){
                base.output.blockRotator.setPosition(base.output.blockRotator.getPosition() - 0.01);
            }
            else if (gamepad2.right_trigger > 0.2){
                base.output.blockRotator.setPosition(base.output.blockRotator.getPosition() + 0.01);
            }

            //----------CAPSTONE-----------------
            if (gamepad2.right_bumper){
                base.output.placeMarker();
            }
            else if (gamepad2.left_bumper){
                base.output.retractMarker();
            }


            //------------------------------------TELEMETRY--------------------------------------------------------


            telemetry.addData("DRIVE STATE :", driveState);
            telemetry.addData("DRIVE MODE :", driveMode);
            telemetry.addLine();
            telemetry.addData("left range is ", base.leftRange.customDistanceInInches());
            telemetry.addData("TOWER LEVEL IS ", towerLevel);
            telemetry.addLine();
            telemetry.addData("front distance ", base.frontRange.customDistanceInInches());
            telemetry.addData("left distance is ", base.leftRange.customDistanceInInches());
//            telemetry.addData("front right encoders are ", base.drivetrain.frontRight.getCurrentPosition());
//            telemetry.addData("front left ", base.drivetrain.frontLeft.getCurrentPosition());
//            telemetry.addData("back left ", base.drivetrain.backLeft.getCurrentPosition());
//            telemetry.addData("back right ", base.drivetrain.backRight.getCurrentPosition());
            telemetry.update();

        }
    }

    private void setAngle(){
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "angle");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = br.readLine();
            int angle = Integer.getInteger(line);
            base.drivetrain.setInitalAngle(angle);
            br.close();
            telemetry.addData("Successfully set angle to ", angle);
            telemetry.update();
        }
        catch(Exception e){
            telemetry.addLine("problem with i/o");
            telemetry.update();
            e.printStackTrace();
        }
    }
}
