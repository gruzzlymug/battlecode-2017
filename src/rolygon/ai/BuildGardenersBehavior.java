package rolygon.ai;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.comm.Channel;
import ddg.util.Randomizer;

/**
 * Created by nobody on 1/14/2017.
 *
 * This needs to be much smarter, duh!
 */
public class BuildGardenersBehavior implements Behavior {
    final int MAX_GARDENERS = 10;

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        int numGardeners = rc.readBroadcast(Channel.GARDENER_COUNT);
        if (numGardeners >= MAX_GARDENERS) {
            return RunResult.SKIPPED;
        }

        // don't build gardeners too quickly
        int currentRound = rc.getRoundNum();
        if (currentRound < numGardeners * 200) {
            return RunResult.SKIPPED;
        }

        Direction dir = Randomizer.getRandomDirection();
        if (rc.canBuildRobot(RobotType.GARDENER, dir)) {
            rc.buildRobot(RobotType.GARDENER, dir);
            numGardeners++;
        }
        // by using finished instead of skipped here, archon won't move until all gardeners built
        return RunResult.FINISHED;
    }
}
