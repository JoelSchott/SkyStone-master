package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.SubsidiaryProjects.Training;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@TeleOp(name = "Simple Drive Train", group = "TeleOp")

public class SimpleDriveTrain extends LinearOpMode {

    Robot robot = new Robot();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        telemetry.addLine("Hey, I'm ready to go! Push play to start.");
        telemetry.update();

        waitForStart();
        while(opModeIsActive()){
            double leftPower = gamepad1.left_stick_y + gamepad1.right_stick_x;
            double rightPower = gamepad1.left_stick_y - gamepad1.right_stick_x;
            double divideBy = 1.0;
            if (Math.abs(leftPower) > Math.abs(rightPower) && Math.abs(leftPower) > 1){
                divideBy = Math.abs(leftPower);
            }
            else if(Math.abs(rightPower) > Math.abs(leftPower) && Math.abs(rightPower) > 1){
                divideBy = Math.abs(rightPower);
            }
            leftPower = leftPower / divideBy;
            rightPower = rightPower / divideBy;

            robot.frontLeft.setPower(leftPower);
            robot.backLeft.setPower(leftPower);
            robot.frontRight.setPower(rightPower);
            robot.backRight.setPower(rightPower);

            telemetry.addData("Left Power is ", leftPower);
            telemetry.addData("Right Power is ", rightPower);
            telemetry.update();

        }
    }
}