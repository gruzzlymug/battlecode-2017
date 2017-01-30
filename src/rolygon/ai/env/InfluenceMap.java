package rolygon.ai.env;

import battlecode.common.*;
import ddg.ai.Node;
import ddg.comm.Channel;

import java.awt.*;

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

    private static Team opponent;

    public static void readExtents(RobotController rc) throws GameActionException {
        mapLeft = rc.readBroadcast(Channel.MAP_EXT_LEFT);
        mapRight = rc.readBroadcast(Channel.MAP_EXT_RIGHT);
        mapTop = rc.readBroadcast(Channel.MAP_EXT_TOP);
        mapBottom = rc.readBroadcast(Channel.MAP_EXT_BOTTOM);

        width = mapRight - mapLeft;
        height = mapTop - mapBottom;

        opponent = rc.getTeam().opponent();
    }

    public static void addRobots(RobotController rc, RobotInfo[] robots) throws GameActionException {
        for (RobotInfo robot : robots) {
            int id = robot.getID();
            RobotType type = robot.getType();
            double health = robot.getHealth();
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
        }
    }

    public static void reset() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
            }
        }
    }

    public static float[][] getGrid() {
        return null;
    }
}
