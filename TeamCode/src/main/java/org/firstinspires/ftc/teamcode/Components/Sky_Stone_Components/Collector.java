package org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;

public class Collector extends RobotComponent {

    public DcMotor leftFlyWheel;
    public DcMotor rightFlyWheel;

    public Collector (RobotBase base){
        super(base);
        leftFlyWheel = base.getMapper().mapMotor("leftFly");
        rightFlyWheel = base.getMapper().mapMotor("rightFly", DcMotorSimple.Direction.REVERSE);
    }

    public void collect(double power){
        leftFlyWheel.setPower(power);
        rightFlyWheel.setPower(power);
    }

    public void spew(double power){
        leftFlyWheel.setPower(-power);
        rightFlyWheel.setPower(-power);
    }


    public void stop(){
        leftFlyWheel.setPower(0);
        rightFlyWheel.setPower(0);
    }
}
