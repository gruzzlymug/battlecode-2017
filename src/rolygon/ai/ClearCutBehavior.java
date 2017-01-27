package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;

/**
 * Created by nobody on 1/16/2017.
 */
public class ClearCutBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        MapLocation robotLocation = rc.getLocation();
        TreeInfo[] trees = rc.senseNearbyTrees();
        for (TreeInfo tree : trees) {
            if (tree.getTeam() == rc.getTeam()) {
                continue;
            }
            MapLocation treeLocation = tree.getLocation();
            if (rc.canShake(treeLocation)) {
                rc.shake(treeLocation);
            }
            Direction toTree = robotLocation.directionTo(treeLocation);
            if (rc.canMove(toTree) && !rc.hasMoved()) {
                rc.move(toTree);
            }
            if (rc.canChop(treeLocation)) {
                rc.chop(treeLocation);
                return RunResult.IN_PROGRESS;
            }
        }
        return RunResult.SKIPPED;
    }
}
