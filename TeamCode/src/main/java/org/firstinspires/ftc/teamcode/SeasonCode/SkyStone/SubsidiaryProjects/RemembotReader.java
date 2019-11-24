package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.SubsidiaryProjects;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@Autonomous(name = "Remembot Reader")
public class RemembotReader extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    private String fileName = "";

    @Override
    public void runOpMode(){

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        telemetry.addLine("Hello! I am the remembot reader.");
        telemetry.addLine("So I know where to read this recording from, please enter a file name with the a and b buttons and then press x");
        telemetry.update();

        boolean aHeld = false;
        boolean bHeld = false;
        while (fileName.equals("") || !gamepad1.x){
            if (gamepad1.a && !aHeld){
                fileName = fileName + "a";
                aHeld = true;
            }
            if (!gamepad1.a){
                aHeld = false;
            }
            if (gamepad1.b && !bHeld){
                fileName = fileName + "B";
                bHeld = true;
            }
            if (!gamepad1.b){
                bHeld = false;
            }
            if (fileName.equals("")){
                telemetry.addLine("The file name cannot be empty");
            }
            else{
                telemetry.addData("The current file name is ", fileName);
            }
            telemetry.update();
        }


        File file;
        try {
            file = new File(Environment.getExternalStorageDirectory(), fileName);
        }
        catch(Exception e)
        {
            telemetry.addData("Could not find file with name ", fileName);
            telemetry.update();
            return;
        }
        telemetry.addLine("Found the file");
        telemetry.update();
        waitForStart();

        int lineNumber = 0;

        //-------------------------------READING----------------------------------
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String[] line;

            while((line = br.readLine().split(" ")) != null){
                frontLeft.setPower(Double.valueOf(line[0]));
                backLeft.setPower(Double.valueOf(line[1]));
                frontRight.setPower(Double.valueOf(line[2]));
                backRight.setPower(Double.valueOf(line[3]));

                sleep(Long.valueOf(line[4]));

                lineNumber++;
            }
            frontLeft.setPower(0);
            backLeft.setPower(0);
            frontRight.setPower(0);
            backRight.setPower(0);
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        telemetry.addData("Datasets were " , lineNumber);
        telemetry.update();
    }
}
