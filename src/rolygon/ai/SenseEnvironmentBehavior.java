package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import ddg.ai.Behavior;
import ddg.ai.Context;

/**
 * Created by nobody on 1/14/2017.
 */
public class SenseEnvironmentBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        // TODO set up standard robot environmental knowledge context
        return RunResult.FINISHED;
    }
}
