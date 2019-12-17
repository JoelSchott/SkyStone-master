package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Competition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomPhoneCameraSkyStone;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomWebcamSkyStone;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.FourWheelMecanum;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

import java.util.List;

@Autonomous(name = "Blue Stone", group = "Autonomous")
public class BlueDoubleStone extends LinearOpMode {

    private MainBase base;

    private CustomWebcamSkyStone vision;

    private List<Recognition> stones;

    private final static double DRIVE_SPEED = 1.0;
    private final static double MINIMUM_TURN_SPEED = 0.1;
    private final static double DISTANCE_ADJUSTMENT_SPEED = 0.09;

    private final static double FIRST_DISTANCE = 38;

    private static final double RIGHT_BRIDGE_DISTANCE = 29.2;
    private static final double MIDDLE_BRIDGE_DISTANCE = 25;
    private static final double LEFT_BRIDGE_DISTANCE =  17;

    private static final double RIGHT_FIRST_DISTANCE_TO_WALL = 33;
    private static final double RIGHT_SECOND_DISTANCE_TO_WALL = 14;

    private static final double MIDDLE_FIRST_DISTANCE_TO_WALL = 40;
    private static final double MIDDLE_SECOND_DISTANCE_TO_WALL = 20.5;

    private static final double LEFT_FIRST_DISTANCE_TO_WALL = 50.2;
    private static final double LEFT_SECOND_DISTANCE_TO_WALL = 27;



    private static final int SPEW_TIME = 300;

    private CustomPhoneCameraSkyStone.SkyStonePosition location = CustomPhoneCameraSkyStone.SkyStonePosition.UNKNOWN;

    @Override
    public void runOpMode(){

        base = new MainBase(hardwareMap,telemetry,this);
        base.init();
        base.drivetrain.setInitalAngle(0);

        vision = new CustomWebcamSkyStone(hardwareMap);
        vision.init();

        telemetry.clearAll();
        telemetry.addLine("May the Force be with us");
        telemetry.update();

        waitForStart();

        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 13.5, 4);

        base.drivetrain.setCurrentAngleAs(0);
        base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.8, 0, 2);

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
        location = CustomPhoneCameraSkyStone.BLUETwoStonesGetPosition(vision.getObjects());
        telemetry.addData("Position is " , location.name());
        telemetry.update();
        if (location == CustomPhoneCameraSkyStone.SkyStonePosition.UNKNOWN){
            location = CustomPhoneCameraSkyStone.SkyStonePosition.MIDDLE;
        }

        base.drivetrain.gyroTurn(0.4, 1, 170, 4);
        base.drivetrain.gyroTurn(0.1,0.2, 180, 3);

        switch(location){
            case RIGHT:

                //strafes left next to the stones
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 8.5, 6);

                //rotates to initial angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.9, 180, 2);

                //drives to specific distance from the wall
                frontRangeDriveToDistance(RIGHT_FIRST_DISTANCE_TO_WALL);

                //strafes to knock blocks out of the way
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 13, 4);

                //starts collecting
                base.collector.collect(0.8);


                //collects first stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 4, 4);

                //keeps collecting to keep block in
                base.collector.collect(0.4);

                //goes back and right to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_RIGHT, 42, 5);

                //turns back to starting angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.5, 180, 2);

                //drives under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 18, 9);

                //turns to face the foundation
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 270, 5);

                //spits out block
                base.collector.spew(0.25);
                sleep(SPEW_TIME);

                //stops collector
                base.collector.stop();

                //turns back
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 184, 6);

                //drives to second skystone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, RIGHT_BRIDGE_DISTANCE + 15, 9, RIGHT_SECOND_DISTANCE_TO_WALL);

                frontRangeDriveToDistance(RIGHT_SECOND_DISTANCE_TO_WALL);


                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 3);

                //strafes left to get in front of block
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 12.5, 6);

                //starts collecting
                base.collector.collect(1);

                //collects second stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 4, 2);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4, 2);

                //keeps collecting block
                base.collector.collect(0.4);

                //goes back and right to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_RIGHT, 34, 5);

                //drives to be in building zone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 36, 4);

                //turns to spit blocks
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 270,  7);

                //releases second block
                base.collector.spew(0.25);
                sleep(SPEW_TIME);

                base.collector.stop();

                //strafes to park on line
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 16, 3);

                break;


            case MIDDLE:

                //drives back to line up
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 7, 6);

                //strafes left to be next to stones
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 8.5, 4);

                //rotates to initial angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.9, 180, 2);

                //drives to specific distance from the wall
                frontRangeDriveToDistance(MIDDLE_FIRST_DISTANCE_TO_WALL);

                //strafes to knock blocks out of the way
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 12, 4);

                //starts collecting
                base.collector.collect(0.8);


                //collects first stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 4);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 6, 4);

                //keeps block in
                base.collector.collect(0.4);

                //goes right to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 15, 5);

                //turns back to starting angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.5, 180, 2);

                //drives under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, MIDDLE_BRIDGE_DISTANCE, 9);

                //turns to face the foundation
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 270, 5);

                //spits out block
                base.collector.spew(0.25);
                sleep(SPEW_TIME);

                //stops collector
                base.collector.stop();

                //turns back
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 182, 6);

                //drives to second skystone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, MIDDLE_BRIDGE_DISTANCE + 18, 9, MIDDLE_SECOND_DISTANCE_TO_WALL);

                frontRangeDriveToDistance(MIDDLE_SECOND_DISTANCE_TO_WALL);

                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 3);

                //strafes left to get in front of block
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 12.5, 6);

                //starts collecting
                base.collector.collect(1);

                //collects second stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 2);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4, 2);

                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 2);

                //keeps block in
                base.collector.collect(0.4);

                //goes back and right to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_RIGHT, 36, 5);

                //drives to be in building zone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 34, 4);

                //turns around
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 269,  7);

                //releases second block
                base.collector.spew(0.25);
                sleep(SPEW_TIME);

                base.collector.stop();

                //parks on line
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 14, 3);

                break;

            case LEFT:

                //strafes back and left to line up
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_LEFT, 24.7, 6);

                //rotates to face forward
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.6, 180, 2);

                //drives back to distance
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 7, 4);

                //drives to specific distance from the wall
                frontRangeDriveToDistance(LEFT_FIRST_DISTANCE_TO_WALL);

                //rotates to face block on the left
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.5,255,1);

                //starts collecting
                base.collector.collect(0.8);

                //collects first stone
                base.drivetrain.encoderDrive(0.5, FourWheelMecanum.Direction.FORWARD, 10, 4);
                base.drivetrain.encoderDrive(0.5, FourWheelMecanum.Direction.BACK, 11, 4);

                //keeps collecting to keep block in
                base.collector.collect(0.4);

                //turns to go under bridge
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,1, 270, 4);

                //strafes under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, LEFT_BRIDGE_DISTANCE, 9);

                //spits out block
                base.collector.spew(0.25);
                sleep(SPEW_TIME);

                //stops collector
                base.collector.stop();

                //turns back
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.7, 182, 6);

                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.6, 180, 3);

                //drives to second skystone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, LEFT_BRIDGE_DISTANCE + 20, 9, LEFT_SECOND_DISTANCE_TO_WALL);

                //adjusts distance to wall
                frontRangeDriveToDistance(LEFT_SECOND_DISTANCE_TO_WALL);

                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 3);

                //strafes left to get in front of block
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 14, 6);

                //starts collecting
                base.collector.collect(1);

                //collects second stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 2);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4, 2);

                //keeps block in
                base.collector.collect(0.4);

                //goes back and right to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_RIGHT, 45, 5);

                //drives to be in building zone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 23, 4);

                //turns around
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 270,  7);

                //releases second block
                base.collector.spew(0.25);
                sleep(SPEW_TIME);

                base.collector.stop();

                //strafes to park on line
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 16, 4);

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
