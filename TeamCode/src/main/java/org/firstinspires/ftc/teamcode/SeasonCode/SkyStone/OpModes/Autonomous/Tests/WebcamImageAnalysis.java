package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.stream.CameraStreamServer;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBaseWebcam;

@Disabled
@Autonomous(name = "Webcam Image Analysis ", group = "Autonomous")
public class WebcamImageAnalysis extends LinearOpMode {

    MainBaseWebcam base;

    CameraStreamServer server;

    public void runOpMode(){

        base = new MainBaseWebcam(hardwareMap, telemetry, this);
        server = CameraStreamServer.getInstance();
        server.setSource(base.webcam.getVuforia());


        waitForStart();



    }
}
