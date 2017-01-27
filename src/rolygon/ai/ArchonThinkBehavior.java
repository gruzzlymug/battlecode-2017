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

        float numBullets = rc.getTeamBullets();

        // late game strategy...
        if (rc.getRoundNum() > 2990) {
            // no need for bullets
            rc.donate(numBullets);
        }

        // donate 10%
        if (numBullets > 200) {
            rc.donate(numBullets * 0.05F);
        }

        return RunResult.SKIPPED;
    }
}
