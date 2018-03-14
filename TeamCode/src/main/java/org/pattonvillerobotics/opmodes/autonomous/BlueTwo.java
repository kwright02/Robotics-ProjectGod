package org.pattonvillerobotics.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.pattonvillerobotics.commoncode.enums.Direction;
import org.pattonvillerobotics.commoncode.opmodes.OpModeGroups;
import org.pattonvillerobotics.commoncode.robotclasses.drive.MecanumEncoderDrive;
import org.pattonvillerobotics.commoncode.robotclasses.opencv.ImageProcessor;
import org.pattonvillerobotics.commoncode.robotclasses.opencv.relicrecovery.jewels.JewelColorDetector;
import org.pattonvillerobotics.commoncode.robotclasses.opencv.util.PhoneOrientation;
import org.pattonvillerobotics.commoncode.robotclasses.vuforia.VuforiaNavigation;
import org.pattonvillerobotics.opmodes.CustomizedRobotParameters;
import org.pattonvillerobotics.robotclasses.mechanisms.ServoArm;
import org.pattonvillerobotics.robotclasses.mechanisms.Spinner;
import org.pattonvillerobotics.robotclasses.mechanisms.XWing;

/**
 * Created by skaggsw on 11/21/17.
 */

@Autonomous(name = "Blue 2", group = OpModeGroups.MAIN)
public class BlueTwo extends LinearOpMode {

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

        telemetry.addData("Blue Two", "Initialization Complete");
        telemetry.update();

        waitForStart();

        xWing.bottomClawClose();
        sleep(1000);
        slides.setPower(-.3);
        sleep(1000);
        slides.setPower(0);

        drive.rotateDegrees(Direction.LEFT, 20, .6);

        columnKey = vuforia.getCurrentVisibleRelic();

        while(columnKey == RelicRecoveryVuMark.UNKNOWN) {
            columnKey = vuforia.getCurrentVisibleRelic();
        }

        telemetry.addData("Column Key: ", columnKey).setRetained(true);
        telemetry.update();

        drive.rotateDegrees(Direction.RIGHT, 20, .6);

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

        drive.moveInches(Direction.FORWARD, 30, .25);

        drive.rotateDegrees(Direction.LEFT, 90, 0.5);

        switch (columnKey) {
            case RIGHT:
                drive.moveInches(Direction.BACKWARD, 25, .4);
                break;
            case CENTER:
                drive.moveInches(Direction.BACKWARD, 14, .4);
                break;
            case LEFT:
                drive.moveInches(Direction.BACKWARD, 7, .4);
                break;
            default:
                break;
        }

        drive.rotateDegrees(Direction.LEFT, 90, .4);

        drive.moveInches(Direction.BACKWARD, 15, .4);
        xWing.bottomClawOpen();
        drive.moveInches(Direction.FORWARD, 12, .4);
        slides.setPower(.3);
        sleep(500);
        slides.setPower(0);
        drive.moveInches(Direction.BACKWARD, 15, .4);
        drive.moveInches(Direction.FORWARD, 13, .4);
        switch (columnKey) {
            case LEFT:
                drive.moveInches(Direction.RIGHT, 18, 1);
                break;
            case CENTER:
                drive.moveInches(Direction.RIGHT, 10, 1);
                break;
            default:
                break;
        }
        drive.rotateDegrees(Direction.RIGHT, 100, .5);
    }

    public void initialize() {
        ImageProcessor.initOpenCV(hardwareMap, this);
        slides = hardwareMap.dcMotor.get("slides");
        drive = new MecanumEncoderDrive(hardwareMap, this, CustomizedRobotParameters.ROBOT_PARAMETERS);
        arm = new ServoArm(hardwareMap, this);
        xWing = new XWing(hardwareMap, this);
        spinner = new Spinner(hardwareMap, this);

        jewelColorDetector = new JewelColorDetector(PhoneOrientation.PORTRAIT_INVERSE, true);
        vuforia = new VuforiaNavigation(CustomizedRobotParameters.VUFORIA_PARAMETERS);
    }
}