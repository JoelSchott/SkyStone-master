package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.MRGyro;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.MRRange;
import org.firstinspires.ftc.robotcontroller.internal.Core.Sensors.REVIMU;
import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomWebcamSkyStone;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.Collector;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.Foundation;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.FourWheelMecanum;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.Output;

public class MainBaseWebcam extends RobotBase {

    public FourWheelMecanum drivetrain;
    public Foundation foundation;
    public Collector collector;
    public Output output;

    public REVIMU imu;
    public MRGyro gyro;
    public MRRange frontRange;

    public CustomWebcamSkyStone webcam;

    private RobotComponent[] components = new RobotComponent[4];

    public MainBaseWebcam(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode mode){

        super(hardwareMap, telemetry, mode);
        telemetry.addLine("creating Main Base");
        telemetry.update();
    }

    @Override
    public void init(){

        webcam = new CustomWebcamSkyStone(hardwareMap);
        webcam.init();

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

        gyro = new MRGyro(this,"gyro");
        telemetry.addLine("created gyro");
        telemetry.update();

        frontRange = new MRRange(this, "frontRange");
        telemetry.addLine("Made front range");
        telemetry.update();

        drivetrain = new FourWheelMecanum(this, imu, gyro, frontRange);
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

        output = new Output(this);
        components[3] = output;
        telemetry.addLine("created output");
        telemetry.update();
    }

    public void stop(){

        for (RobotComponent component : components){
            component.stop();
        }
    }


}

