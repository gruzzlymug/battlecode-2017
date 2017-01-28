package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.ai.Keys;

/**
 * Created by nobody on 1/16/2017.
 */
public class ArchonThinkBehavior implements Behavior {
    final private static int CHANNEL_GARDENER_SUM = 20;
    final private static int CHANNEL_LUMBERJACK_SUM = 21;
    final private static int CHANNEL_SCOUT_SUM = 22;
    final private static int CHANNEL_SOLDIER_SUM = 23;

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        rc.senseBroadcastingRobotLocations();
        rc.senseNearbyTrees();
        rc.senseNearbyRobots();

        countArmy(rc, context);
        manageBullets(rc);

        return RunResult.SKIPPED;
    }

    private void countArmy(RobotController rc, Context context) throws GameActionException {
        int numGardeners = countUnits(rc, CHANNEL_GARDENER_SUM);
        int numLumberjacks = countUnits(rc, CHANNEL_LUMBERJACK_SUM);
        int numScouts = countUnits(rc, CHANNEL_SCOUT_SUM);
        int numSoldiers = countUnits(rc, CHANNEL_SOLDIER_SUM);

        context.memorize(Keys.NUM_GARDENERS, numGardeners);
        context.memorize(Keys.NUM_LUMBERJACKS, numLumberjacks);
        context.memorize(Keys.NUM_SCOUTS, numScouts);
        context.memorize(Keys.NUM_SOLDIERS, numSoldiers);
    }

    private int countUnits(RobotController rc, int channel) throws GameActionException {
        int numUnits = rc.readBroadcast(channel);
        System.out.println(">> " + numUnits + " " + channel + " units");
        rc.broadcast(channel, 0);
        return numUnits;
    }

    private void manageBullets(RobotController rc) throws GameActionException {
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
    }
}
