package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.ai.Node;

/**
 * Created by nobody on 1/27/2017.
 */
public class MeleeAttackBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        RobotInfo[] nearbyEnemies = (RobotInfo[]) context.recall("nearby_enemies");
        if (nearbyEnemies.length > 0) {
            RobotInfo target = nearbyEnemies[0];
            MapLocation here = rc.getLocation();
            MapLocation goal = target.getLocation();
            Direction toGoal = here.directionTo(goal);
            float howFar = here.distanceTo(goal);
            if (rc.canMove(toGoal) && !rc.hasMoved()) {
                rc.move(toGoal);
            }
            if (rc.canChop(goal)) {
                rc.chop(goal);
            }
        }

        return RunResult.FINISHED;
    }
}

