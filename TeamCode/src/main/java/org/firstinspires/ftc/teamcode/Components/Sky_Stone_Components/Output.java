package org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.Core.RobotBase;
import org.firstinspires.ftc.robotcontroller.internal.Core.RobotComponent;

public class Output extends RobotComponent {
    public DcMotor lift;
    public Servo blockRotator;
    public Servo clamp;
    public Servo marker;

    public Output (RobotBase base){
        super(base);
        lift = base.getMapper().mapMotor("lift");
        blockRotator = base.getMapper().mapServo("rotator", Servo.Direction.FORWARD);
        clamp = base.getMapper().mapServo("clamp", Servo.Direction.FORWARD);
        marker = base.getMapper().mapServo("marker", Servo.Direction.FORWARD);
    }
    public void stop(){
        lift.setPower(0);
    }
    //This controls the lift motion upwards
    public void liftUp(double power){
        lift.setPower(power);

    }
    //This controls the lift motion downwards
    public void liftDown(double power){
        lift.setPower(-0.75);
    }
    //This controls the outtake system that moves the block outside of the robot
    public void outRotate(double power){
        blockRotator.setPosition(1.0);
    }
    //This rotates the outtake system back to the pick-up position
    public void inRotate(double power){
        blockRotator.setPosition(0.12);
    }
    //This system clamps onto the block and keeps it secure
    public void clampGrab(){
        clamp.setPosition(0.8);
    }
    //This system releases the clamp to the block
    public void clampRelease(){
        clamp.setPosition(0.0);
    }
    //This system moves the team marker clamper outwards to stack onto the block
    public void placeMarker(){
        marker.setPosition(1.0);
    }
    //This system moves the team marker clamper back inside the robot
    public void retractMarker(){
        marker.setPosition(0.0);
    }
}
