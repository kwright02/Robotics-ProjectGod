package org.pattonvillerobotics.robotclasses;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.pattonvillerobotics.commoncode.enums.AllianceColor;
import org.pattonvillerobotics.commoncode.enums.Direction;
import org.pattonvillerobotics.commoncode.robotclasses.drive.MecanumEncoderDrive;
import org.pattonvillerobotics.commoncode.robotclasses.opencv.ImageProcessor;
import org.pattonvillerobotics.commoncode.robotclasses.opencv.relicrecovery.jewels.JewelColorDetector;
import org.pattonvillerobotics.commoncode.robotclasses.opencv.util.PhoneOrientation;
import org.pattonvillerobotics.commoncode.robotclasses.vuforia.VuforiaNavigation;
import org.pattonvillerobotics.opmodes.CustomizedRobotParameters;
import org.pattonvillerobotics.robotclasses.enums.StartingPosition;
import org.pattonvillerobotics.robotclasses.mechanisms.ServoArm;
import org.pattonvillerobotics.robotclasses.mechanisms.XWing;

/**
 * Created by bahrg on 10/24/17.
 */

public class CommonAutonomous {

    private AllianceColor allianceColor;
    private StartingPosition startingPosition;
    private HardwareMap hardwareMap;
    private LinearOpMode linearOpMode;
    private VuforiaNavigation vuforia;
    private JewelColorDetector jewelColorDetector;
    private RelicRecoveryVuMark vuMark;
    private JewelColorDetector.AnalysisResult analysis;

    private XWing xWing;
    private ServoArm arm;
    private MecanumEncoderDrive drive;
    private DcMotor slideMotor;

    public CommonAutonomous(StartingPosition startingPosition, AllianceColor allianceColor, HardwareMap hardwareMap, LinearOpMode linearOpMode) {
        this.allianceColor = allianceColor;
        this.hardwareMap = hardwareMap;
        this.linearOpMode = linearOpMode;
        this.startingPosition = startingPosition;

        ImageProcessor.initOpenCV(hardwareMap, linearOpMode);
        jewelColorDetector = new JewelColorDetector(PhoneOrientation.PORTRAIT);
        vuforia = new VuforiaNavigation(CustomizedRobotParameters.VUFORIA_PARAMETERS);
        drive = new MecanumEncoderDrive(hardwareMap, linearOpMode, CustomizedRobotParameters.ROBOT_PARAMETERS);

        arm = new ServoArm(hardwareMap, linearOpMode);
        slideMotor = hardwareMap.dcMotor.get("slide_motor");
    }

    public void detectAndStoreVuMark() {
        xWing.bottomClawClose();
        linearOpMode.sleep(500);

        switch (startingPosition) {
            case BLUE_ONE:
                drive.rotateDegrees(Direction.RIGHT, 20, .4);
                break;
            case BLUE_TWO:
                drive.rotateDegrees(Direction.RIGHT, 20, .4);
                break;
            case RED_ONE:
                drive.rotateDegrees(Direction.LEFT, 20, .4);
                break;
            case RED_TWO:
                drive.rotateDegrees(Direction.LEFT, 20, .4);
                break;
        }
        while((vuMark == null || vuMark == RelicRecoveryVuMark.UNKNOWN) && linearOpMode.opModeIsActive()) {
            vuMark = vuforia.getCurrentVisibleRelic();
        }

        linearOpMode.sleep(1000);

        switch (startingPosition) {
            case BLUE_ONE:
                drive.rotateDegrees(Direction.LEFT, 20, .4);
                break;
            case BLUE_TWO:
                drive.rotateDegrees(Direction.LEFT, 20, .4);
                break;
            case RED_ONE:
                drive.rotateDegrees(Direction.RIGHT, 20, .4);
                break;
            case RED_TWO:
                drive.rotateDegrees(Direction.RIGHT, 20, .4);
                break;
        }
    }

    public void extendArmAndDetectColor() {
        arm.extendArm();
        jewelColorDetector.process(vuforia.getImage());
        analysis = jewelColorDetector.getAnalysis();

        while(analysis.leftJewelColor == null && linearOpMode.opModeIsActive()) {
            jewelColorDetector.process(vuforia.getImage());
            analysis = jewelColorDetector.getAnalysis();
        }
    }

    public void knockOffJewel() {
        switch (startingPosition) {
            case BLUE_ONE:
                switch (analysis.leftJewelColor) {
                    case RED:
                        drive.moveInches(Direction.BACKWARD,6,0.5);
                        drive.moveInches(Direction.FORWARD,6,0.5);
                        break;
                    case BLUE:
                        drive.moveInches(Direction.FORWARD,6,0.5);
                        drive.moveInches(Direction.BACKWARD,6,0.5);
                        break;
                    default:

                }
                break;
            case BLUE_TWO:
                switch (analysis.leftJewelColor) {
                    case RED:
                        drive.moveInches(Direction.BACKWARD,6,0.5);
                        drive.moveInches(Direction.FORWARD,6,0.5);
                        break;
                    case BLUE:
                        drive.moveInches(Direction.FORWARD,6,0.5);
                        drive.moveInches(Direction.BACKWARD,6,0.5);
                        break;
                    default:

                }
                break;
            case RED_ONE:
                switch (analysis.leftJewelColor) {
                    case RED:
                        drive.moveInches(Direction.FORWARD,6,0.5);
                        drive.moveInches(Direction.BACKWARD,6,0.5);
                        break;
                    case BLUE:
                        drive.moveInches(Direction.BACKWARD,6,0.5);
                        drive.moveInches(Direction.FORWARD,6,0.5);
                        break;
                    default:

                }
                break;
            case RED_TWO:
                switch (analysis.leftJewelColor) {
                    case RED:
                        drive.moveInches(Direction.FORWARD,6,0.5);
                        drive.moveInches(Direction.BACKWARD,6,0.5);
                        break;
                    case BLUE:
                        drive.moveInches(Direction.BACKWARD,6,0.5);
                        drive.moveInches(Direction.FORWARD,6,0.5);
                        break;
                    default:

                }
                break;
            default:
                Log.wtf("CommonAutonomous", "This should never happen.");
        }
        arm.retractArm();
    }

    public void moveToColumn() {
        switch (startingPosition) {
            case BLUE_ONE:
                drive.moveInches(Direction.FORWARD, 30, 0.5);

                linearOpMode.sleep(500);

                drive.rotateDegrees(Direction.LEFT, 90, 0.5);

                linearOpMode.sleep(500);

                switch (vuMark) {
                    case CENTER:
                        drive.moveInches(Direction.RIGHT, 12, 1);
                        break;
                    case RIGHT:
                        drive.moveInches(Direction.RIGHT, 23, 1);
                        break;
                    default:
                        break;
                }
                break;
            case BLUE_TWO:
                drive.moveInches(Direction.FORWARD, 28, 0.5);

                linearOpMode.sleep(500);

                drive.rotateDegrees(Direction.RIGHT, 180, 0.5);

                linearOpMode.sleep(500);

                switch (vuMark) {
                    case LEFT:
                        drive.moveInches(Direction.RIGHT, 10,1);
                        break;
                    case CENTER:
                        drive.moveInches(Direction.RIGHT, 26, 1);
                        break;
                    case RIGHT:
                        drive.moveInches(Direction.RIGHT, 33, 1);
                        break;
                    default:
                        break;
                }
                break;
            case RED_ONE:
                drive.moveInches(Direction.FORWARD, 30, 0.5);

                linearOpMode.sleep(500);

                drive.rotateDegrees(Direction.RIGHT, 90, 0.5);

                linearOpMode.sleep(500);

                switch (vuMark) {
                    case CENTER:
                        drive.moveInches(Direction.RIGHT, 12, 1);
                        break;
                    case LEFT:
                        drive.moveInches(Direction.RIGHT, 23, 1);
                        break;
                    default:
                        break;
                }
                break;
            case RED_TWO:
                drive.moveInches(Direction.BACKWARD, 28, 0.5);

                linearOpMode.sleep(500);

                drive.rotateDegrees(Direction.LEFT, 180, 0.5);

                linearOpMode.sleep(500);

                switch (vuMark) {
                    case LEFT:
                        drive.moveInches(Direction.RIGHT, 10,1);
                        break;
                    case CENTER:
                        drive.moveInches(Direction.RIGHT, 26, 1);
                        break;
                    case RIGHT:
                        drive.moveInches(Direction.RIGHT, 33, 1);
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void insertBlock() {
        switch (startingPosition) {
            case BLUE_ONE:
                drive.moveInches(Direction.BACKWARD, 15, .5);
                linearOpMode.sleep(500);
                xWing.bottomClawOpen();
                linearOpMode.sleep(500);
                drive.moveInches(Direction.FORWARD, 14, .5);
                break;
            case BLUE_TWO:
                drive.moveInches(Direction.BACKWARD, 10, .5);
                linearOpMode.sleep(500);
                xWing.bottomClawOpen();
                linearOpMode.sleep(500);
                drive.moveInches(Direction.FORWARD, 9, .5);
                break;
            case RED_ONE:
                drive.moveInches(Direction.FORWARD, 15, .5);
                linearOpMode.sleep(500);
                xWing.bottomClawOpen();
                linearOpMode.sleep(500);
                drive.moveInches(Direction.BACKWARD, 14, .5);
                break;
            case RED_TWO:
                drive.moveInches(Direction.FORWARD, 10, .5);
                linearOpMode.sleep(500);
                xWing.bottomClawOpen();
                linearOpMode.sleep(500);
                drive.moveInches(Direction.BACKWARD, 9, .5);
                break;
        }
        linearOpMode.sleep(500);
        drive.rotateDegrees(Direction.RIGHT, 180, .5);
    }

    public void initialize() {
        ImageProcessor.initOpenCV(hardwareMap, linearOpMode);
        slideMotor = hardwareMap.dcMotor.get("slides");
        drive = new MecanumEncoderDrive(hardwareMap, linearOpMode, CustomizedRobotParameters.ROBOT_PARAMETERS);
        arm = new ServoArm(hardwareMap, linearOpMode);
        xWing = new XWing(hardwareMap, linearOpMode);

        jewelColorDetector = new JewelColorDetector(PhoneOrientation.PORTRAIT_INVERSE);
        vuforia = new VuforiaNavigation(CustomizedRobotParameters.VUFORIA_PARAMETERS);
        vuforia.activateTracking();
    }
}
