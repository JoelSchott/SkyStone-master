package org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;

public class Output extends RobotComponent {

    public DcMotor lift;
    public CRServo blockRotator;
    public CRServo clamp;

    public Output (RobotBase base){
        super(base);
        lift = base.getMapper().mapMotor("lift");
        blockRotator = base.getMapper().mapCRServo("rotator", CRServo.Direction.FORWARD);
        clamp = base.getMapper().mapCRServo("clamp", CRServo.Direction.FORWARD);

    }
    public void stop(){
        lift.setPower(0);
        blockRotator.setPower(0);
        clamp.setPower(0);
    }

}
