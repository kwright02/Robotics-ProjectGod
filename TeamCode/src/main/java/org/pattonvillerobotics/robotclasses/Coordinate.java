package org.pattonvillerobotics.robotclasses;

import org.pattonvillerobotics.commoncode.enums.Direction;

/**
 * Created by wrightk03 on 3/14/18.
 *
 * This class is used purely to allow the mapMove method in the ProjectGod class
 * to easily obtain information about each move step and perform it accurately.
 */

public class Coordinate {

    //This is the coordinate that is represented by the class
    public double x,y;

    //This is the direction in which the robot should face at the end of the move
    public Direction endDirection;

    //This is the number of miliseconds that the robot should sleep at the end of the move
    public long sleepTime;

    //This is the power the move should use
    public double speed;

    //This is the event that takes places after the the move ends and the sleep time passes
    public Action action;

    public Coordinate(double x, double y, Direction endDirection, double speed, long sleepTime) {
        this.x = x;
        this.y = y;
        this.endDirection = endDirection;
        this.sleepTime = sleepTime;
        this.speed = speed;
    }

    public Coordinate(double x, double y, Direction endDirection, double speed, long sleepTime, Action action) {
        this.x = x;
        this.y = y;
        this.endDirection = endDirection;
        this.sleepTime = sleepTime;
        this.speed = speed;
        this.action = action;
    }


    public interface Action {
        public void run();
    }
}
