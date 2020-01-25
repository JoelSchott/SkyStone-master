package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Competition.Red;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomPhoneCameraSkyStone;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.FourWheelMecanum;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBaseWebcam;

import java.util.List;

@Autonomous(name = "Red Double Stone Encoders", group = "Autonomous")
public class RedDoubleStoneEncoders extends LinearOpMode {

    private MainBaseWebcam base;

    private final static double DRIVE_SPEED = 1.0;
    private static final double MAX_TURN_SPEED = 0.5;
    private final static double MINIMUM_TURN_SPEED = 0.1;
    private final static double DISTANCE_ADJUSTMENT_SPEED = 0.09;

    private final static double FIRST_DISTANCE = 26.80;
    private final static double FIRST_LEFT_DISTANCE = 16.84;
    private static final double COLLECTING_DISTANCE = 27.33;

    private static final double LEFT_BRIDGE_DISTANCE = 40;
    private static final double MIDDLE_BRIDGE_DISTANCE = 32;
    private static final double RIGHT_BRIDGE_DISTANCE =  24;

    private static final double LEFT_FIRST_DISTANCE_TO_WALL = 20.86;
    private static final double LEFT_SECOND_DISTANCE_TO_WALL = 1.32;

    private static final double MIDDLE_FIRST_DISTANCE_TO_WALL = 27;
    private static final double MIDDLE_SECOND_DISTANCE_TO_WALL = 8.2;

    private static final double RIGHT_FIRST_DISTANCE_TO_WALL = 34.4;
    private static final double RIGHT_SECOND_DISTANCE_TO_WALL = 14.3;

    private static final double AROUND_GATE_DISTANCE = 13;
    private static final double RIGHT_PARKING_DISTANCE = 5;
    private static final double PARKING_DISTANCE = 15;


    private CustomPhoneCameraSkyStone.SkyStonePosition position = CustomPhoneCameraSkyStone.SkyStonePosition.UNKNOWN;

    @Override
    public void runOpMode(){

        base = new MainBaseWebcam(hardwareMap,telemetry,this);
        base.init();
        base.drivetrain.setInitalAngle(180);

        telemetry.clearAll();
        telemetry.addLine("May the Force be with us");
        telemetry.update();

        waitForStart();

        base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 10.5, 4);

        base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, MAX_TURN_SPEED, 180, 2);

        base.getTelemetry().addData("front distance is ", base.frontRange.customDistanceInInches());
        base.getTelemetry().addData("left distance is ", base.leftRange.customDistanceInInches());
        base.getTelemetry().update();

        frontRangeDriveToDistance(FIRST_DISTANCE);
        leftRangeDriveToDistance(FIRST_LEFT_DISTANCE);

        base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.8, 180, 2);

        base.drivetrain.setPowers(0);

        sleep(800);

        List<Recognition> stones = base.webcam.getObjects();
        position = CustomPhoneCameraSkyStone.REDTwoStonesGetPosition(stones);
        if (position == CustomPhoneCameraSkyStone.SkyStonePosition.UNKNOWN){
            position = CustomPhoneCameraSkyStone.SkyStonePosition.MIDDLE;
        }

        for (Recognition stone : stones){
            double midPoint = (stone.getRight() + stone.getLeft())/2.0;
            telemetry.addLine("Saw " + stone.getLabel() + " with confidence " + stone.getConfidence() + " with midpoint " + midPoint);
        }
        telemetry.addData("Position is ",  position);
        telemetry.update();

        switch(position){
            case LEFT:

                //drives forward after seeing stones
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 4, 4);

                //strafes right next to the stones
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 14, 6);

                //drives to specific distance from both walls
                frontRangeDriveToDistance(LEFT_FIRST_DISTANCE_TO_WALL);
                leftRangeDriveToDistance(COLLECTING_DISTANCE);

                grabBlock();

                //drive left to go to building zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, AROUND_GATE_DISTANCE, 4);

                //drive to other zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, LEFT_BRIDGE_DISTANCE, 6);

                releaseBlock();

                //drives to second stone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, LEFT_BRIDGE_DISTANCE + 16, 8);

                //drives right next to blocks
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, AROUND_GATE_DISTANCE - 1.5, 5);

                //drives to distance from both walls
                frontRangeDriveToDistance(LEFT_SECOND_DISTANCE_TO_WALL);
                leftRangeDriveToDistance(COLLECTING_DISTANCE);

                base.drivetrain.straightEncoderDrive(0.2, FourWheelMecanum.Direction.FORWARD, 1.2, 1);

                grabBlock();

                //drives left to go to building zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, AROUND_GATE_DISTANCE, 6);

                //drives back to go to other zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, LEFT_BRIDGE_DISTANCE + 18, 8);

                //turns to face gate side of line
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, MAX_TURN_SPEED, 160, 5);

                //releases and parks
                base.arms.setRightPosition(0.1);
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 15, 5);

                break;

            case MIDDLE:

                //strafes right next to the stones
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 14, 6);

                //drives to specific distance from both walls
                frontRangeDriveToDistance(MIDDLE_FIRST_DISTANCE_TO_WALL);
                leftRangeDriveToDistance(COLLECTING_DISTANCE);

                grabBlock();

                //drive left to go to building zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, AROUND_GATE_DISTANCE, 4);

                //drive to other zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, MIDDLE_BRIDGE_DISTANCE, 6);

                releaseBlock();

                //drives to second stone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, MIDDLE_BRIDGE_DISTANCE + 16, 8);

                //drives right next to blocks
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, AROUND_GATE_DISTANCE - 1, 5);

                //drives to distance from both walls
                frontRangeDriveToDistance(MIDDLE_SECOND_DISTANCE_TO_WALL);
                leftRangeDriveToDistance(COLLECTING_DISTANCE);

                grabBlock();

                //drives left to go to building zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, AROUND_GATE_DISTANCE, 6);

                //drives back to go to other zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, MIDDLE_BRIDGE_DISTANCE + 16, 8);

                //turns to face gate side of line
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, MAX_TURN_SPEED, 160, 5);

                releaseBlock();

                //park
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 10, 2);

                break;

            case RIGHT:

                //drives back after seeing stones
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4.5, 4);

                //strafes right next to the stones
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 14, 6);

                //drives to specific distance from both walls
                frontRangeDriveToDistance(RIGHT_FIRST_DISTANCE_TO_WALL);
                leftRangeDriveToDistance(COLLECTING_DISTANCE);

                grabBlock();

                //drive left to go to building zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, AROUND_GATE_DISTANCE, 4);

                //drive to other zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, RIGHT_BRIDGE_DISTANCE, 6);

                releaseBlock();

                //drives to second stone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, RIGHT_BRIDGE_DISTANCE + 16, 8);

                //drives right next to blocks
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, AROUND_GATE_DISTANCE - 3, 5);

                //drives to distance from both walls
                frontRangeDriveToDistance(RIGHT_SECOND_DISTANCE_TO_WALL);
                leftRangeDriveToDistance(COLLECTING_DISTANCE);

                grabBlock();

                //drives left to go to building zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, AROUND_GATE_DISTANCE, 6);

                //drives back to go to other zone
                base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, RIGHT_BRIDGE_DISTANCE + 18, 8);

                //turns to face gate side of line
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, MAX_TURN_SPEED, 160, 5);

                releaseBlock();

                park();

                break;
        }

    }

    private void frontRangeDriveToDistance(double distance){
        if (base.frontRange.customDistanceInInches() > distance){
            while (base.frontRange.customDistanceInInches() > distance){
                base.drivetrain.setPowers(DISTANCE_ADJUSTMENT_SPEED);
            }
        }
        else if (base.frontRange.customDistanceInInches() < distance){
            while (base.frontRange.customDistanceInInches() < distance){
                base.drivetrain.setPowers(-DISTANCE_ADJUSTMENT_SPEED);
            }
        }
    }

    private void leftRangeDriveToDistance(double distance){
//        if (base.leftRange.customDistanceInInches() > distance){
//            while (base.leftRange.customDistanceInInches() > distance){
//                base.drivetrain.frontRight.setPower(DISTANCE_ADJUSTMENT_SPEED);
//                base.drivetrain.frontLeft.setPower(-DISTANCE_ADJUSTMENT_SPEED);
//                base.drivetrain.backLeft.setPower(DISTANCE_ADJUSTMENT_SPEED);
//                base.drivetrain.backRight.setPower(-DISTANCE_ADJUSTMENT_SPEED);
//            }
//        }
//        else if (base.leftRange.customDistanceInInches() < distance){
//            while (base.leftRange.customDistanceInInches() < distance){
//                base.drivetrain.frontRight.setPower(-DISTANCE_ADJUSTMENT_SPEED);
//                base.drivetrain.frontLeft.setPower(DISTANCE_ADJUSTMENT_SPEED);
//                base.drivetrain.backLeft.setPower(-DISTANCE_ADJUSTMENT_SPEED);
//                base.drivetrain.backRight.setPower(DISTANCE_ADJUSTMENT_SPEED);
//            }
//        }
    }

    private void grabBlock(){
        base.arms.setRightPosition(0.75);
        sleep(300);
    }

    private void releaseBlock(){
        base.arms.setRightPosition(0.1);
        sleep(300);
    }
    private void park(){
        base.drivetrain.straightEncoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, PARKING_DISTANCE, 5);
    }
}
