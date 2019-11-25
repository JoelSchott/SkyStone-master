package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

@Autonomous (name = "Simple Test Auto")
public class PathTest extends LinearOpMode {

    MainBase base;

    static final double DRIVE_SPEED = 1;
    static final double TURN_SPEED = 0.4;

    private double distance = 12;

    @Override
    public void runOpMode(){

        base = new MainBase(hardwareMap, telemetry, this);
        base.init();

        telemetry.addLine("Resetting encoders");
        base.drivetrain.setModes(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        base.drivetrain.setModes(DcMotor.RunMode.RUN_USING_ENCODER);
        telemetry.update();

        telemetry.addLine("All Systems Go");
        telemetry.update();

        waitForStart();

        base.drivetrain.encoderDrive(DRIVE_SPEED, distance,distance,distance,distance, 6);

        telemetry.addLine(distance + " inches");
        telemetry.addData("front left encoder is ", base.drivetrain.frontLeft.getCurrentPosition());
        telemetry.addData("front right encoder is ", base.drivetrain.frontRight.getCurrentPosition());
        telemetry.addData("back left encoder is ", base.drivetrain.backLeft.getCurrentPosition());
        telemetry.addData("back right encoder is ", base.drivetrain.backRight.getCurrentPosition());
        double averageEncoders = (double)(base.drivetrain.frontLeft.getCurrentPosition() + base.drivetrain.frontRight.getCurrentPosition() +
                base.drivetrain.backRight.getCurrentPosition() + base.drivetrain.backLeft.getCurrentPosition())/4.0;
        telemetry.addData("average encoders are ", averageEncoders);

    }

}
