package rolygon.ai;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.util.RandomDirection;

/**
 * Created by nobody on 1/12/2017.
 */
public class RandomMoveBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        Direction dir = RandomDirection.getDirection();
        if (rc.canMove(dir)) {
            rc.move(dir);
        }
        return RunResult.FINISHED;
    }
}
