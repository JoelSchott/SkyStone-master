package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.robotcore.hardware.DigitalChannel;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;

/**
 * Created by pmkf2 on 12/5/2018.
 */

public final class REVMagnetic extends RobotSensor {

    public REVMagnetic(RobotBase base, String name){
        super(base, name);

        limitSwitch = base().getMapper().mapDigitalChannel(name);

    }

    private DigitalChannel limitSwitch; //the actual sensor

    public boolean isClose ()
    {
        return !this.limitSwitch.getState();
    }





}
