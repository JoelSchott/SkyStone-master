package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

@Autonomous (name = "Encoder Octagon Auto")
public class EncoderOctagon extends LinearOpMode {

    MainBase base;

    static final double DRIVE_SPEED = 0.8;
    static final double TURN_SPEED = 0.4;

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

        base.drivetrain.encoderDrive(DRIVE_SPEED, 5,5,5,5, 6);
        base.drivetrain.encoderDrive(DRIVE_SPEED, 5,-5,-5,5, 4);

    }

}
