package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous;

import android.drm.DrmInfoEvent;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

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
    private final static double TURN_SPEED = 0.6;

    @Override
    public void runOpMode(){

        base = new MainBase(hardwareMap,telemetry,this);
        base.init();

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

        //strafes right and knocks stones out of the way
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 40, 6);

        //starts collecting
        base.collector.collect(0.8);


        //collects first stone
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 6, 4);
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 6, 4);

        //stops collecting
        base.collector.stop();

        //goes left to prepare to go under bridge
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 20, 5);

        //drives under bridge
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 40, 9);

        //turns right pi/2 radians
        base.drivetrain.encoderTurn(TURN_SPEED, Math.PI, 4);

        //spits out block
        base.collector.spew(1);
        sleep(600);

        //stops collector
        base.collector.stop();

        //turns back
        base.drivetrain.encoderTurn(TURN_SPEED, -Math.PI, 4);

        //drives to second skystone
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 48, 9);

        //strafes right to get in front of block
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 20, 6);

        //starts collecting
        base.collector.collect(1);

        //collects second stone
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 4, 2);
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK, 4, 2);

        //stops collecting
        base.collector.stop();

        //goes left to prepare to go under bridge
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 15, 5);

        //turns around
        base.drivetrain.encoderTurn(TURN_SPEED, Math.PI*2, 3);

        //drives to park on line
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD, 25, 4);

        //releases second block
        base.collector.spew(1);
        sleep(600);

        base.collector.stop();



    }
}
