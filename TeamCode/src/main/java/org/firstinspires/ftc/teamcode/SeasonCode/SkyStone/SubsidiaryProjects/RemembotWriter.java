package org.firstinspires.ftc.teamcode.SeasonCode.SkyStone.SubsidiaryProjects;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

@TeleOp(name = "Remembot Writer", group = "TeleOp")
public class RemembotWriter extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor backLeft;
    private DcMotor frontRight;
    private DcMotor backRight;

    public final static String FILE_NAME = "powerFile";

    private String fileName = "";

    private FileOutputStream outputStream;
    private PrintStream printStream;

    @Override
    public void runOpMode(){

        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft = hardwareMap.dcMotor.get("backLeft");
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        telemetry.addLine("Hello! I am the remembot writer.");
        telemetry.addLine("So I know where to save this recording, please enter a file name with the a and b buttons and then press x");
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
            if(!gamepad1.b){
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

        try {

            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            //file.exists()
            telemetry.addLine("made a file with name " + fileName);
            outputStream = new FileOutputStream(file);
            telemetry.addLine("made output stream");
            printStream = new PrintStream(outputStream);
            telemetry.addData("printStream is not null ", printStream.toString());
            printStream.flush();
            telemetry.addLine("flushed file");
            telemetry.addLine("All Systems Go");
        }
        catch(Exception e){
            e.printStackTrace();
            telemetry.addLine("error in creating files or streams");
            telemetry.update();
            return;
        }
        telemetry.update();


        waitForStart();

        int dataset = 0;
        long lastCallTime = System.currentTimeMillis();

        while (opModeIsActive()){

            //-------------------------------DRIVING----------------------------------

            double driveSpeed = Range.clip(gamepad1.left_stick_y, -1, 1);
            double turnSpeed = Range.clip(gamepad1.right_stick_x, -1, 1);

            double leftPower = driveSpeed + turnSpeed;
            double rightPower = driveSpeed - turnSpeed;

            if (Math.abs(leftPower) > Math.abs(rightPower)){
                if (leftPower > 1){
                    rightPower = rightPower/leftPower;
                    leftPower = 1.0;
                }
                else if (leftPower < -1){
                    rightPower = rightPower/-leftPower;
                    leftPower = -1.0;
                }
            }
            else{
                if (rightPower > 1){
                    leftPower = leftPower/rightPower;
                    rightPower = 1.0;
                }
                else if (rightPower < -1){
                    leftPower = leftPower/-rightPower;
                    rightPower = -1.0;
                }
            }

            if (Math.abs(driveSpeed) < 0.15 && Math.abs(turnSpeed) < 0.15){
                frontLeft.setPower(0);
                backLeft.setPower(0);
                frontRight.setPower(0);
                backRight.setPower(0);

            }
            else{
                frontLeft.setPower(leftPower);
                backLeft.setPower(leftPower);
                frontRight.setPower(rightPower);
                backRight.setPower(rightPower);
            }
            //----------------------------WRITING-------------------------------------

            long timeInterval = System.currentTimeMillis() - lastCallTime;
            if (timeInterval > 50){
                printStream.println(frontLeft.getPower() + " " + backLeft.getPower() + " " + frontRight.getPower() + " " + backRight.getPower() + " " + timeInterval);
                telemetry.addData("writing now with datatset ", dataset);
                telemetry.update();
                lastCallTime = System.currentTimeMillis();
                dataset++;
            }
        }

        try{
            printStream.close();
            outputStream.close();
        }
        catch(Exception e){
            e.printStackTrace();
            telemetry.addLine("problem closing streams");
        }

        telemetry.addData("Datasets were " , dataset);
        telemetry.update();

    }



}