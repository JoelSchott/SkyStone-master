package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomTensorFlowSkyStone;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.List;

//@Autonomous(name = "Vision Test")
public class VisionTest extends LinearOpMode {

    private CustomTensorFlowSkyStone vision;

    private List<Recognition> objects;

    @Override
    public void runOpMode() throws InterruptedException {
        vision = new CustomTensorFlowSkyStone(hardwareMap);
        vision.init();

        waitForStart();

        while(opModeIsActive()){
            objects = vision.getObjects();
            if (objects != null){
                telemetry.addData("Stones visible are ", objects.size());
                int i = 0;
                for (Recognition object : objects){
                    telemetry.addData("Label is ", object.getLabel());
                    telemetry.addData("Confidence is ", object.getConfidence());
                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                            object.getLeft(), object.getTop());
                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                            object.getRight(), object.getBottom());
                }
            }
            telemetry.update();
        }
    }
}
