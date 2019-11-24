package org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;

public class Foundation extends RobotComponent {
    public CRServo leftServo;
    public CRServo rightServo;

    public Foundation(RobotBase base){
        super(base);
        leftServo = base().getMapper().mapCRServo("leftServo", CRServo.Direction.FORWARD);
        rightServo = base().getMapper().mapCRServo("rightServo", CRServo.Direction.REVERSE);
    }

    public void setLeftPower(double power){
        leftServo.setPower(power);
    }
    public void setRightPower(double power){
        rightServo.setPower(power);
    }

    public void stop(){
        leftServo.setPower(0);
        rightServo.setPower(0);
    }

}
