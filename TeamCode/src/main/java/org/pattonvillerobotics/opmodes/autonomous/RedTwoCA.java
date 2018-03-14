package org.pattonvillerobotics.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.pattonvillerobotics.commoncode.enums.AllianceColor;
import org.pattonvillerobotics.commoncode.opmodes.OpModeGroups;
import org.pattonvillerobotics.robotclasses.CommonAutonomous;
import org.pattonvillerobotics.robotclasses.enums.StartingPosition;

/**
 * Created by skaggsw on 11/28/17.
 */

@Autonomous(name = "Red 2 Common Autonomous", group = OpModeGroups.MAIN)
public class RedTwoCA extends LinearOpMode {

    CommonAutonomous autonomous;

    @Override
    public void runOpMode() throws InterruptedException {
        autonomous = new CommonAutonomous(StartingPosition.RED_TWO, AllianceColor.RED, hardwareMap, this);
        autonomous.initialize();
        autonomous.detectAndStoreVuMark();
        autonomous.extendArmAndDetectColor();
        autonomous.knockOffJewel();
        autonomous.moveToColumn();
        autonomous.insertBlock();
    }
}
