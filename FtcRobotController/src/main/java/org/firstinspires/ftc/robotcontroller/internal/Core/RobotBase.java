package org.firstinspires.ftc.robotcontroller.internal.Core;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.HardwareMapper;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 *
 * Team 4964 Rolla Patriots
 *
 * Authors: Joel Schott and now Pranal Madria ;)
 * Created on 8/8/2018.
 */

public abstract class RobotBase
{
    protected HardwareMap hardwareMap;
    protected Telemetry telemetry;
    protected HardwareMapper mapper;
    protected LinearOpMode opMode;

    public RobotBase(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode mode){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.mapper = new HardwareMapper(this);
        this.opMode = mode;
    }

    //where components are instantiated and "this" can be passed in without being null
    //in the constructor "this" can be null
    public void init(){}


    public HardwareMap getHardwareMap(){
        return hardwareMap;
    }

    public Telemetry getTelemetry(){
        return telemetry;
    }

    public HardwareMapper getMapper(){
        return mapper;
    }

    public LinearOpMode getOpMode(){
        return opMode;
    }

    abstract public void stop();

}
