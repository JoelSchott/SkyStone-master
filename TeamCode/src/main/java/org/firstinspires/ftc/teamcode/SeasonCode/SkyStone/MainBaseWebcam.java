package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomWebcamSkyStone;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MainBaseWebcam extends MainBase{

    public CustomWebcamSkyStone webcam;

    public MainBaseWebcam(HardwareMap hardwareMap, Telemetry telemetry, LinearOpMode mode){
        super(hardwareMap, telemetry, mode);
    }

    @Override
    public void init(){
        super.init();

        webcam = new CustomWebcamSkyStone(hardwareMap, "Webcam");
        webcam.init();
    }
}
