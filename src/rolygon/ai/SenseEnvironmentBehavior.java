package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;
import rolygon.ai.env.InfluenceMap;

/**
 * Created by nobody on 1/14/2017.
 */
public class SenseEnvironmentBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        InfluenceMap.setController(rc);

        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
        InfluenceMap.addRobots(nearbyRobots);

        return RunResult.SKIPPED;
    }
}
