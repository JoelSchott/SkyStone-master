package org.firstinspires.ftc.robotcontroller.internal.Core.Sensors;

import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class I2cDeviceRange extends RobotSensor {

    public I2cDeviceRange(RobotBase base, String name, I2cAddr address){
        super (base, name);

        range = base().getMapper().mapI2cDevice(name);

        reader = new I2cDeviceSynchImpl(range, address, false);
        reader.engage();
    }

    private I2cDevice range;
    private byte[] rangeCache;
    private I2cDeviceSynch reader;


    public double distance(DistanceUnit INCH){

        rangeCache = reader.read(0x04,2);

        return rangeCache[0] & 0xFF;
    }

    public double getOpticalDistance(){

        rangeCache = reader.read(0x04, 2);
        return rangeCache[1] & 0xFF;
    }
}
