package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

//@TeleOp(name = "Test TeleOp", group = "TeleOp")

public class TestTeleOp extends LinearOpMode {

    private CRServo rotator;

    public void runOpMode(){
        rotator = hardwareMap.crservo.get("rotator");

        waitForStart();

        rotator.setPower(gamepad1.right_stick_y);
    }
}
