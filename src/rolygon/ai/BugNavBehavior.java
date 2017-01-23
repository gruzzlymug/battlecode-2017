package rolygon.ai;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import ddg.ai.Behavior;
import ddg.ai.Context;
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
    private MapLocation goal;

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        if (goal == null) {
            return RunResult.SKIPPED;
        }

        MapLocation robotLocation = rc.getLocation();
        Direction toGoal = robotLocation.directionTo(goal);
        if (rc.canMove(toGoal)) {
            rc.move(toGoal);
        } else {
            // try to move left
            for (int i = 0; i < 8; i++) {
                toGoal.rotateLeftDegrees(45);
            }
        }
        return RunResult.IN_PROGRESS;
    }

    // public for now, but this should be private? and handled through generic interface
    // via pub-sub with tree.
    public void setGoal(MapLocation goal) {
        this.goal = goal;
    }
}
