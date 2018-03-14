package org.pattonvillerobotics.opmodes.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.pattonvillerobotics.commoncode.enums.Direction;
import org.pattonvillerobotics.robotclasses.Coordinate;
import org.pattonvillerobotics.robotclasses.ProgressStep;
import org.pattonvillerobotics.commoncode.robotclasses.drive.MecanumEncoderDrive;
import org.pattonvillerobotics.opmodes.CustomizedRobotParameters;
import org.pattonvillerobotics.robotclasses.mechanisms.ServoArm;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by wrightk03 on 3/13/18.
 */

public class ProjectGod extends LinearOpMode {

    //The DriveTrain operation
    private MecanumEncoderDrive drive;

    //X, Y coordinates direction,rds and the defined tile distance;
    public static int tiledistance = 24;
    public static double x,y;

    //Robot's starting direction
    public static final Direction startDirection = Direction.FORWARD;

    //Robot's starting rotation
    public static final double startRotation = 0.0;

    //Robot's current direction
    public static Direction currentDirection = Direction.FORWARD;

    /**
     * This method is the main method of the autonomous and is where progressSteps
     * are handled, the propagationState is recorded and output to telemetry.
     *
     * New movements can be added as follows:
     *
     * coords.add(new Coordinate(x cord, y cord, endDirection, speed, sleepTime))
     *
     * Coordinate Objects can also take in an optional paramater at the end where you
     * specify an action for the robot to perform. Here is an example: https://pastebin.com/N6EX2k2R
     *
     *
     *
     * @throws InterruptedException
     */

    @Override
    public void runOpMode() {
        moveToCoordinate(currentDirection, 0.7, 3, 1.23);
        moveToCoordinate(currentDirection, 0.7, 1.23, 3);
        moveToCoordinate(currentDirection, 0.7, 3, 2.5);
        moveToCoordinate(currentDirection, 0.7, 0, 0);


        List<Coordinate> coords = new LinkedList<>();
        coords.add(new Coordinate(3,1.23, currentDirection, 0.7, 0));
        coords.add(new Coordinate(1.23, 3, currentDirection, 0.7, 0));
        coords.add(new Coordinate(3, 2.5, currentDirection, 0.7, 0));

        coords.add(new Coordinate(0, 0, currentDirection, 0.7, 0, new Coordinate.Action(){
            @Override
            public void run() {
                ServoArm exampleArm = new ServoArm(hardwareMap, ProjectGod.this);
                exampleArm.extendArm();
            }
        }));

        moveMap(coords);

    }

    /**
     * This method is used to allow the robot to make a series of moves quickly and repeat them
     * at any point later in the code. It is advised that you keep a list of what move happens
     * at what index somewhere outside of this code so that you can see where to reuse Coordinate
     * objects.
     *
      * @param coordinateMap
     */

    public void moveMap(List<Coordinate> coordinateMap) {
        for(Coordinate c: coordinateMap) {
            if(c.action == null) {
                moveToCoordinate(c.endDirection, c.speed, c.x, c.y);
                try {
                    Thread.sleep(c.sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                moveToCoordinate(c.endDirection, c.speed, c.x, c.y);
                try {
                    Thread.sleep(c.sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                c.action.run();
            }
        }
    }

    /**
     * This is the main move method for movement. When executed, this method moves the
     * robot to the coordinates specified by the user within a small margin of error.
     *
     * X and Y cord parameters are what you use to specify where the robot should move to.
     * The origin position is where the robot is when the autonomous starts and is defined as (0,0)
     *
     * The direction on the field that the robot moves when told to move forward is considered positive y
     * and the direction on the field that the robot moves when told to turn right 90º and move forward is
     * considered positive x. The reverse of those two outcomes is considered the negative.
     *
     * Setting the endDirection field to null means the robot will just stay the way it's facing
     * when the move ends.
     *
     * @param endDirection
     * @param speed
     */

    public void moveToCoordinate(Direction endDirection, double speed, double movex, double movey) {
        if(endDirection == null){
            if(movex == 0){
                movex = tiledistance*x;
            }
            if(movey == 0){
                movey = tiledistance*x;
            }
            if (movex < ProjectGod.x) {
                ProjectGod.currentDirection = Direction.BACKWARD;
            } else {
                ProjectGod.currentDirection = Direction.FORWARD;
            }
            drive.moveInches(ProjectGod.currentDirection, ProjectGod.tiledistance * movex, speed);
            if (movey < ProjectGod.y) {
                rotate(Direction.COUNTERCLOCKWISE, 90, 0.8);
                ProjectGod.currentDirection = Direction.BACKWARD;
            } else {
                rotate(Direction.CLOCKWISE, 90, 0.8);
                ProjectGod.currentDirection = Direction.FORWARD;
            }
            drive.moveInches(ProjectGod.currentDirection, ProjectGod.tiledistance * movey, speed);
        } else {
            if (movex == 0) {
                movex = tiledistance * x;
            }
            if (movey == 0) {
                movey = tiledistance * x;
            }
            if (movex < ProjectGod.x) {
                ProjectGod.currentDirection = Direction.BACKWARD;
            } else {
                ProjectGod.currentDirection = Direction.FORWARD;
            }
            drive.moveInches(ProjectGod.currentDirection, ProjectGod.tiledistance * movex, speed);
            if (movey < ProjectGod.y) {
                rotate(Direction.COUNTERCLOCKWISE, 90, 0.8);
                ProjectGod.currentDirection = Direction.BACKWARD;
            } else {
                rotate(Direction.CLOCKWISE, 90, 0.8);
                ProjectGod.currentDirection = Direction.FORWARD;
            }
            drive.moveInches(ProjectGod.currentDirection, ProjectGod.tiledistance * movey, speed);

            if (currentDirection != endDirection) {
                if (currentDirection == Direction.BACKWARD && endDirection == Direction.FORWARD) {
                    rotate(Direction.CLOCKWISE, 180, 0.8);
                } else if (currentDirection == Direction.BACKWARD && endDirection == Direction.LEFT) {
                    rotate(Direction.CLOCKWISE, 90, 0.8);
                } else if (currentDirection == Direction.BACKWARD && endDirection == Direction.RIGHT) {
                    rotate(Direction.COUNTERCLOCKWISE, 180, 0.8);
                } else if (currentDirection == Direction.FORWARD && endDirection == Direction.BACKWARD) {
                    rotate(Direction.CLOCKWISE, 180, 0.8);
                } else if (currentDirection == Direction.FORWARD && endDirection == Direction.LEFT) {
                    rotate(Direction.COUNTERCLOCKWISE, 90, 0.8);
                } else if (currentDirection == Direction.FORWARD && endDirection == Direction.RIGHT) {
                    rotate(Direction.CLOCKWISE, 180, 0.8);
                } else if (currentDirection == Direction.LEFT && endDirection == Direction.FORWARD) {
                    rotate(Direction.CLOCKWISE, 90, 0.8);
                } else if (currentDirection == Direction.LEFT && endDirection == Direction.BACKWARD) {
                    rotate(Direction.COUNTERCLOCKWISE, 90, 0.8);
                } else if (currentDirection == Direction.BACKWARD && endDirection == Direction.RIGHT) {
                    rotate(Direction.COUNTERCLOCKWISE, 90, 0.8);
                }
            }
        }
    }

//
//    /**
//     * This is the main move method for movement. When executed, this method moves the
//     * robot to the coordinates specified by the user within a small margin of error.
//     *
//     * X and Y cord parameters are what you use to specify where the robot should move to.
//     * The origin position is where the robot is when the autonomous starts and is defined as (0,0)
//     *
//     * The direction on the field that the robot moves when told to move forward is considered positive y
//     * and the direction on the field that the robot moves when told to turn right 90º and move forward is
//     * considered positive x. The reverse of those two outcomes is considered the negative.
//     *
//     * The system that specifies speed is called a variable speed pattern. The first array is used
//     * to specify the actual speed that the robot travels at, and the second array is used to
//     * specify the time that speed runs for
//     *
//     * for example: if speed[0] was: 1 the speed of the robot would be 1 and if time[0] was
//     * 1 then the robot would go that speed for 1 second before trying the next speed.
//     * if there is no next speed and the robot is not stopped it will continue to use the last
//     * available speed.
//     *
//     * @param endDirection
//     * @param speed
//     * @param movex
//     * @param movey
//     */
//
//      @Deprecated
//    public void moveToCoordinate(Direction endDirection, double[] speed, double[] time, int movex, int movey) {
//        double speeda = 0;
//        double timeToMove = 0;
//
//        for (int i = 0; i < speed.length; i++) {
//            speeda = speed[i];
//            timeToMove = time[i];
//
//            long stopTime = System.currentTimeMillis() + Long.parseLong((timeToMove * 1000) + "");
//
//            while (System.currentTimeMillis() != stopTime) {
//
//                if (movex < ProjectGod.x) {
//                    ProjectGod.currentDirection = Direction.BACKWARD;
//                } else {
//                    ProjectGod.currentDirection = Direction.FORWARD;
//                }
//                drive.moveInches(ProjectGod.currentDirection, ProjectGod.tiledistance * movex, speeda);
//                if (movey < ProjectGod.y) {
//                    drive.rotateDegrees(Direction.COUNTERCLOCKWISE, 90, 0.8);
//                    ProjectGod.currentDirection = Direction.BACKWARD;
//                } else {
//                    drive.rotateDegrees(Direction.CLOCKWISE, 90, 0.8);
//                    ProjectGod.currentDirection = Direction.FORWARD;
//                }
//                drive.moveInches(ProjectGod.currentDirection, ProjectGod.tiledistance * movex, speeda);
//            }
//            continue;
//        }
//    }

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


    /**
     * Telemetry Output of Propagation State Changes
     *
     * @param ps
     */

    public void writePropagationState(PropagationState ps){
        telemetry.addLine("-[ " + ps.name() + " ]-");
    }

    /**
     * This method is how the autonomous is initialized.
     */

    public void initalize(){
        x = 0;
        y = 0;
        drive = new MecanumEncoderDrive(hardwareMap, this, CustomizedRobotParameters.ROBOT_PARAMETERS);

        drive.leftRearMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        drive.rightRearMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        drive.leftDriveMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        drive.rightDriveMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /**
     * This enum is what provides the PropagationStates that the TelemetryRecorder can
     * accurately output what the robot is doing and when
     */

    public enum PropagationState {
        STOPPED, RUNNING, HOMING_TO_ORIGIN;
    }
}
