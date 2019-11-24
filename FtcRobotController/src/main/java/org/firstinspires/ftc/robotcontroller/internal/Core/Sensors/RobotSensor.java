package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;

public abstract class RobotSensor {

    public RobotSensor(final RobotBase BASE, String name){
        this.base = BASE;
    }

    protected RobotBase base = null;

    public final RobotBase base(){
        return base;
    }
}
