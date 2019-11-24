package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.REVIMU;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.Collector;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.Foundation;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.FourWheelMecanum;

public class MainBase extends RobotBase {

    public FourWheelMecanum drivetrain;
    public Foundation foundation;
    public Collector collector;

    public REVIMU imu;

    private RobotComponent[] components = new RobotComponent[3];

    public MainBase(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode mode){

        super(hardwareMap, telemetry, mode);
        telemetry.addLine("creating Main Base");
        telemetry.update();
    }

    @Override
    public void init(){

        BNO055IMU.Parameters params = new BNO055IMU.Parameters();

        // IMU parameters
        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        params.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        params.loggingEnabled = true;
        params.loggingTag = "IMU";
        imu = new REVIMU(this,"imu", params);
        imu.calibrateTo(0);
        telemetry.addLine("created imu");
        telemetry.update();

        drivetrain = new FourWheelMecanum(this, imu);
        components[0] = drivetrain;
        telemetry.addLine("created drivetrain");
        telemetry.update();

        foundation = new Foundation(this);
        components[1] = foundation;
        telemetry.addLine("created foundation");
        telemetry.update();

        collector = new Collector(this);
        components [2] = collector;
        telemetry.addLine("created collector");
        telemetry.update();
    }

    public void stop(){

        for (RobotComponent component : components){
            component.stop();
        }
    }


}
