package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.GyroSensor;

//@TeleOp(name = "gyroSensor", group = "")
public class TestTeleOp extends LinearOpMode {

    private GyroSensor gyro;

    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        gyro = hardwareMap.gyroSensor.get("gyro");

        // Put initialization blocks here.
        waitForStart();
        if (opModeIsActive()) {
            // Put run blocks here.
            while (opModeIsActive()) {
                telemetry.addData("heading is", gyro.getHeading());
                // Put loop blocks here.
                telemetry.update();
            }
        }
    }
}