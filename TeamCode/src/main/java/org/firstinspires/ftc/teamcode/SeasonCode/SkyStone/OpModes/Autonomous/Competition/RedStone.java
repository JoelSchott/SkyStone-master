package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Competition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomTensorFlowSkyStone;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.FourWheelMecanum;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

import java.util.List;

@Autonomous(name = "Red Stone", group = "Autonomous")
public class RedStone extends LinearOpMode {

    private MainBase base;

    private CustomTensorFlowSkyStone vision;

    private List<Recognition> stones;

    private final static double DRIVE_SPEED = 1.0;
    private final static double MINIMUM_TURN_SPEED = 0.1;

    private static final double LEFT_BRIDGE_DISTANCE = 24;
    private static final double MIDDLE_BRIDGE_DISTANCE = 16;
    private static final double RIGHT_BRIDGE_DISTANCE =  8;

    private static final double LEFT_FIRST_DISTANCE_TO_WALL = 32.5;
    private static final double LEFT_SECOND_DISTANCE_TO_WALL = 26;

    private static final double MIDDLE_FIRST_DISTANCE_TO_WALL = 40.5;
    private static final double MIDDLE_SECOND_DISTANCE_TO_WALL = 34;

    private static final double RIGHT_FIRST_DISTANCE_TO_WALL = 48.5;
    private static final double RIGHT_SECOND_DISTANCE_TO_WALL = 42;



    private static final int SPEW_TIME = 300;

    private CustomTensorFlowSkyStone.SkyStonePosition location = CustomTensorFlowSkyStone.SkyStonePosition.UNKNOWN;

    @Override
    public void runOpMode(){

        base = new MainBase(hardwareMap,telemetry,this);
        base.init();
        base.drivetrain.setInitalAngle(180);

        vision = new CustomTensorFlowSkyStone(hardwareMap);


        waitForStart();

        //reads current vision input
        stones = vision.getObjects();
        if (stones != null){
            for (Recognition stone : stones){
                telemetry.addLine(String.format("stone type %s with confidence %f", stone.getLabel(), stone.getConfidence()));
                telemetry.addLine(String.format("LEFT, BOTTOM is %f, %f", stone.getLeft(), stone.getBottom()));
                telemetry.addLine(String.format("RIGHT, TOP is %f, %f", stone.getRight(), stone.getTop()));
                telemetry.addLine();
            }
        }
        telemetry.update();

        location = CustomTensorFlowSkyStone.getPosition(vision.getObjects());
        if (location == CustomTensorFlowSkyStone.SkyStonePosition.UNKNOWN){
            location = CustomTensorFlowSkyStone.SkyStonePosition.LEFT;
        }

        switch(location){
            case LEFT:
                //strafes right next to the stones
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 24, 6);

                //rotates to initial angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.9, 180, 2);

                //drives to specific distance from the wall
                while (base.frontRange.customDistanceInInches() > LEFT_FIRST_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(0.2);
                }
                while (base.frontRange.customDistanceInInches() < LEFT_FIRST_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(-0.2);
                }

                //strafes to knock blocks out of the way
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 10, 4);

                //starts collecting
                base.collector.collect(0.8);


                //collects first stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 4);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 6, 4);

                //stops collecting
                base.collector.stop();

                //goes left to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 17, 5);

                //turns back to starting angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.5, 180, 2);

                //drives under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, LEFT_BRIDGE_DISTANCE, 9);

                //turns to face the foundation
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 55, 5);

                //spits out block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                //stops collector
                base.collector.stop();

                //turns back
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 180, 6);

                //drives to second skystone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, LEFT_BRIDGE_DISTANCE, 9);

                while(base.frontRange.customDistanceInInches() > LEFT_SECOND_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(0.2);
                }
                while (base.frontRange.customDistanceInInches() < LEFT_SECOND_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(-0.2);
                }

                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 3);

                //strafes right to get in front of block
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD_RIGHT, 22, 6);

                //starts collecting
                base.collector.collect(1);

                //collects second stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 2);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4, 2);

                //stops collecting
                base.collector.stop();

                //goes left to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_LEFT, 30, 5);

                //drives to park on line
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 30, 4);

                //turns around
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 0,  7);

                //releases second block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                base.collector.stop();
                break;

            case MIDDLE:

                //strafes right and back to line up
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_RIGHT, 11, 6);

                //rotates to initial angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.5,180,1);

                //strafes right to be next to stones
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 18, 4);

                //rotates to initial angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.9, 180, 2);

                //drives to specific distance from the wall
                while (base.frontRange.customDistanceInInches() > MIDDLE_FIRST_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(0.2);
                }
                while (base.frontRange.customDistanceInInches() < MIDDLE_FIRST_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(-0.2);
                }

                //strafes to knock blocks out of the way
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 6, 4);

                //starts collecting
                base.collector.collect(0.8);


                //collects first stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 4);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 6, 4);

                //stops collecting
                base.collector.stop();

                //goes left to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 17, 5);

                //turns back to starting angle
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.5, 180, 2);

                //drives under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, MIDDLE_BRIDGE_DISTANCE, 9);

                //turns to face the foundation
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 50, 5);

                //spits out block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                //stops collector
                base.collector.stop();

                //turns back
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 180, 6);

                //drives to second skystone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, MIDDLE_BRIDGE_DISTANCE + 10, 9);

                while(base.frontRange.customDistanceInInches() > MIDDLE_SECOND_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(0.2);
                }
                while (base.frontRange.customDistanceInInches() < MIDDLE_SECOND_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(-0.2);
                }

                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 3);

                //strafes right to get in front of block
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD_RIGHT, 22, 6);

                //starts collecting
                base.collector.collect(1);

                //collects second stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 2);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4, 2);

                //stops collecting
                base.collector.stop();

                //goes left to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_LEFT, 30, 5);

                //drives to park on line
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 22, 4);

                //turns around
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 0,  7);

                //releases second block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                base.collector.stop();
                break;

            case RIGHT:

                //strafes right and back to line up
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_RIGHT, 20, 6);

                //strafes right to be closer to right block
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 20, 5);

                //drives to specific distance from the wall
                while (base.frontRange.customDistanceInInches() > RIGHT_FIRST_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(0.2);
                }
                while (base.frontRange.customDistanceInInches() < RIGHT_FIRST_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(-0.2);
                }

                //rotates to face block on the right
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.5,120,1);

                //starts collecting
                base.collector.collect(0.8);

                //collects first stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 10, 4);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 10, 4);

                //stops collecting
                base.collector.stop();

                //turns to go under bridge
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED,0.7, 20, 4);

                //drives under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, RIGHT_BRIDGE_DISTANCE, 9);

                //spits out block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                //stops collector
                base.collector.stop();

                //turns back
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 180, 6);

                //drives to second skystone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, RIGHT_BRIDGE_DISTANCE + 10, 9);

                while(base.frontRange.customDistanceInInches() > RIGHT_SECOND_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(0.2);
                }
                while (base.frontRange.customDistanceInInches() < RIGHT_SECOND_DISTANCE_TO_WALL){
                    base.drivetrain.setPowers(-0.2);
                }

                //straightens out
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 0.5, 180, 3);

                //strafes right to get in front of block
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD_RIGHT, 22, 6);

                //starts collecting
                base.collector.collect(1);

                //collects second stone
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 2);
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4, 2);

                //stops collecting
                base.collector.stop();

                //goes left to prepare to go under bridge
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_LEFT, 30, 5);

                //drives to park on line
                base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 22, 4);

                //turns around
                base.drivetrain.gyroTurn(MINIMUM_TURN_SPEED, 1, 0,  7);

                //releases second block
                base.collector.spew(1);
                sleep(SPEW_TIME);

                base.collector.stop();
                break;
        }

    }
}
