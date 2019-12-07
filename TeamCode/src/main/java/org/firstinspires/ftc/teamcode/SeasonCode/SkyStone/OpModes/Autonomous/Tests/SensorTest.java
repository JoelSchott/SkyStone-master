package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Tests;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.MRRange;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

//@Autonomous(name = "Sensor Test")
public class SensorTest extends LinearOpMode {

    private BNO055IMU IMUsensor;
    private ModernRoboticsI2cRangeSensor range;

    public void runOpMode(){

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;


        IMUsensor = hardwareMap.get(BNO055IMU.class, "gyro");
        IMUsensor.initialize(parameters);
        telemetry.addLine("Successfully made IMU sensor");
        telemetry.update();

        range = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "range");
        telemetry.addLine("successfully made range sensor");
        telemetry.update();

        telemetry.addLine("Ready to go");
        telemetry.update();

        waitForStart();

        while(opModeIsActive()) {
            telemetry.addData("extrinsic x is", IMUsensor.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle);
            telemetry.addData("extrinsic y is", IMUsensor.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle);
            telemetry.addData("extrinsic z is", IMUsensor.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle);

            telemetry.addLine();
            telemetry.addData("intrinsic x is", IMUsensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle);
            telemetry.addData("intrinsic y is", IMUsensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle);
            telemetry.addData("intrinsic z is", IMUsensor.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle);

            telemetry.addLine();
            telemetry.addData("distance in inches detected is ", range.getDistance(DistanceUnit.INCH));
            telemetry.update();
        }

    }
}
