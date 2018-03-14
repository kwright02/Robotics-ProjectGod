package org.pattonvillerobotics.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.pattonvillerobotics.commoncode.opmodes.OpModeGroups;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.GamepadData;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.ListenableButton;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.ListenableGamepad;

/**
 * Created by gregbahr on 2/13/18.
 */
@TeleOp(name = "Servo Position Test", group = OpModeGroups.TESTING)
public class ServoPositionTest extends LinearOpMode {

    private Servo servo;
    private ListenableGamepad gamepad;

    @Override
    public void runOpMode() throws InterruptedException {
        servo = hardwareMap.servo.get("servo");
        gamepad = new ListenableGamepad();

        gamepad.addButtonListener(GamepadData.Button.LEFT_BUMPER, ListenableButton.ButtonState.JUST_PRESSED, new ListenableButton.ButtonListener() {
            @Override
            public void run() {
                servo.setPosition(servo.getPosition() - 0.05);
            }
        });

        gamepad.addButtonListener(GamepadData.Button.RIGHT_BUMPER, ListenableButton.ButtonState.JUST_PRESSED, new ListenableButton.ButtonListener() {
            @Override
            public void run() {
                servo.setPosition(servo.getPosition() + 0.05);
            }
        });

        gamepad.addButtonListener(GamepadData.Button.DPAD_LEFT, ListenableButton.ButtonState.JUST_PRESSED, () -> {
            servo.setPosition(servo.getPosition() - .01);
        });

        gamepad.addButtonListener(GamepadData.Button.DPAD_RIGHT, ListenableButton.ButtonState.JUST_PRESSED, () -> {
            servo.setPosition(servo.getPosition() + .01);
        });

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Servo Position", servo.getPosition());
            gamepad.update(gamepad1);
            telemetry.update();
        }
    }
}
