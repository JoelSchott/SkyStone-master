package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomSounds;

//@Autonomous(name = "Sound Test")
public class SoundTest extends LinearOpMode {

    CustomSounds sounds;

    public void runOpMode(){

        sounds = new CustomSounds(hardwareMap);
        telemetry.addLine("Waiting for you Cap.");
        telemetry.update();

        waitForStart();

        for (int i = 0; i < CustomSounds.SKYSTONE_SOUNDS.length; i++){
            sounds.playSkystoneSound(i);
            telemetry.addData("Playing sound with index ", i);
            telemetry.addData("sounds is called ", CustomSounds.SKYSTONE_SOUNDS[i]);
            telemetry.update();
            while(sounds.isPlayingSound()){

            }
        }

    }
}
