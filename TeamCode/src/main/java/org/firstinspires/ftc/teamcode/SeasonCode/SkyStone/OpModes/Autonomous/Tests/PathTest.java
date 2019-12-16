package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.FourWheelMecanum;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

@Autonomous (name = "Path Test")
public class PathTest extends LinearOpMode {

    MainBase base;

    static final double DRIVE_SPEED = 1;
    static final double TURN_SPEED = 0.4;

    private double distance = 48;

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

        base.drivetrain.setInitalAngle(0);

        waitForStart();

        base.drivetrain.driveTurn(-40, 0, 180, 0.1, 0.7, true);

    }

}
