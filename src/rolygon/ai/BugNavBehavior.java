package rolygon.ai;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.ai.Key;
import ddg.ai.Node;

/**
 * Created by nobody on 1/23/2017.
 *
 * Create some kind of dispatcher/router/pub-sub thing in the behavior tree.
 * Allow behaviors in the tree to register with the tree to handle events.
 * Allow clients of the tree to post events to the tree.
 * Tree will route events (such as 'Set Goal') along with 'hash' of params.
 */
public class BugNavBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        MapLocation goal = (MapLocation)context.recall(Key.ATTACK_TARGET);
        if (goal == null) {
            return RunResult.SKIPPED;
        }

        MapLocation robotLocation = rc.getLocation();
        Direction toGoal = robotLocation.directionTo(goal);
        if (rc.canMove(toGoal)) {
            rc.move(toGoal);
            return RunResult.IN_PROGRESS;
        } else {
            // try to move left
            boolean idIsEven = ((rc.getID() % 1) == 0);
            for (int i = 0; i < 8; i++) {
                toGoal = idIsEven ? toGoal.rotateRightDegrees(45) : toGoal.rotateRightDegrees(45);
                if (rc.canMove(toGoal)) {
                    rc.move(toGoal);
                    return RunResult.IN_PROGRESS;
                }
            }
        }
        return RunResult.SKIPPED;
    }
}
