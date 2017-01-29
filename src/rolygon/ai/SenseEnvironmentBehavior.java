package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;
import rolygon.ai.comm.Channel;

/**
 * Created by nobody on 1/14/2017.
 */
public class SenseEnvironmentBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        float mapLeft = rc.readBroadcast(Channel.MAP_EXT_LEFT);
        float mapRight = rc.readBroadcast(Channel.MAP_EXT_RIGHT);
        float mapTop = rc.readBroadcast(Channel.MAP_EXT_TOP);
        float mapBottom = rc.readBroadcast(Channel.MAP_EXT_BOTTOM);

        float width = mapRight - mapLeft;
        float height = mapTop - mapBottom;

        // TODO set up standard robot environmental knowledge context
        RobotInfo[] nearbyRobots = rc.senseNearbyRobots();
        for (RobotInfo robot : nearbyRobots) {
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
//            System.out.println(id + ":" + type + " " + health + "; (" + idxCol + ", " + idxRow + ")");
            int channel = Channel.INFLUENCE_MAP + idxRow * 10 + idxCol;
            int adder = (rc.getTeam() == robot.getTeam()) ? 1 : -1;
            rc.broadcast(channel, adder+rc.readBroadcast(channel));
        }
        return RunResult.SKIPPED;
    }
}
