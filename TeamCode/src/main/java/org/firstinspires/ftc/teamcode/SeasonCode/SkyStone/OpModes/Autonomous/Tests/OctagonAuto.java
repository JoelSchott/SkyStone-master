package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.OpModes.Autonomous.Tests;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.Core.Utility.CustomTensorFlowSkyStone;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Components.Sky_Stone_Components.FourWheelMecanum;
import org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.MainBase;

import java.util.List;


//@Autonomous(name = "Custom Joel Control Octagon", group = "Autonomous")
public class OctagonAuto extends LinearOpMode {

    MainBase base;

    AutoState state = AutoState.LEFT;

    enum AutoState{
        LEFT,BACK_LEFT,BACK,BACK_RIGHT,RIGHT,FORWARD_RIGHT,FORWARD,FORWARD_LEFT,TURN,END
    }

    boolean targetsSet = false;

    double distance = 12;

    @Override
    public void runOpMode() throws InterruptedException {

        base = new MainBase(hardwareMap, telemetry, this);
        base.init();


        while (!opModeIsActive()){
            telemetry.addLine("All Systems Go");
            telemetry.update();
        }

        while (opModeIsActive()){

            telemetry.addData("currently in state ", state);
            telemetry.addData("frontLeft encoder is ", base.drivetrain.frontLeft.getCurrentPosition());
            telemetry.update();

            switch (state){
                case LEFT:
                    if (!targetsSet){
                        base.drivetrain.autoDrive.setDriveTarget(distance, FourWheelMecanum.Direction.LEFT, 30);
                        targetsSet = true;
                    }
                    else{
                        if (base.drivetrain.autoDrive.driveToTarget()){
                            state = AutoState.BACK_LEFT;
                            targetsSet = false;
                        }
                    }
                    break;

                case BACK_LEFT:
                    if (!targetsSet){
                        base.drivetrain.autoDrive.setDriveTarget(distance, FourWheelMecanum.Direction.BACK_LEFT, 10);
                        targetsSet = true;
                    }
                    else{
                        if (base.drivetrain.autoDrive.driveToTarget()){
                            state = AutoState.BACK;
                            targetsSet = false;
                        }
                    }
                    break;

                case BACK:
                    if (!targetsSet){
                        base.drivetrain.autoDrive.setDriveTarget(distance, FourWheelMecanum.Direction.BACK, 10);
                        targetsSet = true;
                    }
                    else{
                        if (base.drivetrain.autoDrive.driveToTarget()){
                            state = AutoState.BACK_RIGHT;
                            targetsSet = false;
                        }
                    }
                    break;

                case BACK_RIGHT:
                    if (!targetsSet){
                        base.drivetrain.autoDrive.setDriveTarget(distance, FourWheelMecanum.Direction.BACK_RIGHT, 10);
                        targetsSet = true;
                    }
                    else{
                        if (base.drivetrain.autoDrive.driveToTarget()){
                            state = AutoState.RIGHT;
                            targetsSet = false;
                        }
                    }
                    break;

                case RIGHT:
                    if (!targetsSet){
                        base.drivetrain.autoDrive.setDriveTarget(distance, FourWheelMecanum.Direction.RIGHT, 10);
                        targetsSet = true;
                    }
                    else{
                        if (base.drivetrain.autoDrive.driveToTarget()){
                            state = AutoState.FORWARD_RIGHT;
                            targetsSet = false;
                        }
                    }
                    break;

                case FORWARD_RIGHT:
                    if (!targetsSet){
                        base.drivetrain.autoDrive.setDriveTarget(distance, FourWheelMecanum.Direction.FORWARD_RIGHT, 10);
                        targetsSet = true;
                    }
                    else{
                        if (base.drivetrain.autoDrive.driveToTarget()){
                            state = AutoState.FORWARD;
                            targetsSet = false;
                        }
                    }
                    break;

                case FORWARD:
                    if (!targetsSet){
                        base.drivetrain.autoDrive.setDriveTarget(distance, FourWheelMecanum.Direction.FORWARD, 10);
                        targetsSet = true;
                    }
                    else{
                        if (base.drivetrain.autoDrive.driveToTarget()){
                            state = AutoState.FORWARD_LEFT;
                            targetsSet = false;
                        }
                    }
                    break;

                case FORWARD_LEFT:
                    if (!targetsSet){
                        base.drivetrain.autoDrive.setDriveTarget(distance, FourWheelMecanum.Direction.FORWARD_LEFT, 10);
                        targetsSet = true;
                    }
                    else{
                        if (base.drivetrain.autoDrive.driveToTarget()){
                            state = AutoState.TURN;
                            targetsSet = false;
                        }
                    }
                    break;

                case TURN:
                    if (!targetsSet){
                        base.drivetrain.autoTurn.setTurnTarget(90,10);
                        targetsSet = true;
                    }
                    else{
                        if (base.drivetrain.autoTurn.turnToTarget()){
                            state = AutoState.END;
                            targetsSet = false;
                        }
                    }
                    break;

                case END:
                    base.stop();
                    break;
            }

        }
    }
}
