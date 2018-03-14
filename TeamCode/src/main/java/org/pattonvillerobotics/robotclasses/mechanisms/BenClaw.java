package org.pattonvillerobotics.robotclasses.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Contains the servos for each claw mechanism, as well as all the methods involved with
 * the operation of the mechanism
 */
@Deprecated
public class BenClaw extends AbstractMechanism {

    private static final double WIDE_POSITION_1 = 0;
    private static final double OPEN_POSITION_1 = .3;
    private static final double CLOSED_POSITION_1 = .55;
    private static final double RELEASE_POSITION_1 = .4;

    private static final double WIDE_POSITION_2 = .55;
    private static final double OPEN_POSITION_2 = .4;
    private static final double CLOSED_POSITION_2 = 0;
    private static final double RELEASE_POSITION_2 = .3;

    private Servo claw1, claw2;
    private boolean isOpen, isHalfOpen, isClosed;

    /**
     * Initializes the hardwaremap and linearopmode, as well as the claw's servo
     * Sets the position of the claw to open and initializes the isOpen boolean to true
     *
     * @param hardwareMap a hardwaremap to initialize the claw's servo
     * @param linearOpMode a linearopmode that allows for sleeping and telemetry
     */
    public BenClaw(HardwareMap hardwareMap, LinearOpMode linearOpMode, String claw1Name, String claw2Name) {
        super(hardwareMap, linearOpMode);
        claw1 = hardwareMap.servo.get(claw1Name);
        claw2 = hardwareMap.servo.get(claw2Name);

        isOpen = true;

        claw1.setPosition(WIDE_POSITION_1);
        claw2.setPosition(WIDE_POSITION_2);
    }

    /**
     * Opens the claw
     */
    public void open() {
        claw1.setPosition(OPEN_POSITION_1);
        claw2.setPosition(OPEN_POSITION_2);
    }

    /**
     * Closes the claw
     */
    public void close() {
        claw1.setPosition(CLOSED_POSITION_1);
        claw2.setPosition(CLOSED_POSITION_2);
    }

    /**
     * Barely opens the claw to release any glyphs.
     */
    public void release() {
        claw1.setPosition(RELEASE_POSITION_1);
        claw2.setPosition(RELEASE_POSITION_2);
    }

    /**
     * Opens the claws out wide.
     */
    public void wide() {
        claw1.setPosition(WIDE_POSITION_1);
        claw2.setPosition(WIDE_POSITION_2);
    }

    /**
     * Toggles the claw between open and closed positions
     * Negates isOpen
     */
    public void togglePosition() {
        if (isHalfOpen) {
            close();
            isOpen = false;
            isClosed = true;
            isHalfOpen = false;
        } else if (isOpen) {
            release();
            isHalfOpen = true;
            isClosed = false;
            isOpen = false;
        } else if (isClosed) {
            release();
            isHalfOpen = false;
            isOpen = false;
            isClosed = false;
        } else {
            open();
            isHalfOpen = false;
            isOpen = true;
            isClosed = false;
        }
    }

    /**
     * Checks if the claw is open
     * @return whether the claw is open or not
     */
    public boolean isOpen() {
        return isOpen;
    }
}
