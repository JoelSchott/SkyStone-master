package org.firstinspires.ftc.robotcontroller.internal.Core;

/**
 * Created by Computer on 9/8/2018.
 */

public abstract class RobotComponent
{
    public RobotComponent(RobotBase base){
        this.base = base;
    }

    protected RobotBase base;

    //Returns base object of the component
    public final RobotBase base()
    {

        return base;
    }

    public abstract void stop();
}
