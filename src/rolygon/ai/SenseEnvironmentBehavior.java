package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;

/**
 * Created by nobody on 1/14/2017.
 */
public class SenseEnvironmentBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        // TODO set up standard robot environmental knowledge context
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
        for (RobotInfo robot : nearbyRobots) {
            int id = robot.getID();
            RobotType type = robot.getType();
            double health = robot.getHealth();
            MapLocation location = robot.getLocation();
            System.out.println(id + ":" + type + ":" + health + " " + location.x + ", " + location.y);
        }
        return RunResult.FINISHED;
    }
}
