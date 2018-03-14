package org.pattonvillerobotics.opmodes.teleop;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.pattonvillerobotics.commoncode.opmodes.OpModeGroups;
import org.pattonvillerobotics.commoncode.robotclasses.drive.SimpleMecanumDrive;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.GamepadData;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.ListenableButton;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.ListenableGamepad;
import org.pattonvillerobotics.robotclasses.enums.SpinnerPosition;
import org.pattonvillerobotics.robotclasses.mechanisms.ServoArm;
import org.pattonvillerobotics.robotclasses.mechanisms.Spinner;
import org.pattonvillerobotics.robotclasses.mechanisms.XWing;

@TeleOp(name = "MainTeleOp", group = OpModeGroups.MAIN)
public class MainTeleOp extends LinearOpMode {

    private SimpleMecanumDrive drive;
    private DcMotor slides;
    private ListenableGamepad gamepad;
    private boolean fieldOrientedDriveMode;
    private ServoArm servoArm;
    private Spinner spinny;
    private BNO055IMU imu;
    private XWing xWing;

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        Vector2D polarCoords;
        Orientation angles;

        waitForStart();

        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        while (opModeIsActive()) {
            polarCoords = SimpleMecanumDrive.toPolar(gamepad1.left_stick_x, -gamepad1.left_stick_y);
            angles = imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);
            gamepad.update(gamepad1);

            drive.moveFreely(polarCoords.getY() - (fieldOrientedDriveMode ? angles.secondAngle + (Math.PI / 2.) : 0), polarCoords.getX(), -gamepad1.right_stick_x);
            slides.setPower(gamepad1.left_trigger - gamepad1.right_trigger);
            telemetry.addData("Field Oriented Drive", fieldOrientedDriveMode);

            telemetry.addData("Left Stick Radius", polarCoords.getX());
            telemetry.addData("Left Stick Angle", FastMath.toDegrees(polarCoords.getY() + (fieldOrientedDriveMode ? angles.secondAngle + (Math.PI / 2.) : 0)));
            telemetry.addData("Right Stick X", gamepad1.right_stick_x);
            telemetry.addData("Angles", angles.toString());
            telemetry.addData("Current Spinny Position", spinny.getCurrentPosition());

            telemetry.update();
        }
    }

    public void initialize() {

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        slides = hardwareMap.dcMotor.get("slides");
        drive = new SimpleMecanumDrive(this, hardwareMap);
        servoArm = new ServoArm(hardwareMap, this);

        drive.leftRearMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        drive.rightRearMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        drive.leftDriveMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        drive.rightDriveMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        drive.leftRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive.leftDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive.rightRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        drive.rightDriveMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        gamepad = new ListenableGamepad();

        xWing = new XWing(hardwareMap, this);

        spinny = new Spinner(hardwareMap, this);

        gamepad.getButton(GamepadData.Button.Y).addListener(ListenableButton.ButtonState.JUST_PRESSED, () -> {
            if (spinny.getCurrentPosition() == SpinnerPosition.UP) {
                xWing.toggleTopClawPosition();
            } else {
                xWing.toggleBottomClawPosition();
            }
        });
        gamepad.getButton(GamepadData.Button.X).addListener(ListenableButton.ButtonState.JUST_PRESSED, () -> {
            if (spinny.getCurrentPosition() == SpinnerPosition.UP) {
                xWing.toggleBottomClawPosition();
            } else {
                xWing.toggleTopClawPosition();
            }
        });

        gamepad.getButton(GamepadData.Button.A).addListener(ListenableButton.ButtonState.JUST_PRESSED, spinny::toggleSpinnerPosition);
        gamepad.getButton(GamepadData.Button.B).addListener(ListenableButton.ButtonState.JUST_PRESSED, servoArm::toggleArmPosition);

        gamepad.getButton(GamepadData.Button.STICK_BUTTON_RIGHT).addListener(ListenableButton.ButtonState.JUST_PRESSED, () -> fieldOrientedDriveMode = !fieldOrientedDriveMode);

    }
}
