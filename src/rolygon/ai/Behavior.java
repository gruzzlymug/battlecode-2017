package rolygon.ai;

import battlecode.common.*;

/**
 * Created by nobody on 1/11/2017.
 */
public interface Behavior {
    public void run(RobotController rc) throws GameActionException;
}
