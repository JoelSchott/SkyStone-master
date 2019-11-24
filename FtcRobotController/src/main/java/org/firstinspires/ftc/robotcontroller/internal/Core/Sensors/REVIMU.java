package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

/**
 * Created by pmkf2 on 9/1/2018.
 */

public class REVIMU extends RobotSensor
{
    public REVIMU(RobotBase base, String name, BNO055IMU.Parameters parameters){
        super(base, name);

        imu = base().getMapper().mapREVIMU(name, parameters);
        angle1Offset = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle;
        angle2Offset = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle;
        angle3Offset = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;
    }

    private BNO055IMU imu = null;
    private Acceleration accel = null;

    private double angle1, angle2, angle3;

    private double angle1Offset, angle2Offset, angle3Offset;
    /*
    Begins the tracking of accel.
     */

    public void startAccel()
    {
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    /*Writes to IMU.
     */
    public void write8(final BNO055IMU.Register REGISTER, final int BYTE)
    {
        imu.write8(REGISTER, BYTE);
    }

    //Calibrate to given offset
    public void calibrateTo(final int DEGREES)
    {
        angle1 = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle - angle1Offset;
        angle2 = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle - angle2Offset;
        angle3 = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle - angle3Offset;
        angle1Offset = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle - DEGREES;
        angle2Offset = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle - DEGREES;
        angle3Offset = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle - DEGREES;
    }

    //Return x angle from accelerometer.

    public double xAngle()
    {
        angle1 = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).firstAngle - angle1Offset;
        return angle1;
    }

    //Return y angle from accelerometer.
    public double yAngle()
    {
        angle2 = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).secondAngle - angle2Offset;
        return angle2;
    }

    //Return z angle from accelerometer.
    public double zAngle()
    {
        angle3 = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle - angle3Offset;
        return angle3;
    }

    //Return x accel from accelerometer.
    public double xAccel()
    {
        return accel.xAccel;
    }

    //Return x accel from accelerometer.
    public double yAccel()
    {
        return accel.yAccel;
    }

    //Return z accel from accelerometer.
    public double zAccel()
    {
        return accel.zAccel;
    }

}
