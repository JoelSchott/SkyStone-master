package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Competition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomPhoneCameraSkyStone;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomWebcamSkyStone;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.FourWheelMecanum;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

import java.util.List;

@Autonomous(name = "Red Stone", group = "Autonomous")
public class RedStone extends LinearOpMode {

    private MainBase base;

    private CustomWebcamSkyStone vision;

    private List<Recognition> stones;

    private final static double DRIVE_SPEED = 1.0;
    private final static double MINIMUM_TURN_SPEED = 0.1;
    private final static double DISTANCE_ADJUSTMENT_SPEED = 0.09;

    private final static double FIRST_DISTANCE = 30;

    private static final double LEFT_BRIDGE_DISTANCE = 29.2;
    private static final double MIDDLE_BRIDGE_DISTANCE = 25;
    private static final double RIGHT_BRIDGE_DISTANCE =  16;

    private static final double LEFT_FIRST_DISTANCE_TO_WALL = 32.5;
    private static final double LEFT_SECOND_DISTANCE_TO_WALL = 13;

    private static final double MIDDLE_FIRST_DISTANCE_TO_WALL = 39;
    private static final double MIDDLE_SECOND_DISTANCE_TO_WALL = 20.5;

    private static final double RIGHT_FIRST_DISTANCE_TO_WALL = 48.1;
    private static final double RIGHT_SECOND_DISTANCE_TO_WALL = 24;



    private static final int SPEW_TIME = 300;

    private CustomPhoneCameraSkyStone.SkyStonePosition location = CustomPhoneCameraSkyStone.SkyStonePosition.UNKNOWN;

    @Override
    public void runOpMode(){

        base = new MainBase(hardwareMap,telemetry,this);
        base.init();
        base.drivetrain.setInitalAngle(180);

        vision = new CustomWebcamSkyStone(hardwareMap);
        vision.init();

        telemetry.clearAll();
        telemetry.addLine("May the Force be with us");
        telemetry.update();

        waitForStart();

        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 13.5, 4);
        base.drivetrain.setCurrentAngleAs(180);

        base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.8, 180, 2);

        frontRangeDriveToDistance(FIRST_DISTANCE);

        base.drivetrain.setPowers(0);

        sleep(750);

        stones = vision.getObjects();
        if (stones != null){
            for (Recognition stone : stones){
                telemetry.addLine(String.format("stone type %s with confidence %f", stone.getLabel(), stone.getConfidence()));
                telemetry.addLine(String.format("LEFT, BOTTOM is %f, %f", stone.getLeft(), stone.getBottom()));
                telemetry.addLine(String.format("RIGHT, TOP is %f, %f", stone.getRight(), stone.getTop()));
                telemetry.addLine();
            }
        }
        location = CustomPhoneCameraSkyStone.REDTwoStonesGetPosition(vision.getObjects());
        telemetry.addData("Position is " , location.name());
        telemetry.update();


        switch(location){
            case LEFT:

                //drives back after seeing stones
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 2.4, 4);

                //strafes right next to the stones
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 8.5, 6);

                //rotates to initial angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.9, 180, 2);


                //drives to specific distance from the wall
                frontRangeDriveToDistance(LEFT_FIRST_DISTANCE_TO_WALL);

                //strafes to knock blocks out of the way
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 13, 4);

                //starts collecting
                base.collector.collect(0.8);


                //collects first stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 4, 4);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 2, 4);

                //stops collecting
                base.collector.collect(0.4);

                //goes left to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_LEFT, 39, 5);

                //turns back to starting angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.5, 180, 2);

                //drives under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 25, 9);

                //turns to face the foundation
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 90, 5);

                //spits out block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                //stops collector
                base.collector.stop();

                //turns back
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 6);

                //drives to second skystone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, LEFT_BRIDGE_DISTANCE + 22, 9);

                frontRangeDriveToDistance(LEFT_SECOND_DISTANCE_TO_WALL);


                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 3);

                //strafes right to get in front of block
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 6, 6);

                //starts collecting
                base.collector.collect(1);

                //collects second stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 4, 2);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4, 2);

                //stops collecting
                base.collector.collect(0.4);

                //goes left to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_LEFT, 45, 5);

                //drives to be in building zone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 31, 4);

                //turns to spit blocks
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 90,  7);

                //releases second block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                base.collector.stop();

                //strafes to park on line
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_LEFT, 6, 3);

                break;


            case MIDDLE:

                //drives back to line up
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 6, 6);

                //strafes right to be next to stones
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 8.5, 4);

                //rotates to initial angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.9, 180, 2);

                //drives to specific distance from the wall
                frontRangeDriveToDistance(MIDDLE_FIRST_DISTANCE_TO_WALL);

                //strafes to knock blocks out of the way
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 12, 4);

                //starts collecting
                base.collector.collect(0.8);


                //collects first stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 4);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 6, 4);

                // collects more slowly to maintain block
                base.collector.collect(0.5);

                //goes left to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 13, 5);

                //turns back to starting angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.5, 180, 2);

                //drives under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, MIDDLE_BRIDGE_DISTANCE, 9);

                //turns to face the foundation
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 90, 5);

                //spits out block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                //stops collector
                base.collector.stop();

                //turns back
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.6, 180, 6);

                //drives to second skystone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, MIDDLE_BRIDGE_DISTANCE + 18, 9);

                frontRangeDriveToDistance(MIDDLE_SECOND_DISTANCE_TO_WALL);

                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 3);

                //strafes right to get in front of block
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 10, 6);

                //starts collecting
                base.collector.collect(1);

                //collects second stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 2);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4, 2);

                //stops collecting
                base.collector.collect(0.5);

                //goes left to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_LEFT, 40, 5);

                //drives to be in building zone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 28, 4);

                //turns around
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 90,  7);

                //releases second block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                base.collector.stop();

                //parks on line
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 6.5, 3);

                break;

            case RIGHT:

                //strafes back and right to line up
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_RIGHT, 22, 6);

                //rotates to face forward
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.6, 180, 2);

                //drives back to distance
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 7.5, 4);

                //drives to specific distance from the wall
                frontRangeDriveToDistance(RIGHT_FIRST_DISTANCE_TO_WALL);

                //rotates to face block on the right
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.5,118,1);

                //starts collecting
                base.collector.collect(0.8);

                //collects first stone
                base.drivetrain.encoderDrive(0.5, FourWheelMecanum.Direction.FORWARD, 10, 4);
                base.drivetrain.encoderDrive(0.5, FourWheelMecanum.Direction.BACK, 16, 4);

                //collects more slowly to keep block in
                base.collector.collect(0.4);

                //turns to go under bridge
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,1, 90, 4);

                //strafes under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, RIGHT_BRIDGE_DISTANCE, 9);

                //spits out block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                //stops collector
                base.collector.stop();

                //turns back
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.7, 180, 6);

                //drives to second skystone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, RIGHT_BRIDGE_DISTANCE + 22, 9);

                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.6, 180, 3);

                //adjusts distance to wall
                frontRangeDriveToDistance(RIGHT_SECOND_DISTANCE_TO_WALL);

                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 3);

                //strafes right to get in front of block
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 10, 6);

                //starts collecting
                base.collector.collect(1);

                //collects second stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 2);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4, 2);

                //keeps block in
                base.collector.collect(0.4);

                //goes left to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_LEFT, 45, 5);

                //drives to be in building zone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 23, 4);

                //turns around
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 83,  7);

                //releases second block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                base.collector.stop();

                //strafes to park on line
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_LEFT, 14, 4);

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

}
