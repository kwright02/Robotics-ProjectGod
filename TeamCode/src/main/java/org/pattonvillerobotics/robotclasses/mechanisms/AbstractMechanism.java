package org.pattonvillerobotics.robotclasses.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by gregbahr on 10/23/17.
 * <p>
 * An abstract class for our mechanisms to inherit the hardwareMap and linearOpMode objects from
 */

public abstract class AbstractMechanism {
    protected final HardwareMap hardwareMap;
    protected final LinearOpMode linearOpMode;
    /**
     * Initializes the hardwaremap and linearopmode
     *
     * @param hardwareMap a hardwaremap to initialize the hardware
     * @param linearOpMode a linearopmode that allows for sleeping and telemetry
     */
    public AbstractMechanism(HardwareMap hardwareMap, LinearOpMode linearOpMode) {
        this.hardwareMap = hardwareMap;
        this.linearOpMode = linearOpMode;
    }
}
