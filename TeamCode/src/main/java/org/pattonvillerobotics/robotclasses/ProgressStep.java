package org.pattonvillerobotics.robotclasses;

import com.qualcomm.robotcore.robot.Robot;

import org.pattonvillerobotics.commoncode.enums.Direction;
import org.pattonvillerobotics.commoncode.robotclasses.drive.MecanumEncoderDrive;
import org.pattonvillerobotics.opmodes.autonomous.ProjectGod;

import java.util.HashMap;

/**
 * Created by wrightk03 on 3/13/18.
 *
 * This class is the main component that makes up the *ProjectGod* Autonomous System
 *
 * This system is currently written for MecanumDrive which is a QuadEncoderDrive
 * which is an EncoderDrive which is an AbstractComplexDrive
 */

@Deprecated
public class ProgressStep {

    //This is the state in which the robot should be in while the step is being conducted
    private ProjectGod.PropagationState ps;

    //This is the ammount of miliseconds that the robot should sleep for at the end of this step
    private long timeToSleepAtEndOfStep;

    //This boolean indicates that there is nothing for the robot to do but sleep
    private boolean waitStep;

    //The location in which the robot should move to. Origin is defined as (0,0)
    private int movex, movey;

    //The drive interface
    private MecanumEncoderDrive drive;

    //Direction that the step moves the robot in
    private Direction direction;

    //Speed at which this step will be run
    private double speed;

    /*Optional variable speed input

      This variable speed input allows users to specify the robot to move
      at different speeds throughout a single step incase the robot is needed to move discretly
     */
    private HashMap<Double, Long> speedVariation = new HashMap<>();

    /**
     * This is the class constructor.
     *
     * @param movex
     * @param movey
     * @param ps
     * @param timeToSleepAtEndOfStep
     * @param waitStep
     * @param drive
     * @param direction
     * @param speed
     */

    public ProgressStep(int movex, int movey, Direction direction, double speed, ProjectGod.PropagationState ps, long timeToSleepAtEndOfStep, boolean waitStep, MecanumEncoderDrive drive){
        this.ps = ps;
        this.timeToSleepAtEndOfStep = timeToSleepAtEndOfStep;
        this.drive = drive;
    }

    /**
     * This method is used to get the propagation state by the outside Telemetry Recorder
     *
     * @return
     */

    public ProjectGod.PropagationState getPState(){
        return ps;
    }

    /**
     * this method is used to get the Wait time necessary to pause
     *
     * @return
     */

    public long getWaitTime(){
        return timeToSleepAtEndOfStep;
    }

    /**
     * This is the main move method for the progressStep. When executed, this method moves the
     * robot to the coordinates specified by the user within a small margin of error.
     *
     * @param endDirection
     * @param speed
     */

    public void moveToCoordinate(Direction endDirection, double speed) {
        if (movex < ProjectGod.x) {
            ProjectGod.currentDirection = Direction.BACKWARD;
        } else {
            ProjectGod.currentDirection = Direction.FORWARD;
        }
        drive.moveInches(ProjectGod.currentDirection, ProjectGod.tiledistance * movex, speed);
        if (movey < ProjectGod.y) {
            drive.rotateDegrees(Direction.COUNTERCLOCKWISE, 90, 0.8);
            ProjectGod.currentDirection = Direction.BACKWARD;
        } else {
            drive.rotateDegrees(Direction.CLOCKWISE, 90, 0.8);
            ProjectGod.currentDirection = Direction.FORWARD;
        }
        drive.moveInches(ProjectGod.currentDirection, ProjectGod.tiledistance * movex, speed);
    }

    /**
     * This custom move method is writen seprately from the drive's
     * rotationDegrees method because it is better to have value protection
     * in place to ensure silly mistakes don't happen in the code that slow
     * down production time. Also ensures that the correct move method is used
     *
     * @param direction
     * @param distanceInInches
     * @param speed
     */

    public void moveInchesInDirection(Direction direction, long distanceInInches, double speed){
        if(speed > 1){
            speed = 1;
        } else if(speed == 0.0 || speed < 0.1) {
            speed = 0.1;
        }
        drive.moveInches(direction, distanceInInches, speed);
    }

    /**
     * This custom rotate method is writen seprately from the drive's
     * rotateDegrees method because it is better to have value protection
     * in place to ensure silly mistakes don't happen in the code that slow
     * down production time.
     *
     * @param direction
     * @param degrees
     * @param speed
     */

    public void rotate(Direction direction, long degrees, double speed){
        if(speed > 1){
            speed = 1;
        } else if(speed == 0.0 || speed < 0.1) {
            speed = 0.1;
        }
        drive.rotateDegrees(direction, degrees, speed);
    }

}
