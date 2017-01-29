package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.ai.Key;
import rolygon.ai.comm.Channel;
import rolygon.ai.comm.Value;

/**
 * Created by nobody on 1/16/2017.
 *
 * GameConstants.BROADCAST_MAX_CHANNELS = 10,000
 */
public class ArchonThinkBehavior implements Behavior {
    private static float mapLeft = 500;
    private static float mapTop = 0;
    private static float mapRight = 0;
    private static float mapBottom = 500;

    // bottom left is origin
    private MapLocation bottomLeft;
    private MapLocation topRight;

    private ArchonThinkBehavior() {
        // prevent default construction
    }

    public ArchonThinkBehavior(RobotController rc) throws GameActionException {
        MapLocation[] friendlyArchonLocations = rc.getInitialArchonLocations(rc.getTeam());
        for (MapLocation loc : friendlyArchonLocations) {
            findExtents(loc);
        }

        MapLocation[] enemyArchonLocations = rc.getInitialArchonLocations(rc.getTeam().opponent());
        for (MapLocation loc : enemyArchonLocations) {
            findExtents(loc);
        }

        rc.broadcast(Channel.MAP_EXT_BOTTOM, (int)Math.floor(mapBottom));
        rc.broadcast(Channel.MAP_EXT_LEFT, (int)Math.floor(mapLeft));
        rc.broadcast(Channel.MAP_EXT_TOP, (int)Math.ceil(mapTop));
        rc.broadcast(Channel.MAP_EXT_RIGHT, (int)Math.ceil(mapRight));
        rc.broadcast(Channel.MAP_EXT_CHANGED, Value.TRUE);

        topRight = new MapLocation(mapRight, mapTop);
        bottomLeft = new MapLocation(mapLeft, mapBottom);
    }

    private void findExtents(MapLocation loc) {
        if (loc.x < mapLeft) {
            mapLeft = loc.x;
        } else if (loc.x > mapRight) {
            mapRight = loc.x;
        }
        if (loc.y > mapTop) {
            mapTop = loc.y;
        } else if (loc.y < mapBottom) {
            mapBottom = loc.y;
        }
    }

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        if (rc.readBroadcast(Channel.MAP_EXT_CHANGED) == Value.TRUE) {
            mapLeft = rc.readBroadcast(Channel.MAP_EXT_LEFT);
            mapRight = rc.readBroadcast(Channel.MAP_EXT_RIGHT);
            mapTop = rc.readBroadcast(Channel.MAP_EXT_TOP);
            mapBottom = rc.readBroadcast(Channel.MAP_EXT_BOTTOM);
            topRight = new MapLocation(mapRight, mapTop);
            bottomLeft = new MapLocation(mapLeft, mapBottom);
            rc.broadcast(Channel.MAP_EXT_CHANGED, Value.FALSE);
        }

//        MapLocation[] broadcasters = rc.senseBroadcastingRobotLocations();
//        System.out.println("--[ " + rc.getRoundNum() + " ]-----------------");
//        for (MapLocation broadcastLocation : broadcasters) {
//            System.out.println(broadcastLocation.x + ", " + broadcastLocation.y);
//        }

        rc.setIndicatorDot(topRight, 128, 128, 128);
        rc.setIndicatorDot(bottomLeft, 128, 128, 128);
//
//        rc.senseNearbyTrees();
//        rc.senseNearbyRobots();

        countArmy(rc, context);
        manageBullets(rc);

        if ((int)context.recall(Key.NUM_SCOUTS) == 0 && rc.readBroadcast(Channel.ATTACK_TARGET) != 0) {
            rc.broadcast(Channel.ATTACK_TARGET, 0);
        }

        // influence map
        if (0 == 0) {
            MapLocation attackTarget = null;
            int worstScore = 0;

            float width = mapRight - mapLeft;
            float height = mapTop - mapBottom;

            float w10 = width / 10;
            float h10 = height / 10;

            for (int row = 0; row < 10; row++) {
                for (int col = 0; col < 10; col++) {
                    int channel = Channel.INFLUENCE_MAP + row * 10 + col;
                    int value = rc.readBroadcast(channel);
                    if (value == 0) {
                        continue;
                    }
                    float newX = mapLeft + col * w10 + (width / 20);
                    float newY = mapBottom + row * h10 + (height / 20);
                    MapLocation dot = new MapLocation(newX, newY);
                    int shade = 128;
                    if (value < 0) {
                        shade = Math.min(0, 128 - value * 16);
                        if (value < worstScore) {
                            worstScore = value;
                            attackTarget = dot;
                        }
                    } else if (value < 0) {
                        shade = Math.max(255, 128 + value * 16);
                    }
                    rc.setIndicatorDot(dot, shade, shade, shade);
                    rc.broadcast(channel, 0);
                }
            }

            if (attackTarget != null) {
                int packedAttack = 1000 * (int)attackTarget.x + (int)attackTarget.y;
                rc.broadcast(Channel.ATTACK_TARGET, packedAttack);
            }
        }

        return RunResult.SKIPPED;
    }

    private void countArmy(RobotController rc, Context context) throws GameActionException {
        int numGardeners = countUnits(rc, Channel.GARDENER_SUM);
        int numLumberjacks = countUnits(rc, Channel.LUMBERJACK_SUM);
        int numScouts = countUnits(rc, Channel.SCOUT_SUM);
        int numSoldiers = countUnits(rc, Channel.SOLDIER_SUM);

//        System.out.println("G: " + numGardeners + ", L: " + numLumberjacks + ", S: " + numScouts + ", X: " + numSoldiers);

        rc.broadcast(Channel.GARDENER_COUNT, numGardeners);
        rc.broadcast(Channel.LUMBERJACK_COUNT, numLumberjacks);
        rc.broadcast(Channel.SCOUT_COUNT, numScouts);
        rc.broadcast(Channel.SOLDIER_COUNT, numSoldiers);

        // TODO this is pointless unless multiple behaviors need it, remove.
        context.memorize(Key.NUM_GARDENERS, numGardeners);
        context.memorize(Key.NUM_LUMBERJACKS, numLumberjacks);
        context.memorize(Key.NUM_SCOUTS, numScouts);
        context.memorize(Key.NUM_SOLDIERS, numSoldiers);
    }

    private int countUnits(RobotController rc, int channel) throws GameActionException {
        int numUnits = rc.readBroadcast(channel);
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
