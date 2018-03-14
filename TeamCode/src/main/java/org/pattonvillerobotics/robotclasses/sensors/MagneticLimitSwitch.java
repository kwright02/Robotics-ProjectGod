package org.pattonvillerobotics.robotclasses.sensors;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by gregbahr on 2/14/18.
 */

public class MagneticLimitSwitch {
    private DigitalChannel limitSwitch;

    public MagneticLimitSwitch(HardwareMap hardwareMap, String deviceName) {
        limitSwitch = hardwareMap.digitalChannel.get(deviceName);
        limitSwitch.setMode(DigitalChannel.Mode.INPUT);
    }

    public boolean detectsMagnet() {
        return limitSwitch.getState();
    }
}
