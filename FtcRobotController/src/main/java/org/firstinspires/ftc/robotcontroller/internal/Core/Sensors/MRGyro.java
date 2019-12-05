package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;

public class MRGyro extends RobotSensor {

    private ModernRoboticsI2cGyro gyro;

    public MRGyro(RobotBase base, String name){
        super(base, name);
        gyro = base().getHardwareMap().get(ModernRoboticsI2cGyro.class, name);
    }

    public int heading(){
        return gyro.getHeading();
    }
    public int getIntegratedZ(){
        return gyro.getIntegratedZValue();
    }

}
