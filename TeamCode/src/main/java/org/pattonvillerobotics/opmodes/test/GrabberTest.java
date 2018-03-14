package org.pattonvillerobotics.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.pattonvillerobotics.commoncode.opmodes.OpModeGroups;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.GamepadData;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.ListenableButton;
import org.pattonvillerobotics.commoncode.robotclasses.gamepad.ListenableGamepad;
import org.pattonvillerobotics.robotclasses.mechanisms.Spinner;
import org.pattonvillerobotics.robotclasses.mechanisms.XWing;

@TeleOp(name = "GrabberTest", group = OpModeGroups.TESTING)
public class GrabberTest extends LinearOpMode {

    private XWing xWing;
    private Spinner spinner;
    private ListenableGamepad gamepad;

    @Override
    public void runOpMode() throws InterruptedException {
        xWing = new XWing(hardwareMap, this);
        spinner = new Spinner(hardwareMap, this);

        gamepad = new ListenableGamepad();

        gamepad.getButton(GamepadData.Button.X).addListener(ListenableButton.ButtonState.JUST_PRESSED, xWing::toggleBottomClawPosition);
        gamepad.getButton(GamepadData.Button.Y).addListener(ListenableButton.ButtonState.JUST_PRESSED, xWing::toggleTopClawPosition);
        gamepad.getButton(GamepadData.Button.A).addListener(ListenableButton.ButtonState.JUST_PRESSED, spinner::toggleSpinnerPosition);

        waitForStart();

        while (opModeIsActive()) {
            gamepad.update(gamepad1);
            telemetry.update();
        }
    }
}
