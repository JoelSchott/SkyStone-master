package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.SubsidiaryProjects;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

//@TeleOp(name = "HugBotTeleOp", group = "TeleOp")
public class HugBot extends LinearOpMode {

    private DcMotor driveMotor1;
    private DcMotor driveMotor2;
    private DcMotor driveMotor3;
    private DcMotor driveMotor4;
    private DcMotor driveMotor5;
    private DcMotor driveMotor6;

    private DcMotor armLiftMotor;

    private Servo armServo1;
    private Servo armServo2;


    @Override
    public void runOpMode(){

        driveMotor1 = hardwareMap.dcMotor.get("frontLeft");
        driveMotor2 = hardwareMap.dcMotor.get("middleLeft");
        driveMotor2.setDirection(DcMotor.Direction.REVERSE);
        driveMotor3 = hardwareMap.dcMotor.get("backLeft");

        driveMotor4 = hardwareMap.dcMotor.get("frontRight");
        driveMotor4.setDirection(DcMotorSimple.Direction.REVERSE);
        driveMotor5 = hardwareMap.dcMotor.get("middleRight");
        driveMotor6 = hardwareMap.dcMotor.get("backRight");
        driveMotor6.setDirection(DcMotorSimple.Direction.REVERSE);

        armLiftMotor = hardwareMap.dcMotor.get("armLift");

        armServo1 = hardwareMap.servo.get("leftArm");
        armServo2 = hardwareMap.servo.get("rightArm");

        telemetry.addData("Hi! I'm the program for the HugBot" , "");
        telemetry.addData("To start, tap the Play button", "");
        telemetry.update();
        telemetry.clear();

        waitForStart();

        while (opModeIsActive()){

            telemetry.addData("Use the Left Joystick to turn and the Right Joystick to drive" , "");
            telemetry.addData("Lift the arms up and down with DPad Up and DPad Down", "");
            telemetry.addData("Operate the arms with the bumpers and triggers", "");
            telemetry.update();


            //--------------------DRIVETRAIN----------------------------------
            double driveSpeed = Range.clip(gamepad1.left_stick_y, -1, 1);
            double turnSpeed = Range.clip(gamepad1.right_stick_x, -1, 1);

            double leftPower = driveSpeed + turnSpeed;
            double rightPower = driveSpeed - turnSpeed;

            if (Math.abs(leftPower) > Math.abs(rightPower)){
                if (leftPower > 1){
                    rightPower = rightPower/leftPower;
                    leftPower = 1.0;
                }
                else if (leftPower < -1){
                    rightPower = rightPower/leftPower * -1.0;
                    leftPower = -1.0;
                }
            }
            else{
                if (rightPower > 1){
                    leftPower = leftPower/rightPower;
                    rightPower = 1.0;
                }
                else if (rightPower < -1){
                    leftPower = leftPower/rightPower * -1.0;
                    rightPower = -1.0;
                }
            }

            if (Math.abs(driveSpeed) < 0.15 && Math.abs(turnSpeed) < 0.15){
                driveMotor1.setPower(0);
                driveMotor2.setPower(0);
                driveMotor3.setPower(0);
                driveMotor4.setPower(0);
                driveMotor5.setPower(0);
                driveMotor6.setPower(0);
            }
            else{
                driveMotor1.setPower(leftPower);
                driveMotor2.setPower(leftPower);
                driveMotor3.setPower(leftPower);
                driveMotor4.setPower(rightPower);
                driveMotor5.setPower(rightPower);
                driveMotor6.setPower(rightPower);
            }


            //--------------------ARM LIFT--------------------------------
            double upPower = 1;

            if (gamepad1.dpad_up){
                armLiftMotor.setPower(upPower);
                telemetry.addLine("UP UP UP UP UP UP");
            }
            else if (gamepad1.dpad_down){
                armLiftMotor.setPower(-upPower);
                telemetry.addLine("DOWN DWON DOWN DOWN");
            }
            else{
                armLiftMotor.setPower(0);
            }

            //--------------------ARMS-------------------------------

            double increment = 0.05;
            if (gamepad1.left_trigger > 0.8){
                telemetry.addLine("Left moving");
                armServo1.setPosition(armServo1.getPosition() - increment);
            }
            else if (gamepad1.left_bumper){
                armServo1.setPosition(armServo1.getPosition() + increment);
                telemetry.addLine("Left moving other way");
            }
            else{
                armServo1.setPosition(armServo1.getPosition());
            }


            if (gamepad1.right_bumper){
                armServo2.setPosition(armServo2.getPosition() - increment);
                telemetry.addLine("right arm moving");
            }
            else if (gamepad1.right_trigger > 0.8){
                armServo2.setPosition(armServo2.getPosition() + increment);
                telemetry.addLine("right arm moving other way");
            }
            else{
                armServo2.setPosition(armServo2.getPosition());
            }
        }

    }
}
