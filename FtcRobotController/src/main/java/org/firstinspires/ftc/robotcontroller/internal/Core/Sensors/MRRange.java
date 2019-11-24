package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by pmkf2 on 12/5/2018.
 */

public final class MRRange extends RobotSensor
{
    public MRRange(RobotBase base, String name){
        super(base, name);

        range = base().getMapper().mapMRRange(name);
    }

    public MRRange(RobotBase base, String name, I2cAddr address){
        super(base, name);

        range = base().getMapper().mapMRRange(name, address);
    }

    private ModernRoboticsI2cRangeSensor range; //the actual sensor


    public double distance (final DistanceUnit UNIT)
    {
        double distance = range.getDistance(UNIT);

        if(distance == 0){
            distance = -1;
        }

        return distance;
    }





}
