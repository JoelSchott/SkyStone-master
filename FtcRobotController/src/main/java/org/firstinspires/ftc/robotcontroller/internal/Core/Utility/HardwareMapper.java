package org.firstinspires.ftc.robotcontroller.internal.Core.Utility;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;

/*
All of our hardware will be mapped here.
*/
public final class HardwareMapper
{
    private RobotBase robot;

    /*
    The following constructor will assign the robot variable to the robot
    passed in as a parameter.
    */
    public HardwareMapper(RobotBase robotObj)
    {
        robot = robotObj;
    }

    /*
    Maps a motor with the name that is passed in as a parameter.
    The motor that will be mapped is returned.
    */
    public DcMotor mapMotor(final String NAME, final DcMotorSimple.Direction DIRECTION)
    {
        DcMotor tempMotor;
        tempMotor = robot.getHardwareMap().dcMotor.get(NAME);
        tempMotor.setDirection(DIRECTION);

        return tempMotor;
    }
    public DcMotor mapTetrixMotor(String NAME)
    {
        DcMotor tempMotor = null;
        tempMotor = robot.getHardwareMap().get(DcMotor.class, NAME);
        return tempMotor;
    }
    public DcMotor mapMotor(final String NAME)
    {
        DcMotor tempMotor;
        tempMotor = robot.getHardwareMap().dcMotor.get(NAME);

        return tempMotor;
    }

    /*
    Maps a servo with the name that is passed in as a parameter.
    The servo that will be mapped is returned.
    */
    public Servo mapServo(final String NAME, final Servo.Direction DIRECTION)
    {
        Servo tempServo;

        tempServo = robot.getHardwareMap().servo.get(NAME);
        tempServo.setDirection(DIRECTION);

        return tempServo;
    }

    /*
    Maps a CRServo with the name that is passed in as a parameter.
    The CRServo that will be mapped is returned.
    */
    public CRServo mapCRServo(final String NAME, final CRServo.Direction DIRECTION)
    {
        CRServo tempCRServo;

        tempCRServo = robot.getHardwareMap().crservo.get(NAME);
        tempCRServo.setDirection(DIRECTION);

        return tempCRServo;
    }
    /*
    Maps a colorsensor with the name and address that are passed in as parameters.
    The servo that will be mapped is returned.
     */
    public DistanceSensor mapDistanceSensor(final String NAME)
    {
        DistanceSensor tempColor;

        tempColor = robot.getHardwareMap().get(DistanceSensor.class, NAME);

        return tempColor;
    }

    public ColorSensor mapColorSensor(final String NAME)
    {
        ColorSensor tempColor;

        tempColor = robot.getHardwareMap().get(ColorSensor.class, NAME);

        return tempColor;
    }
    public GyroSensor mapMRGyro(final String NAME){
        GyroSensor tempGyro;

        tempGyro = robot.getHardwareMap().gyroSensor.get(NAME);

        return tempGyro;
    }

    public DigitalChannel mapDigitalChannel(final String NAME){
        DigitalChannel temp;

        temp = robot.getHardwareMap().get(DigitalChannel.class, NAME);

        return temp;
    }

    public I2cDevice mapI2cDevice(final String name){
        I2cDevice tempDevice;

        tempDevice = robot.getHardwareMap().i2cDevice.get(name);

        return tempDevice;
    }

    /*
    Maps a colorsensor with the name and address that are passed in as parameters.
    The servo that will be mapped is returned.
     */
//    public ColorSensor mapColorSensor(final String NAME)
//    {
//
//        return mapColorSensor(NAME,ColorSensor.ADDRESS_I2C_DEFAULT.get8Bit());
//    }*/

    /*
    Maps a REVIMU with the name and parameters that are passed in as parameters.
    The IMU that will be mapped is returned.
    */
    public BNO055IMU mapREVIMU(final String NAME , final BNO055IMU.Parameters PARAMETERS)
    {
        BNO055IMU tempIMU;                            // REV Internal IMU to be mapped

        tempIMU = robot.getHardwareMap().get(BNO055IMU.class , NAME);
        tempIMU.initialize(PARAMETERS);

        return tempIMU;
    }

    public ModernRoboticsI2cRangeSensor mapMRRange(final String NAME)
    {
        ModernRoboticsI2cRangeSensor tempRange;

        tempRange = robot.getHardwareMap().get(ModernRoboticsI2cRangeSensor.class, NAME);
        tempRange.initialize();

        try {
            tempRange = robot.getHardwareMap().get(ModernRoboticsI2cRangeSensor.class, NAME);
        }
        catch(Exception e){
        }
        return tempRange;
    }

    public ModernRoboticsI2cRangeSensor mapMRRange(final String NAME, final I2cAddr Address)
    {
        ModernRoboticsI2cRangeSensor tempRange;
        tempRange = robot.getHardwareMap().get(ModernRoboticsI2cRangeSensor.class, NAME);
        tempRange.setI2cAddress(Address);
        tempRange.initialize();

        try {
            tempRange = robot.getHardwareMap().get(ModernRoboticsI2cRangeSensor.class, NAME);
        }
        catch(Exception e){
        }
        return tempRange;
    }

    public OpticalDistanceSensor mapODS(final String NAME){

        OpticalDistanceSensor tempODS = null;

        try {
            tempODS = robot.getHardwareMap().get(ModernRoboticsI2cRangeSensor.class, NAME);
        }
        catch(Exception e){
        }

        return tempODS;
    }

    public RevTouchSensor mapTouch(final String NAME){

        RevTouchSensor tempTouch = null;
        try {
            tempTouch = robot.getHardwareMap().get(RevTouchSensor.class, NAME);
        }
        catch(Exception e){
        }
        return tempTouch;
    }

}
