package org.pattonvillerobotics.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.pattonvillerobotics.commoncode.opmodes.OpModeGroups;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.GamepadData;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.ListenableButton;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.ListenableGamepad;
import org.pattonvillerobotics.robotclasses.mechanisms.ServoArm;

/**
 * Created by skaggsw on 10/5/17.
 */
@TeleOp(name = "ServoArmTest", group = OpModeGroups.TESTING)
public class ServoArmTest extends LinearOpMode {

    private ServoArm servoArm;
    private ListenableGamepad gamepad;

    @Override
    public void runOpMode() throws InterruptedException {

        servoArm = new ServoArm(hardwareMap, this);
        gamepad = new ListenableGamepad();

        gamepad.addButtonListener(GamepadData.Button.X, ListenableButton.ButtonState.JUST_PRESSED, () -> servoArm.extendArm());

        gamepad.addButtonListener(GamepadData.Button.Y, ListenableButton.ButtonState.JUST_PRESSED, () -> servoArm.retractArm());

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Servo Position:", servoArm.getServoPosition());
            telemetry.update();
            gamepad.update(gamepad1);
        }
    }
}
