package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.SubsidiaryProjects;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "Encoder Test", group = "TeleOp")

public class EncoderTest extends LinearOpMode {

    private DcMotor frontLeft;

    private int encodersPerRotation = 1440;

    public void runOpMode(){

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //frontLeft.setTargetPosition(encodersPerRotation * 2);
        //frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        waitForStart();

        //frontLeft.setPower(1);

//        while (opModeIsActive() && frontLeft.isBusy())
//        {
//            telemetry.addData("encoder-fwd", frontLeft.getCurrentPosition() + "  busy=" + frontLeft.isBusy());
//            telemetry.update();
//        }
//        frontLeft.setPower(0);

            while (opModeIsActive()) {
                frontLeft.setPower(gamepad1.left_stick_y);
                telemetry.addData("frontLeft encoder ", frontLeft.getCurrentPosition());
                telemetry.update();
            }
    }
}
