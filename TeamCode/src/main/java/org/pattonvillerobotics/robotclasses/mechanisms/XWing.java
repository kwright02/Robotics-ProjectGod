package org.pattonvillerobotics.robotclasses.mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.pattonvillerobotics.robotclasses.enums.ClawPosition;

/**
 * Created by skaggsw on 3/1/18.
 */

public class XWing extends AbstractMechanism {

    public Servo topClawLeftServo, bottomClawLeftServo, topClawRightServo, bottomClawRightServo;
    private boolean justClosedBottom, justClosedTop;
    private ClawPosition topClawPosition;
    private ClawPosition bottomClawPosition;

    public XWing(HardwareMap hardwareMap, LinearOpMode linearOpMode) {
        super(hardwareMap,linearOpMode);

        topClawLeftServo       = hardwareMap.servo.get("claw1");
        topClawRightServo      = hardwareMap.servo.get("claw3");

        bottomClawLeftServo    = hardwareMap.servo.get("claw2");
        bottomClawRightServo   = hardwareMap.servo.get("claw4");

        topClawPosition        = ClawPosition.OPEN;
        bottomClawPosition     = ClawPosition.OPEN;

        topClawOpen();
        bottomClawOpen();
    }

    public void topClawClose() {
        topClawPosition = ClawPosition.CLOSE;
        topClawLeftServo.setPosition(0.55);
        topClawRightServo.setPosition(0);
    }

    public void topClawOpen() {
        topClawPosition = ClawPosition.OPEN;
        topClawLeftServo. setPosition(0);
        topClawRightServo.setPosition(0.55);
    }

    public void topClawHalf() {
        topClawPosition = ClawPosition.HALF;
        topClawLeftServo.setPosition(0.45);
        topClawRightServo.setPosition(0.1);
    }

    public void bottomClawClose() {
        bottomClawPosition = ClawPosition.CLOSE;
        bottomClawLeftServo. setPosition(0);
        bottomClawRightServo.setPosition(0.55);
    }

    public void bottomClawOpen() {
        bottomClawPosition = ClawPosition.OPEN;
        bottomClawLeftServo.setPosition(0.6);
        bottomClawRightServo.setPosition(0);
    }

    public void bottomClawHalf() {
        bottomClawPosition = ClawPosition.HALF;
        bottomClawLeftServo.setPosition(0.15);
        bottomClawRightServo.setPosition(0.4);
    }

    public ClawPosition getTopClawPosition() {
        return topClawPosition;
    }

    public void setTopClawPosition(double position) {
        topClawLeftServo. setPosition(position);
        topClawRightServo.setPosition(position);
    }

    public ClawPosition getBottomClawPosition() {
        return bottomClawPosition;
    }

    public void setBottomClawPosition(double position) {
        bottomClawLeftServo. setPosition(position);
        bottomClawRightServo.setPosition(position);
    }

    public void toggleTopClawPosition() {
        switch (topClawPosition) {
            case HALF:
                if (justClosedTop) {
                    topClawOpen();
                    topClawPosition = ClawPosition.OPEN;
                    justClosedTop = false;
                } else {
                    topClawClose();
                    topClawPosition = ClawPosition.CLOSE;
                }
                break;
            case OPEN:
                topClawHalf();
                topClawPosition = ClawPosition.HALF;
                break;
            case CLOSE:
                topClawHalf();
                justClosedTop = true;
                topClawPosition = ClawPosition.HALF;
                break;
            default:
                topClawOpen();
                topClawPosition = ClawPosition.OPEN;
                break;
        }
    }

    public void toggleBottomClawPosition() {
        switch (bottomClawPosition) {
            case HALF:
                if (justClosedBottom) {
                    bottomClawOpen();
                    bottomClawPosition = ClawPosition.OPEN;
                    justClosedBottom = false;
                } else {
                    bottomClawClose();
                    bottomClawPosition = ClawPosition.CLOSE;
                }
                break;
            case OPEN:
                bottomClawHalf();
                bottomClawPosition = ClawPosition.HALF;
                break;
            case CLOSE:
                bottomClawHalf();
                justClosedBottom = true;
                bottomClawPosition = ClawPosition.HALF;
                break;
            default:
                bottomClawOpen();
                bottomClawPosition = ClawPosition.OPEN;
                break;
        }
    }
}
