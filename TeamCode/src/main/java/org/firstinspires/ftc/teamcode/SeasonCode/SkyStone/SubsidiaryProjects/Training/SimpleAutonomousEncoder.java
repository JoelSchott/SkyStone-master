package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.SubsidiaryProjects.Training;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class SimpleAutonomousEncoder extends LinearOpMode {

    private final static double WHEEL_DIAMETER = 4;
    private final static double ENCODERS_PER_MOTOR_REV = 1440;
    private final static double MOTOR_REV_PER_WHEEL_REV = 2;
    private final static double WHEEL_REV_PER_INCH = 1 / (3.14 * WHEEL_DIAMETER);
    private final static double ENCODERS_PER_INCH = ENCODERS_PER_MOTOR_REV * MOTOR_REV_PER_WHEEL_REV * WHEEL_REV_PER_INCH;


    private ElapsedTime runTime = new ElapsedTime();

   private DcMotor motor1;
   private DcMotor motor2;
   private DcMotor motor3;
   private DcMotor motor4;

   private DcMotor[] motors = new DcMotor[4];

    @Override
    public void runOpMode(){
        motor1 = hardwareMap.dcMotor.get("frontLeft");
        motor1.setDirection(DcMotor.Direction.REVERSE);
        motors[0] = motor1;
        motor2 = hardwareMap.dcMotor.get("frontRight");
        motors[1] = motor2;
        motor3 = hardwareMap.dcMotor.get("backLeft");
        motor3.setDirection(DcMotor.Direction.REVERSE);
        motors[2] = motor3;
        motor4 = hardwareMap.dcMotor.get("backRight");
        motors[3] = motor4;

        for (DcMotor m : motors){
            m.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        waitForStart();

        encoderDrive(0.8, 10, 10, 6);
        encoderDrive(0.8,-10,-10,6);

    }

    private void encoderDrive(double speed, double leftInches, double rightInches, double timeOutS){
        int newLeftTarget;
        int newRightTarget;
        newLeftTarget = motor1.getCurrentPosition() + (int) (leftInches * ENCODERS_PER_INCH);
        newRightTarget = motor2.getCurrentPosition() + (int) (rightInches * ENCODERS_PER_INCH);
        motor1.setTargetPosition(newLeftTarget);
        motor2.setTargetPosition(newRightTarget);
        motor3.setTargetPosition(newLeftTarget);
        motor4.setTargetPosition(newRightTarget);
        for (DcMotor m : motors){
            m.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            m.setPower(Math.abs(speed));
        }
        runTime.reset();
        while(opModeIsActive() && runTime.seconds() < timeOutS && (motor1.isBusy() || motor2.isBusy() || motor3.isBusy() || motor4.isBusy())){

        }
        for (DcMotor m : motors){
            m.setPower(0);
            m.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }


}
