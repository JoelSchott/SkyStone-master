package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Competition;

import android.os.strictmode.DiskReadViolation;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.FourWheelMecanum;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

import java.util.List;

@Autonomous(name = "Red Foundation Bridge", group = "Autonomous")
public class RedFoundationBridge extends LinearOpMode {

    private MainBase base;

    private final static double DRIVE_SPEED = 1.0;
    private final static double MINIMUM_TURN_SPEED = 0.1;


    @Override
    public void runOpMode(){

        base = new MainBase(hardwareMap,telemetry,this);
        base.init();
        base.drivetrain.setInitalAngle(180);

        waitForStart();

        //strafe back and right to get in front of foundation
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_RIGHT, 24, 5);

        //rotates to starting angle
        base.drivetrain.gyroTurn(0.1,0.7, 180, 2);

        //strafes right to foundation
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.RIGHT, 26, 5);

        //slowly strafes flush to foundation
        base.drivetrain.encoderDrive(0.3, FourWheelMecanum.Direction.RIGHT, 6, 5);

        //grabs foundation
        base.foundation.moveServo(1);
        sleep(750);

        //strafes forward and left
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD_LEFT, 24, 5);

        //turns with foundation
        base.drivetrain.gyroTurn(0.1, 0.5, 90, 5);

        //places foundation in corner
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.BACK_RIGHT, 12, 4);

        //lets go of foundation
        base.foundation.moveServo(-1);
        sleep(500);
        base.foundation.moveServo(0);

        //drives forward and left for parking near bridge
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.FORWARD_LEFT, 20, 5);

        //strafes left to park near bridge
        base.drivetrain.encoderDrive(DRIVE_SPEED, FourWheelMecanum.Direction.LEFT, 20, 5);

    }
}
