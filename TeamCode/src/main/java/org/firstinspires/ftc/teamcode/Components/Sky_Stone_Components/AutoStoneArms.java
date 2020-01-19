package org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;

public class AutoStoneArms extends RobotComponent{

    public CRServo leftArm;
    public CRServo rightArm;

    public AutoStoneArms(RobotBase base){
        super(base);
        leftArm = base.getMapper().mapCRServo("leftArm", CRServo.Direction.FORWARD);
        rightArm = base.getMapper().mapCRServo("rightArm", CRServo.Direction.REVERSE);
    }

    public void setLeftPower(double power){
        leftArm.setPower(power);
    }
    public void setRightPower(double power){
        rightArm.setPower(power);
    }


    public void stop(){
        leftArm.setPower(0);
        rightArm.setPower(0);
    }
}

