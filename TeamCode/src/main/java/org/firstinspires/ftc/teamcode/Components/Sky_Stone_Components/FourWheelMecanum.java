package org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.MRGyro;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.MRRange;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.REVIMU;

import java.util.ArrayList;
import java.util.Collection;

public class FourWheelMecanum extends RobotComponent {

    public DcMotor frontLeft;
    public DcMotor backLeft;
    public DcMotor frontRight;
    public DcMotor backRight;
    public DcMotor[] motors = new DcMotor[4];

    private REVIMU imu;
    private MRGyro gyro;
    private MRRange frontRange;
    private MRRange leftRange;

    private double stopBuffer = 0.05;


//    static final double COUNTS_PER_REVOLUTION = 801.7;
//    static final double WHEEL_REVOLUTIONS_PER_MOTOR_REVOLUTION = 0.5;
//    static final double WHEEL_DIAMETER = 4;
    static final double COUNTS_PER_INCH = 85.9;
    static final double ROBOT_RADIUS = 14.5;

    public AutoDrive autoDrive = new AutoDrive();
    public AutoTurn autoTurn = new AutoTurn();

    private ElapsedTime runTime = new ElapsedTime();

    private int initialAngle = 180;

    public enum Direction{
        FORWARD, FORWARD_RIGHT, RIGHT, BACK_RIGHT, BACK, BACK_LEFT, LEFT, FORWARD_LEFT
    }

    public enum DriveState{
        ACCELERATING,CRUISING,DECELERATING
    }


    public FourWheelMecanum(final RobotBase BASE){
        super(BASE);

        base().getTelemetry().addLine("about to map motors");
        base().getTelemetry().update();

        initMotors();

        base().getTelemetry().addLine("mapped motors");
        base().getTelemetry().update();

    }
    public FourWheelMecanum(final RobotBase BASE, REVIMU IMU){
        super(BASE);

        imu = IMU;
        initMotors();
    }

    public FourWheelMecanum(final RobotBase BASE, REVIMU IMU, MRGyro GYRO){
        super(BASE);
        imu = IMU;
        gyro = GYRO;
        initMotors();
    }

    public FourWheelMecanum(RobotBase BASE, REVIMU IMU, MRGyro GYRO, MRRange FRONT_RANGE){
        super(BASE);
        imu = IMU;
        gyro = GYRO;
        frontRange = FRONT_RANGE;
        initMotors();
    }

    public FourWheelMecanum(RobotBase BASE, REVIMU IMU, MRGyro GYRO, MRRange FRONT_RANGE, MRRange LEFT_RANGE){
        super(BASE);
        imu = IMU;
        gyro = GYRO;
        frontRange = FRONT_RANGE;
        leftRange = LEFT_RANGE;
        initMotors();
    }

    private void initMotors(){

        frontLeft = base().getMapper().mapMotor("frontLeft", DcMotorSimple.Direction.REVERSE);
        motors[0] = frontLeft;
        backLeft = base().getMapper().mapMotor("backLeft", DcMotorSimple.Direction.REVERSE);
        motors[1] = backLeft;
        frontRight = base().getMapper().mapMotor("frontRight");
        motors[2] = frontRight;
        backRight = base().getMapper().mapMotor("backRight");
        motors[3] = backRight;

        setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void robotRelativeDrive(double forward, double right, double turn){
        drive(forward,right,turn);
    }

    public void fieldRelativeDrive(double forward, double right, double turn){

        double angle = getProcessedAngle();
        angle = Math.toRadians(angle);

        double relativeForward = (Math.cos(angle)*right) + (Math.sin(angle)*forward);
        double relativeRight = (Math.sin(angle)*right) - (Math.cos(angle)*forward);

        drive(relativeForward, relativeRight, turn);
    }


    private void drive(double forward, double right, double turn){

        forward = getProcessedInput(forward);
        right = getProcessedInput(right);
        turn = getProcessedInput(turn);

        double leftFrontPower = forward + right + turn;
        double leftBackPower = forward - right + turn;
        double rightFrontPower = forward - right - turn;
        double rightBackPower = forward + right - turn;
        double[] powers = {leftFrontPower, leftBackPower, rightFrontPower, rightBackPower};

        boolean needToScale = false;
        for (double power : powers){
            if(Math.abs(power) > 1){
                needToScale = true;
                break;
            }
        }
        if (needToScale){
            double greatest = 0;
            for (double power : powers){
                if (Math.abs(power) > greatest){
                    greatest = Math.abs(power);
                }
            }
            leftFrontPower /= greatest;
            leftBackPower /= greatest;
            rightFrontPower /= greatest;
            rightBackPower /= greatest;
        }

        boolean stop = true;
        for (double power : powers){
            if (Math.abs(power) > stopBuffer){
                stop = false;
                break;
            }
        }
        if (stop){
            leftFrontPower = 0;
            leftBackPower = 0;
            rightFrontPower = 0;
            rightBackPower = 0;
        }

        frontLeft.setPower(leftFrontPower);
        base().getTelemetry().addData("Setting frontLeft power to " , leftFrontPower);
        backLeft.setPower(leftBackPower);
        base().getTelemetry().addData("Setting backLeft power to " , leftBackPower);

        frontRight.setPower(rightFrontPower);
        base().getTelemetry().addData("Setting frontRight power to " , rightFrontPower);

        backRight.setPower(rightBackPower);
        base().getTelemetry().addData("Setting rightBack power to " , rightBackPower);

    }

    public double[] getPowers(){
        double[] powers = {frontLeft.getPower(), backLeft.getPower(), frontRight.getPower(), backRight.getPower()};
        return powers;
    }

    public void setPowers(double[] powers){
        frontLeft.setPower(powers[0]);
        backLeft.setPower(powers[1]);
        frontRight.setPower(powers[2]);
        backRight.setPower(powers[3]);
    }
    public void setPowers(double power){
        for (DcMotor m : motors){
            m.setPower(power);
        }
    }

    public double getAverageEncoders(ArrayList<DcMotor> dcmotors){
        double sum = 0;
        for (DcMotor m : dcmotors){
            sum += Math.abs(m.getCurrentPosition());
        }
        return (sum/(double)(dcmotors.size()));
    }



    public void stop(){
        setPowers(0);
    }

    public void setModes(DcMotor.RunMode runMode){
        for (DcMotor motor : motors){
            motor.setMode(runMode);
        }
    }
    public void setZeroPowerBehaviors(DcMotor.ZeroPowerBehavior behavior){
        for (DcMotor motor: motors){
            motor.setZeroPowerBehavior(behavior);
        }
    }

    private double getProcessedInput(double hardInput){
        hardInput = Range.clip(hardInput, -1, 1);
        hardInput = Math.pow(hardInput, 3);
        return hardInput;
    }

    public void setInitalAngle(int angle){
        initialAngle = angle;
    }

    public void setCurrentAngleAs(int angle){
        initialAngle = angle - gyro.heading();
    }

    public double getProcessedAngle(){
        int angle = gyro.heading() + initialAngle;
        while (angle < 0){
            angle += 360;
        }
        angle = angle % 360;

        return angle;
    }

    public void encoderTurn(double speed, double radians, double timeOut){
        double distance = radians * ROBOT_RADIUS;
        encoderDrive(speed, distance, distance, -distance, -distance, timeOut);
    }

    public void gyroTurn(double minSpeed, double maxSpeed, double targetDegrees, double timeOut){
        boolean turnLeft;
        double absoluteError = Math.abs(targetDegrees - getProcessedAngle());

        if (targetDegrees > getProcessedAngle()){
            turnLeft = true;
        }
        else{
            turnLeft = false;
        }

        if (absoluteError > 180){
            turnLeft = !turnLeft;
        }

        if (absoluteError > 180){
            absoluteError = 360 - absoluteError;
        }
        runTime.reset();

        //2.5253 is about 2arctan(pi)
        double c = (maxSpeed - minSpeed)/2.5253;
        double theta = 60;
        double d = (2*3.1416/theta);
        double addition = (maxSpeed + minSpeed)/2.0;
        while (absoluteError > 1 && runTime.seconds() < timeOut && base().getOpMode().opModeIsActive()){
            absoluteError = Math.abs(targetDegrees - getProcessedAngle());
            if (absoluteError > 180){
                absoluteError = 360 - absoluteError;
            }
            //double power = (maxSpeed - minSpeed)*absoluteError/180.0 + minSpeed;
            //double power = (maxSpeed-minSpeed)*(Math.log(1+absoluteError))/(Math.log(181)) + minSpeed;
            //double power = (maxSpeed - minSpeed) * Math.pow(absoluteError/180.0, 2.0/5.0) + minSpeed;

            //I derived this all it works in theory
            //based on arctan function scaled from 0 to 180 with minSpeed at 0 and maxSpeed at theta
            double power = c * Math.atan(d*(absoluteError - (theta/2.0))) + addition;

            if (turnLeft){
                backLeft.setPower(-power);
                frontLeft.setPower(-power);
                backRight.setPower(power);
                frontRight.setPower(power);
            }
            else{
                backLeft.setPower(power);
                frontLeft.setPower(power);
                backRight.setPower(-power);
                frontRight.setPower(-power);
            }


        }
    }

    public void driveTurn(double forward, double right, double targetDegrees, double minTurnSpeed, double maxTurnSpeed, boolean turnLeft){

        setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setModes(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        double averageEncoders = 0;

        double distance = Math.sqrt(Math.pow(forward,2) + Math.pow(right,2));
        double absoluteError = Math.abs(targetDegrees - getProcessedAngle());
        if (absoluteError > 180){
            absoluteError = 360 - absoluteError;
        }

        while (averageEncoders / COUNTS_PER_INCH < distance || absoluteError > 2){

            averageEncoders = (Math.abs(frontRight.getCurrentPosition()) + Math.abs(frontLeft.getCurrentPosition()) +
                    Math.abs(backLeft.getCurrentPosition()) + Math.abs(backRight.getCurrentPosition())) / 4.0;

            double angle = getProcessedAngle();
            angle = Math.toRadians(angle);

            double relativeForward = (Math.cos(angle)*right) + (Math.sin(angle)*forward);
            double relativeRight = (Math.sin(angle)*right) - (Math.cos(angle)*forward);

            double scaledError = (distance - (averageEncoders / COUNTS_PER_INCH))/distance;
            if (relativeForward > 0.1 || relativeRight > 0.1){
                scaledError = Math.sqrt(scaledError);
            }
            else{
                distance = averageEncoders / COUNTS_PER_INCH;
                scaledError = 0;
            }
            relativeForward *= scaledError;
            relativeRight *= scaledError;

            absoluteError = Math.abs(targetDegrees - getProcessedAngle());
            if (absoluteError > 180){
                absoluteError = 360 - absoluteError;
            }

            double turnPower = (maxTurnSpeed - minTurnSpeed) * Math.pow(absoluteError/180.0, 2.0/5.0) + minTurnSpeed;
            if (absoluteError <= 2){
                turnPower = 0;
            }

            double leftFrontPower = relativeForward + relativeRight;
            double leftBackPower = relativeForward - relativeRight;
            double rightFrontPower = relativeForward - relativeRight;
            double rightBackPower = relativeForward + relativeRight;

            if (turnLeft){
                leftFrontPower += turnPower;
                leftBackPower += turnPower;
                rightFrontPower -= turnPower;
                rightBackPower -= turnPower;
            }
            else{
                leftFrontPower -= turnPower;
                leftBackPower -= turnPower;
                rightFrontPower += turnPower;
                rightBackPower += turnPower;
            }
            double[] powers = {leftFrontPower, leftBackPower, rightFrontPower, rightBackPower};
            double greatest = 0;
            for (double power : powers){
                if (Math.abs(power) > greatest){
                    greatest = Math.abs(power);
                }
            }
            if (greatest > 1){
                leftFrontPower /= greatest;
                leftBackPower /= greatest;
                rightFrontPower /= greatest;
                rightBackPower /= greatest;
            }


            frontLeft.setPower(leftFrontPower);
            backLeft.setPower(leftBackPower);
            frontRight.setPower(rightFrontPower);
            backRight.setPower(rightBackPower);

            base.getTelemetry().addData("Encoder inches are " , averageEncoders / COUNTS_PER_INCH);
            base.getTelemetry().addData("target distance is ", distance);
            base.getTelemetry().addData("absolute error is ", absoluteError);
            base.getTelemetry().addData("forward/right multiplier is " , scaledError);
            base.getTelemetry().addLine();
            base.getTelemetry().addData("relative forward is ", relativeForward);
            base.getTelemetry().addData("relative right is ", relativeRight);
            base.getTelemetry().addData("turn power is ", turnPower);
            base.getTelemetry().addLine();
            base.getTelemetry().addData("front right powers i ", rightFrontPower);
            base.getTelemetry().addData("front left power is ", leftFrontPower);
            base.getTelemetry().addData("back left power is ", leftBackPower);
            base.getTelemetry().addData("back right power is ", rightBackPower);
            base.getTelemetry().update();
        }



        stop();

    }

    public void straightEncoderDrive(double speed, Direction direction, double distance, double timeOut){
        if (direction == Direction.FORWARD || direction == Direction.RIGHT || direction == Direction.BACK || direction == Direction.LEFT){
            distance = distance * 1.4142;
        }
        if (direction == Direction.FORWARD){
            straightEncoderDrive(speed, distance, distance, distance, distance, timeOut);
        }
        else if (direction == Direction.FORWARD_LEFT){
            straightEncoderDrive(speed, 0, distance, distance, 0, timeOut);
        }
        else if (direction == Direction.LEFT){
            straightEncoderDrive(speed, -distance, distance, distance, -distance, timeOut);
        }
        else if (direction == Direction.BACK_LEFT){
            straightEncoderDrive(speed, -distance, 0, 0, -distance, timeOut);
        }
        else if (direction == Direction.BACK){
            straightEncoderDrive(speed, -distance, -distance, -distance, -distance, timeOut);
        }
        else if (direction == Direction.BACK_RIGHT){
            straightEncoderDrive(speed, 0, -distance, -distance, 0, timeOut);
        }
        else if (direction == Direction.RIGHT){
            straightEncoderDrive(speed, distance, -distance, -distance, distance, timeOut);
        }
        else if (direction == Direction.FORWARD_RIGHT){
            straightEncoderDrive(speed, distance, 0, 0, distance, timeOut);
        }
    }

    public void encoderDrive(double speed, Direction direction, double distance, double timeOut){
        if (direction == Direction.FORWARD || direction == Direction.RIGHT || direction == Direction.BACK || direction == Direction.LEFT){
            distance = distance * 1.4142;
        }
        if (direction == Direction.FORWARD){
            encoderDrive(speed, distance, distance, distance, distance, timeOut);
        }
        else if (direction == Direction.FORWARD_LEFT){
            encoderDrive(speed, 0, distance, distance, 0, timeOut);
        }
        else if (direction == Direction.LEFT){
            encoderDrive(speed, -distance, distance, distance, -distance, timeOut);
        }
        else if (direction == Direction.BACK_LEFT){
            encoderDrive(speed, -distance, 0, 0, -distance, timeOut);
        }
        else if (direction == Direction.BACK){
            encoderDrive(speed, -distance, -distance, -distance, -distance, timeOut);
        }
        else if (direction == Direction.BACK_RIGHT){
            encoderDrive(speed, 0, -distance, -distance, 0, timeOut);
        }
        else if (direction == Direction.RIGHT){
            encoderDrive(speed, distance, -distance, -distance, distance, timeOut);
        }
        else if (direction == Direction.FORWARD_RIGHT){
            encoderDrive(speed, distance, 0, 0, distance, timeOut);
        }
    }

    public void encoderDrive(double speed, Direction direction, double distance, double timeOut, double frontDistance){
        if (direction == Direction.FORWARD || direction == Direction.RIGHT || direction == Direction.BACK || direction == Direction.LEFT){
            distance = distance * 1.4142;
        }
        if (direction == Direction.FORWARD){
            encoderDrive(speed, distance, distance, distance, distance, timeOut, frontDistance);
        }
        else if (direction == Direction.FORWARD_LEFT){
            encoderDrive(speed, 0, distance, distance, 0, timeOut, frontDistance);
        }
        else if (direction == Direction.LEFT){
            encoderDrive(speed, -distance, distance, distance, -distance, timeOut, frontDistance);
        }
        else if (direction == Direction.BACK_LEFT){
            encoderDrive(speed, -distance, 0, 0, -distance, timeOut, frontDistance);
        }
        else if (direction == Direction.BACK){
            encoderDrive(speed, -distance, -distance, -distance, -distance, timeOut, frontDistance);
        }
        else if (direction == Direction.BACK_RIGHT){
            encoderDrive(speed, 0, -distance, -distance, 0, timeOut, frontDistance);
        }
        else if (direction == Direction.RIGHT){
            encoderDrive(speed, distance, -distance, -distance, distance, timeOut, frontDistance);
        }
        else if (direction == Direction.FORWARD_RIGHT){
            encoderDrive(speed, distance, 0, 0, distance, timeOut, frontDistance);
        }
    }

    public void gyroEncoderDrive(double speed, double forwardInches, double rightInches, double angle){
        setModes(DcMotor.RunMode.RUN_USING_ENCODER);
        int frontLeftCounts = (int) ((forwardInches + (1.414 * rightInches)) * COUNTS_PER_INCH);
        int frontRightCounts = (int) ((forwardInches - (1.414 * rightInches)) * COUNTS_PER_INCH);

        int frontLeftTarget = frontLeft.getCurrentPosition() + frontLeftCounts;
        int backRightTarget = backRight.getCurrentPosition() + frontLeftCounts;
        int frontRightTarget= frontRight.getCurrentPosition() + frontRightCounts;
        int backLeftTarget = backLeft.getCurrentPosition() + frontRightCounts;

        frontLeft.setTargetPosition(frontLeftTarget);
        backRight.setTargetPosition(backRightTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backLeft.setTargetPosition(backLeftTarget);

        setModes(DcMotor.RunMode.RUN_TO_POSITION);

        speed = Range.clip(Math.abs(speed), 0.0,1.0);
        frontLeft.setPower(speed);
        frontRight.setPower(speed);
        backLeft.setPower(speed);
        backRight.setPower(speed);

        while (base.getOpMode().opModeIsActive() && frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy()){
            double error = angle - gyro.gyro.getIntegratedZValue();
            while (error > 180){
                error -= 360;
            }
            while (error < -180){
                error += 360;
            }

            double steer = Range.clip(error * 0.1, -1.0, 1.0);
            if (forwardInches < 0){
                steer *= -1.0;
            }

            double leftSpeed = speed - steer;
            double rightSpeed = speed + steer;
            double max = Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed));
            if (max > 1.0){
                leftSpeed /= max;
                rightSpeed /= max;
            }

            frontLeft.setPower(leftSpeed);
            backLeft.setPower(leftSpeed);
            frontRight.setPower(rightSpeed);
            backRight.setPower(rightSpeed);

            double sumEncoderError = Math.abs(frontLeft.getCurrentPosition() - frontLeft.getTargetPosition())+
                    Math.abs(backLeft.getCurrentPosition() - backLeft.getTargetPosition()) +
                    Math.abs(frontRight.getCurrentPosition() - frontRight.getTargetPosition()) +
                            Math.abs(backRight.getCurrentPosition() - backRight.getTargetPosition());
            double sumInchesError = sumEncoderError / COUNTS_PER_INCH;
            if (sumInchesError < 2){
                break;
            }

            // Display drive status for the driver.
            base.getTelemetry().addData("Err/St",  "%5.1f/%5.1f",  error, steer);
            base.getTelemetry().addData("Target",  "%7d:%7d",      frontLeftCounts,  frontRightCounts);
            base.getTelemetry().addData("Actual",  "%7d:%7d",      frontLeft.getCurrentPosition(),
                    frontRight.getCurrentPosition());
            base.getTelemetry().addData("Speed",   "%5.2f:%5.2f",  leftSpeed, rightSpeed);
            base.getTelemetry().update();

        }
        setModes(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void encoderDrive(double speed, double xInches, double yInches, double timeout){
        double frontLeftDistance = yInches + (1.414 * xInches);
        double frontRightDistance = yInches - (1.414 * xInches);
        encoderDrive(speed, frontLeftDistance, frontRightDistance, frontRightDistance, frontLeftDistance, timeout);
    }

    public void straightEncoderDrive(double speed, double frontLeftInches, double backLeftInches, double frontRightInches, double backRightInches, double timeout){
        int frontLeftTarget = frontLeft.getCurrentPosition() + (int)(frontLeftInches * COUNTS_PER_INCH);
        int backLeftTarget = backLeft.getCurrentPosition() + (int)(backLeftInches * COUNTS_PER_INCH);
        int frontRightTarget = frontRight.getCurrentPosition() + (int)(frontRightInches * COUNTS_PER_INCH);
        int backRightTarget = backRight.getCurrentPosition() + (int)(backRightInches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(frontLeftTarget);
        backLeft.setTargetPosition(backLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backRight.setTargetPosition(backRightTarget);

        setModes(DcMotor.RunMode.RUN_TO_POSITION);

        runTime.reset();

        int initialHeading = gyro.heading();

        setPowers(Math.abs(speed));

        int busyMotors = 4;
        while (base().getOpMode().opModeIsActive() && (runTime.seconds() < timeout) &&
                (busyMotors > 1)){
            busyMotors = 0;
            if (frontLeft.isBusy()){
                busyMotors ++;
            }
            if (backLeft.isBusy()){
                busyMotors++;
            }
            if (frontRight.isBusy()){
                busyMotors++;
            }
            if(backRight.isBusy()){
                busyMotors++;
            }
            double sumEncoderError = Math.abs(frontLeft.getCurrentPosition() - frontLeft.getTargetPosition())+ Math.abs(backLeft.getCurrentPosition() - backLeft.getTargetPosition() +
                    Math.abs(frontRight.getCurrentPosition() - frontRight.getTargetPosition()) + Math.abs(backRight.getCurrentPosition() - backRight.getTargetPosition()));
            double sumInchesError = sumEncoderError / COUNTS_PER_INCH;
            if (sumInchesError < 0.65){
                break;
            }

            if (gyro.heading() != initialHeading){
                boolean turnLeft;
                if (gyro.heading() < initialHeading){
                    turnLeft = true;
                }
                else{
                    turnLeft = false;
                }
                if (Math.abs(gyro.heading() - initialHeading) > 180){
                    turnLeft = !turnLeft;
                }

                double scale = 0.8;
                if (turnLeft){
                    frontLeft.setPower(speed * scale);
                    backLeft.setPower(speed * scale);
                }
                else{
                    frontRight.setPower(speed * scale);
                    backRight.setPower(speed * scale);
                }
            }
            else{
                setPowers(Math.abs(speed));
            }

        }

        stop();
        setModes(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encoderDrive(double speed, double frontLeftInches, double backLeftInches, double frontRightInches, double backRightInches, double timeout){

        int frontLeftTarget = frontLeft.getCurrentPosition() + (int)(frontLeftInches * COUNTS_PER_INCH);
        int backLeftTarget = backLeft.getCurrentPosition() + (int)(backLeftInches * COUNTS_PER_INCH);
        int frontRightTarget = frontRight.getCurrentPosition() + (int)(frontRightInches * COUNTS_PER_INCH);
        int backRightTarget = backRight.getCurrentPosition() + (int)(backRightInches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(frontLeftTarget);
        backLeft.setTargetPosition(backLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backRight.setTargetPosition(backRightTarget);

        setModes(DcMotor.RunMode.RUN_TO_POSITION);

        runTime.reset();

        setPowers(Math.abs(speed));

        int busyMotors = 4;
        while (base().getOpMode().opModeIsActive() && (runTime.seconds() < timeout) &&
                (busyMotors > 1)){
            busyMotors = 0;
            if (frontLeft.isBusy()){
                busyMotors ++;
            }
            if (backLeft.isBusy()){
                busyMotors++;
            }
            if (frontRight.isBusy()){
                busyMotors++;
            }
            if(backRight.isBusy()){
                busyMotors++;
            }
            double sumEncoderError = Math.abs(frontLeft.getCurrentPosition() - frontLeft.getTargetPosition())+ Math.abs(backLeft.getCurrentPosition() - backLeft.getTargetPosition() +
                    Math.abs(frontRight.getCurrentPosition() - frontRight.getTargetPosition()) + Math.abs(backRight.getCurrentPosition() - backRight.getTargetPosition()));
            double sumInchesError = sumEncoderError / COUNTS_PER_INCH;
            if (sumInchesError < 0.65){
                break;
            }

        }

        stop();
        setModes(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public void encoderDrive(double speed, double frontLeftInches, double backLeftInches, double frontRightInches, double backRightInches, double timeout, double frontDistance){

        int frontLeftTarget = frontLeft.getCurrentPosition() + (int)(frontLeftInches * COUNTS_PER_INCH);
        int backLeftTarget = backLeft.getCurrentPosition() + (int)(backLeftInches * COUNTS_PER_INCH);
        int frontRightTarget = frontRight.getCurrentPosition() + (int)(frontRightInches * COUNTS_PER_INCH);
        int backRightTarget = backRight.getCurrentPosition() + (int)(backRightInches * COUNTS_PER_INCH);

        frontLeft.setTargetPosition(frontLeftTarget);
        backLeft.setTargetPosition(backLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backRight.setTargetPosition(backRightTarget);

        setModes(DcMotor.RunMode.RUN_TO_POSITION);

        runTime.reset();

        setPowers(Math.abs(speed));

        int busyMotors = 4;
        while (base().getOpMode().opModeIsActive() && (runTime.seconds() < timeout) &&
                (busyMotors > 1)){
            busyMotors = 0;
            if (frontLeft.isBusy()){
                busyMotors ++;
            }
            if (backLeft.isBusy()){
                busyMotors++;
            }
            if (frontRight.isBusy()){
                busyMotors++;
            }
            if(backRight.isBusy()){
                busyMotors++;
            }
            double sumEncoderError = Math.abs(frontLeft.getCurrentPosition() - frontLeft.getTargetPosition())+ Math.abs(backLeft.getCurrentPosition() - backLeft.getTargetPosition() +
                    Math.abs(frontRight.getCurrentPosition() - frontRight.getTargetPosition()) + Math.abs(backRight.getCurrentPosition() - backRight.getTargetPosition()));
            double sumInchesError = sumEncoderError / COUNTS_PER_INCH;
            if (sumInchesError < 0.65){
                break;
            }
            if (Math.abs(frontDistance - frontRange.customDistanceInInches()) < 1.5){
                break;
            }

        }

        setModes(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    public class AutoDrive{

        private double targetDistance;
        private Direction direction;
        private DriveState state;
        private double timeOut;

        private long timeLastCalled = -1;
        private long timeFirstCalled = -1;
        private double initialAngle;
        private double dt;

        private double maxVelocity = 20;
        private double acceleration = 1;
        private double minimumPower = 0.2;

        private ArrayList<DcMotor> usedMotors = new ArrayList<>();

        public void setDriveTarget(double distance, FourWheelMecanum.Direction d, double time){

            timeLastCalled = -1;
            usedMotors.clear();
            targetDistance = distance;
            initialAngle = getProcessedAngle();
            direction = d;
            timeOut = time;

            if (direction == Direction.FORWARD || direction == Direction.RIGHT || direction == Direction.BACK || direction == Direction.LEFT){
                for (DcMotor m : motors){
                    usedMotors.add(m);
                }
            }
            else if (direction == Direction.FORWARD_LEFT || direction == Direction.BACK_RIGHT){
                usedMotors.add(frontLeft);
                usedMotors.add(backRight);
            }
            else{
                usedMotors.add(frontRight);
                usedMotors.add(backLeft);
            }

            for (DcMotor motor : motors) {
                motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }

            state = DriveState.ACCELERATING;
        }

        public boolean driveToTarget(){

            if (timeLastCalled == -1){
                timeLastCalled = System.currentTimeMillis() - 20;
                timeFirstCalled = System.currentTimeMillis();
            }
            dt = (System.currentTimeMillis() - timeLastCalled)/(1000.0);
            timeLastCalled = System.currentTimeMillis();

            double averageEncoders = getAverageEncoders(usedMotors);

            double distance = averageEncoders/COUNTS_PER_INCH;
            double error = targetDistance - distance;

            base().getTelemetry().addData("dt is ", dt);
            base().getTelemetry().addLine("");

            base().getTelemetry().addData("error is ", error);

            if (error < 1 || System.currentTimeMillis() - timeFirstCalled > (timeOut * 1000)){
                stop();
                base().getTelemetry().addLine("Leaving drive to target now");
                base().getTelemetry().update();
                return true;
            }

            double power = Math.abs(usedMotors.get(0).getPower());
            double currentVelocity = power*maxVelocity;
            double stoppingDistance = (currentVelocity * currentVelocity)/(2.0 * (acceleration*maxVelocity));

            base.getTelemetry().addData("power is ", power);
            base.getTelemetry().addData("current velocity is ", currentVelocity);
            base.getTelemetry().addData("acceleration is ", acceleration);
            base.getTelemetry().addData("stopping distance is ", stoppingDistance);
            base.getTelemetry().addData("state is ", state);



            switch(state){
                case ACCELERATING:
                    if (error <= stoppingDistance){
                        state = DriveState.DECELERATING;
                        power -= (acceleration * dt);
                    }
                    else if (power >= 1){
                        power = 1;
                        state = DriveState.CRUISING;
                    }
                    else{
                        base.getTelemetry().addLine("Increasing speed now");
                        power += (acceleration * dt);
                    }
                    break;
                case CRUISING:
                    if (error <= stoppingDistance){
                        state = DriveState.DECELERATING;
                        power -= (acceleration * dt);
                    }
                    break;

                case DECELERATING:
                    if (power <= minimumPower){
                        power = minimumPower;
                    }
                    else{
                        power -= (acceleration * dt);
                    }
                    break;

            }
            base().getTelemetry().addData("Setting power to ", power);


            switch(direction){
                case FORWARD:
                    frontLeft.setPower(power);
                    backLeft.setPower(power);
                    frontRight.setPower(power);
                    backRight.setPower(power);
                    break;
                case FORWARD_RIGHT:
                    frontLeft.setPower(power);
                    backRight.setPower(power);
                    break;
                case RIGHT:
                    frontLeft.setPower(power);
                    backLeft.setPower(-power);
                    frontRight.setPower(-power);
                    backRight.setPower(power);
                    break;
                case BACK_RIGHT:
                    backLeft.setPower(-power);
                    frontRight.setPower(-power);
                    break;
                case BACK:
                    frontLeft.setPower(-power);
                    backLeft.setPower(-power);
                    frontRight.setPower(-power);
                    backRight.setPower(-power);
                    break;
                case BACK_LEFT:
                    frontLeft.setPower(-power);
                    backRight.setPower(-power);
                    break;
                case LEFT:
                    frontLeft.setPower(-power);
                    backLeft.setPower(power);
                    frontRight.setPower(power);
                    backRight.setPower(-power);
                case FORWARD_LEFT:
                    backLeft.setPower(power);
                    frontRight.setPower(power);
                    break;
            }

            return false;

        }
    }

    public class AutoTurn{

        public double targetAngle;
        private double timeOut;

        private long timeFirstCalled = -1;

        double minimumPower = 0.15;

        public void setTurnTarget(double angle, double time){
            targetAngle = angle;
            timeOut = time;
        }

        public boolean turnToTarget(){

            if (timeFirstCalled == -1){
                timeFirstCalled = System.currentTimeMillis();
            }
            boolean turnLeft = false;
            double currentAngle = getProcessedAngle();
            double error = currentAngle - targetAngle;

            if (Math.abs(error) < 2 || System.currentTimeMillis() - timeFirstCalled > timeOut){
                stop();
                return true;
            }

            if (Math.abs(error) <= 180){
                if (currentAngle > targetAngle){
                    turnLeft = true;
                }
                else{
                    turnLeft = false;
                }
            }
            else{
                if (currentAngle > targetAngle){
                    turnLeft = false;
                }
                else{
                    turnLeft = true;
                }
            }

            if (error > 180){
                error = 360 - error;
            }

            double power = (error/180) * (1 - minimumPower) + minimumPower;

            if (turnLeft){
                frontLeft.setPower(power);
                backLeft.setPower(power);
                frontRight.setPower(-power);
                backRight.setPower(-power);
            }
            else{
                frontLeft.setPower(-power);
                backLeft.setPower(-power);
                frontRight.setPower(power);
                backRight.setPower(power);
            }

            return false;
        }
    }
}
