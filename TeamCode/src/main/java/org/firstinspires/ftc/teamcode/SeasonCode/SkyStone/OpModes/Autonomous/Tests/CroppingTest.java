package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomPhoneCameraSkyStone;

@Autonomous(name = "Cropping Test")
public class CroppingTest extends LinearOpMode {

    private CustomPhoneCameraSkyStone vision;

    public void runOpMode(){

        vision = new CustomPhoneCameraSkyStone(hardwareMap);
        vision.init();

        waitForStart();

        int[] croppings = CustomPhoneCameraSkyStone.REDCroppingPositions(vision.getObjects());

        while (opModeIsActive()){
            if (croppings != null){
                vision.getObjects(croppings[0],croppings[1],croppings[6],croppings[7]);
            }
        }


    }
}
