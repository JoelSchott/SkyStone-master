package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.SubsidiaryProjects.Training;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;


    public void init(HardwareMap map){
        frontLeft = map.dcMotor.get("frontLeft");
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight = map.dcMotor.get("frontRight");
        backLeft = map.dcMotor.get("backLeft");
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight = map.dcMotor.get("backRight");

    }





}
