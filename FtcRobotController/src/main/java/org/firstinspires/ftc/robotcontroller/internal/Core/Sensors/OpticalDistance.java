package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;

/**
 * Created by pmkf2 on 12/5/2018.
 */

public class OpticalDistance extends RobotSensor
{
    public OpticalDistance(RobotBase base, String name){
        super(base, name);

        sensor = base().getMapper().mapODS(name);
    }

    private OpticalDistanceSensor sensor;


    public double rawLightDetected ()
    {

        double answer = -1;
        try{
            answer = sensor.getRawLightDetected()/sensor.getRawLightDetectedMax();
        }
        catch(Exception e){
            base().getTelemetry().addLine("Error with Optical Distance Sensor " + sensor.getDeviceName());
        }

        return answer;
    }

    public double lightDetected ()
    {
        double answer = -1;
        try{
            answer = sensor.getLightDetected();
        }
        catch(Exception e){e.printStackTrace();}

        return answer;
    }





}
