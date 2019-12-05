package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.configuration.ExpansionHubMotorControllerParamsState;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcore.internal.android.dex.Code;

public class MRGyro extends RobotSensor {

    public GyroSensor gyro;

    public MRGyro(RobotBase base, String name){
        super(base, name);
        gyro = base().getMapper().mapMRGyro(name);
    }

    public int heading(){
        return gyro.getHeading();
    }

}
