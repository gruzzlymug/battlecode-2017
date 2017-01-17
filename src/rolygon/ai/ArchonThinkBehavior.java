package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.ai.Node;

/**
 * Created by nobody on 1/16/2017.
 */
public class ArchonThinkBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        rc.senseBroadcastingRobotLocations();
        rc.senseNearbyTrees();
        rc.senseNearbyRobots();

        if (rc.getTeamBullets() > 200) {
            rc.donate(10);
        }
        return RunResult.FINISHED;
    }
}
