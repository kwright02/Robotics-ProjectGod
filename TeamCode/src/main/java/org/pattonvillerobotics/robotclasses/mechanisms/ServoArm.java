package org.pattonvillerobotics.robotclasses.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by skaggsw on 10/5/17.
 * <p>
 * Contains the servo and sensor for the arm mechanism, as well as all the methods involved with
 * the operation of the mechanism
 */
public class ServoArm extends AbstractMechanism {

    private Servo servo;
    private ColorSensor sensorColor;
    private boolean isExtended;

    /**
     * Initializes the hardwaremap and linearopmode, as well as the servo and color sensor
     *
     * @param hardwareMap a hardwaremap to initialize the arm's servo and sensor
     * @param linearOpMode a linearopmode that allows for sleeping and telemetry
     */
    public ServoArm(HardwareMap hardwareMap, LinearOpMode linearOpMode) {
        super(hardwareMap, linearOpMode);
        servo = hardwareMap.servo.get("servo_arm");
        retractArm();
    }

    /**
     * Extends the claw arm
     */
    public void extendArm() {
        servo.setPosition(0.25);
        isExtended = true;
    }

    /**
     * Retracts the claw arm
     */
    public void retractArm() {
        servo.setPosition(.8);
        isExtended = false;
    }

    public void toggleArmPosition() {
        if (isExtended) {
            retractArm();
        } else {
            extendArm();
        }
    }

    /**
     * Returns the claw's current position
     * @return position of the claw
     */
    public double getServoPosition() {
        return servo.getPosition();
    }
}
