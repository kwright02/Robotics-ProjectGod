package org.pattonvillerobotics.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.pattonvillerobotics.commoncode.opmodes.OpModeGroups;

/**
 * Created by skaggsw on 10/19/17.
 */
@TeleOp(name = "MotorTest w/ Joystick", group = OpModeGroups.TESTING)
public class MotorTest extends LinearOpMode {

    private DcMotor motor;

    @Override
    public void runOpMode() throws InterruptedException {

        motor = hardwareMap.dcMotor.get("motor");
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        waitForStart();

        while(opModeIsActive()) {
            motor.setPower(-gamepad1.left_stick_y);
        }
    }
}
