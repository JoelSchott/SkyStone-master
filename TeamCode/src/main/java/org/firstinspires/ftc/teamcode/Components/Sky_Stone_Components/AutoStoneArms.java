package org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;

public class AutoStoneArms extends RobotComponent{

    public CRServo leftArm;
    public Servo rightArm;

    public AutoStoneArms(RobotBase base){
        super(base);
        leftArm = base.getMapper().mapCRServo("leftArm", CRServo.Direction.FORWARD);
        rightArm = base.getMapper().mapServo("rightArm", Servo.Direction.REVERSE);
        rightArm.setPosition(0.1);
    }

    public void setLeftPower(double power){
        leftArm.setPower(power);
    }
    public void setRightPosition(double position){
        rightArm.setPosition(position);
    }


    public void stop(){
        leftArm.setPower(0);
        rightArm.setPosition(rightArm.getPosition());
    }
}

