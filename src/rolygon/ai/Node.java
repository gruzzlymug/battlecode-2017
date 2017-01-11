package rolygon.ai;

import battlecode.common.*;

/**
 * Created by nobody on 1/11/2017.
 */
public interface Node {
    public Behavior evaluate(RobotController rc) throws GameActionException;
}
