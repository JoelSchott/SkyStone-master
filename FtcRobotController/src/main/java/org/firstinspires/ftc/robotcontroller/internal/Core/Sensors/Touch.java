package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.hardware.rev.RevTouchSensor;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;

/**
 * Created by pmkf2 on 1/6/2019.
 */

public class Touch extends RobotSensor {

    public Touch(RobotBase base, String name){
        super(base, name);

        touch = this.base().getMapper().mapTouch(name);
    }

    private RevTouchSensor touch ; //the actual sensor

    public boolean isPressed(){
        boolean result = false;
        if (touch != null){
            try{
                result = touch.isPressed();
            }
            catch(Exception e){
                base().getTelemetry().addLine("Touch sensor unplugged");
            }
        }

        return result;
    }
}
