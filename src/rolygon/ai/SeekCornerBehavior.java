package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;

/**
 * Created by nobody on 1/11/2017.
 */
public class SeekCornerBehavior implements Behavior {
    static Direction up;
    static Direction right;
    static Direction down;
    static Direction left;

    static {
        up = new Direction((float)Math.PI / 2);
        right = new Direction(0);
        down = new Direction((float) Math.PI / -2);
        left = new Direction((float) Math.PI);
    }

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        boolean canGoUp = rc.canMove(up);
        boolean canGoRight = rc.canMove(right);
        boolean canGoDown = rc.canMove(down);
        boolean canGoLeft = rc.canMove(left);

        if (canGoUp && canGoRight && canGoDown && canGoLeft) {
            rc.move(right);
            return RunResult.IN_PROGRESS;
        } else if (!canGoUp && !canGoLeft) {
            // upper left corner
            return RunResult.FINISHED;
        } else if (!canGoUp && !canGoRight) {
            // upper right
            return RunResult.FINISHED;
        } else if (!canGoDown && !canGoLeft) {
            // lower left
            return RunResult.FINISHED;
        } else if (!canGoDown && !canGoRight) {
            // lower right
            return RunResult.FINISHED;
        } else if (canGoUp) {
            rc.move(up);
            return RunResult.IN_PROGRESS;
        }
        return RunResult.SKIPPED;
    }
}
