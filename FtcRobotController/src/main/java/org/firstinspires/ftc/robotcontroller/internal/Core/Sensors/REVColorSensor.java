package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Created by pmkf2 on 9/1/2018.
 */

public class REVColorSensor extends RobotSensor
{
    public REVColorSensor(RobotBase base, String name){
        super(base, name);

        distanceSensor = base().getMapper().mapDistanceSensor(name);
        colorSensor = base().getMapper().mapColorSensor(name);
    }

    private DistanceSensor distanceSensor;
    private ColorSensor colorSensor;

    public double distance(DistanceUnit unit){
        return (distanceSensor.getDistance(unit));
    }
    public int lightAmount(){
        return colorSensor.alpha();
    }
    public int red(){
        return colorSensor.red();
    }
    public int green(){
        return colorSensor.green();
    }
    public int blue(){
        return colorSensor.blue();
    }
    public int hue(){
        return colorSensor.argb();
    }
    public void turnOnLED(boolean flag){
        colorSensor.enableLed(flag);
    }

}
