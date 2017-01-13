package rolygon.ai;

import battlecode.common.*;

/**
 * Created by nobody on 1/11/2017.
 */
public class SeekCornerBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc) throws GameActionException {
        Direction direction = new Direction((float)Math.PI / -2);
//        if (rc.onTheMap()) {
            // completely on the map
            if (rc.canMove(direction)) {
                rc.move(direction);
                return RunResult.IN_PROGRESS;
            } else {
                // ideally be partially off the map, but could just be blocked or ...
                return RunResult.FINISHED;
            }
//        }
    }
}
