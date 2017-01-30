package rolygon.ai.env;

import battlecode.common.*;
import ddg.comm.Channel;

/**
 * Created by nobody on 1/16/2017.
 *
 * Grid-based map of the environment...who controls which parts of the map?
 */
public class InfluenceMap {
    // bottom left corner of map is the origin
    // maps range from 30x30 to 100x100
    // positions will be offset by a random amount
    private static float mapLeft;
    private static float mapRight;
    private static float mapTop;
    private static float mapBottom;

    protected static float width;
    protected static float height;

    private static RobotController rc;
    private static Team opponent;

    public static void setController(RobotController rc) throws GameActionException {
        // this is saved for broadcasting
        InfluenceMap.rc = rc;

        mapLeft = rc.readBroadcast(Channel.MAP_EXT_LEFT);
        mapRight = rc.readBroadcast(Channel.MAP_EXT_RIGHT);
        mapTop = rc.readBroadcast(Channel.MAP_EXT_TOP);
        mapBottom = rc.readBroadcast(Channel.MAP_EXT_BOTTOM);

        width = mapRight - mapLeft;
        height = mapTop - mapBottom;

        opponent = rc.getTeam().opponent();
    }

    public static void addRobots(RobotInfo[] robots) throws GameActionException {
        for (RobotInfo robot : robots) {
            int id = robot.getID();
            if (isKnown(id)) {
                continue;
            }

//            RobotType type = robot.getType();
//            double health = robot.getHealth();
            MapLocation location = robot.getLocation();

            float adjX = location.x - mapLeft;
            adjX = Math.max(0, Math.min(adjX, width));
            int idxCol = (int)((adjX / width) * 10);
            float adjY = location.y - mapBottom;
            adjY = Math.max(0, Math.min(adjY, height));
            int idxRow = (int)((adjY / height) * 10);

            //System.out.println(id + ":" + type + " " + health + "; (" + idxCol + ", " + idxRow + ")");

            int channel = Channel.INFLUENCE_MAP + idxRow * 10 + idxCol;
            int adder = (robot.getTeam() == opponent) ? -1 : 1;
            rc.broadcast(channel, adder+rc.readBroadcast(channel));

            int numProcessed = rc.readBroadcast(Channel.PROCESSED_IDS) + 1;
            rc.broadcast(Channel.PROCESSED_IDS, numProcessed);
            rc.broadcast(Channel.PROCESSED_IDS + numProcessed, id);
        }
    }

    private static boolean isKnown(int id) throws GameActionException {
        int numIds = rc.readBroadcast(Channel.PROCESSED_IDS);
        for (int i = 0; i < numIds; i++) {
            int otherId = rc.readBroadcast(Channel.PROCESSED_IDS + i);
            if (id == otherId) {
                return true;
            }
        }
        return false;
    }

    public static void reset() throws GameActionException {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int channel = Channel.INFLUENCE_MAP + row * 10 + col;
                if (rc.readBroadcast(channel) != 0) {
                    rc.broadcast(channel, 0);
                }
            }
        }
        rc.broadcast(Channel.PROCESSED_IDS, 0);
    }

    public static void debugDraw() throws GameActionException {
        float w10 = width / 10;
        float h10 = height / 10;
        int perUnit = 128;

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                int channel = Channel.INFLUENCE_MAP + row * 10 + col;
                int value = rc.readBroadcast(channel);
                float newX = mapLeft + col * w10 + (width / 20);
                float newY = mapBottom + row * h10 + (height / 20);
                MapLocation dot = new MapLocation(newX, newY);
                int shade = 128 + value * perUnit;
                shade = Math.max(0, Math.min(255, shade));
                rc.setIndicatorDot(dot, shade, shade, shade);
            }
        }
    }
}
