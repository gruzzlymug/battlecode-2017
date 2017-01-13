package rolygon.ai;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * Created by nobody on 1/12/2017.
 */
public class RandomMoveBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc) throws GameActionException {
        Direction dir = randomDirection();
        if (rc.canMove(dir)) {
            rc.move(dir);
        }
        return RunResult.IN_PROGRESS;
    }

    Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }
}
