package rolygon.ai;

import battlecode.common.*;

/**
 * Created by nobody on 1/11/2017.
 */
public interface Node {
    public enum RunResult {
        SKIPPED, IN_PROGRESS, FINISHED, FAILED;
    }

    public RunResult run(RobotController rc, Context context) throws GameActionException;
}
