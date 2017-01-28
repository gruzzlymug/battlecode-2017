package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
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

    private static float mapLeft = 500;
    private static float mapTop = 0;
    private static float mapRight = 0;
    private static float mapBottom = 500;

    // bottom left is origin (see specs if skeptical)
    private MapLocation bottomLeft;
    private MapLocation topRight;

    // TODO consider adding to interface
    public void initialize(RobotController rc) {
        MapLocation[] friendlyArchonLocations = rc.getInitialArchonLocations(rc.getTeam());
        for (MapLocation loc : friendlyArchonLocations) {
            findExtents(loc);
        }

        MapLocation[] enemyArchonLocations = rc.getInitialArchonLocations(rc.getTeam().opponent());
        for (MapLocation loc : enemyArchonLocations) {
            findExtents(loc);
        }

        topRight = new MapLocation(mapRight, mapTop);
        bottomLeft = new MapLocation(mapLeft, mapBottom);
    }

    private void findExtents(MapLocation loc) {
        if (loc.x < mapLeft) {
            mapLeft = loc.x;
        }
        if (loc.x > mapRight) {
            mapRight = loc.x;
        }
        if (loc.y > mapTop) {
            mapTop = loc.y;
        }
        if (loc.y < mapBottom) {
            mapBottom = loc.y;
        }
    }

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        MapLocation[] broadcasters = rc.senseBroadcastingRobotLocations();
        System.out.println("--[ " + rc.getRoundNum() + " ]-----------------");
        for (MapLocation broadcastLocation : broadcasters) {
            System.out.println(broadcastLocation.x + ", " + broadcastLocation.y);
        }

        rc.setIndicatorDot(topRight, 0, 0, 0);
        rc.setIndicatorDot(bottomLeft, 255, 255, 255);
        System.out.println("> " + topRight + ", " + bottomLeft);

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
