package org.pattonvillerobotics.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.pattonvillerobotics.commoncode.enums.Direction;
import org.pattonvillerobotics.commoncode.opmodes.OpModeGroups;
import org.pattonvillerobotics.commoncode.robotclasses.drive.MecanumEncoderDrive;
import org.pattonvillerobotics.commoncode.robotclasses.opencv.ImageProcessor;
import org.pattonvillerobotics.commoncode.robotclasses.opencv.relicrecovery.jewels.JewelColorDetector;
import org.pattonvillerobotics.commoncode.robotclasses.vuforia.VuforiaNavigation;
import org.pattonvillerobotics.opmodes.CustomizedRobotParameters;
import org.pattonvillerobotics.robotclasses.mechanisms.ServoArm;
import org.pattonvillerobotics.robotclasses.mechanisms.Spinner;
import org.pattonvillerobotics.robotclasses.mechanisms.XWing;

/**
 * Created by wingertj01 on 11/2/17.
 */
@Autonomous (name = "Red 1", group = OpModeGroups.MAIN)
public class RedOne extends LinearOpMode{

    private MecanumEncoderDrive drive;
    private ServoArm arm;
    private XWing xWing;
    private JewelColorDetector jewelColorDetector;
    private VuforiaNavigation vuforia;
    private DcMotor slides;
    private Spinner spinner;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        JewelColorDetector.AnalysisResult analysis;
        vuforia.activateTracking();

        RelicRecoveryVuMark columnKey;

        telemetry.addData("Red One", "Initialization Complete");
        telemetry.update();

        waitForStart();

        xWing.bottomClawClose();
        sleep(1000);
        slides.setPower(-.7);
        sleep(1000);
        slides.setPower(0);

        drive.rotateDegrees(Direction.RIGHT, 20, .6);

        columnKey = vuforia.getCurrentVisibleRelic();

        while(columnKey == RelicRecoveryVuMark.UNKNOWN) {
            columnKey = vuforia.getCurrentVisibleRelic();
        }

        telemetry.addData("Column Key: ", columnKey).setRetained(true);
        telemetry.update();

        drive.rotateDegrees(Direction.LEFT, 20, .8);

        arm.extendArm();

        jewelColorDetector.process(vuforia.getImage());
        analysis = jewelColorDetector.getAnalysis();

        while ((analysis.leftJewelColor == null || analysis.rightJewelColor == null) && opModeIsActive()) {
            jewelColorDetector.process(vuforia.getImage());
            analysis = jewelColorDetector.getAnalysis();
        }

        telemetry.addData("LEFT", analysis.leftJewelColor).setRetained(true);
        telemetry.addData("RIGHT", analysis.rightJewelColor).setRetained(true);
        telemetry.update();

        switch (analysis.leftJewelColor) {
            case RED:
                drive.rotateDegrees(Direction.LEFT, 20, .5);
                arm.retractArm();
                sleep(1000);
                drive.rotateDegrees(Direction.RIGHT, 20, .5);
                break;
            case BLUE:
                drive.rotateDegrees(Direction.RIGHT, 20, .5);
                arm.retractArm();
                sleep(1000);
                drive.rotateDegrees(Direction.LEFT, 20, .5);
                break;
            default:
        }

        switch (columnKey) {
            case LEFT:
                drive.moveInches(Direction.FORWARD, 51, 0.7);
                break;
            case CENTER:
                drive.moveInches(Direction.FORWARD, 42, .7);
                break;
            case RIGHT:
                drive.moveInches(Direction.FORWARD, 33, .7);
                break;
            default:
                break;
        }

        drive.rotateDegrees(Direction.LEFT, 90, 0.7);
        drive.moveInches(Direction.FORWARD, 15, 1);
        xWing.bottomClawOpen();
        drive.moveInches(Direction.BACKWARD, 14, 1);
        slides.setPower(.5);
        sleep(500);
        slides.setPower(0);
        drive.moveInches(Direction.FORWARD, 14, 1);
        drive.moveInches(Direction.BACKWARD, 10, 1);
        drive.rotateDegrees(Direction.RIGHT, 180, 1);
    }

    public void initialize() {
        ImageProcessor.initOpenCV(hardwareMap, this);
        slides = hardwareMap.dcMotor.get("slides");
        drive = new MecanumEncoderDrive(hardwareMap, this, CustomizedRobotParameters.ROBOT_PARAMETERS);
        arm = new ServoArm(hardwareMap, this);
        xWing = new XWing(hardwareMap, this);
        spinner = new Spinner(hardwareMap, this);

        jewelColorDetector = new JewelColorDetector(CustomizedRobotParameters.PHONE_ORIENTATION, false);
        vuforia = new VuforiaNavigation(CustomizedRobotParameters.VUFORIA_PARAMETERS);

        drive.leftRearMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        drive.rightRearMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        drive.leftDriveMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        drive.rightDriveMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
}
