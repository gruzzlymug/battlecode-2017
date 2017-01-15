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

    // one for each corner, starting in UR, going opp, up, opp: LL, UL, LR
    boolean[] found = { false, false, false, false };
    int goal;

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        boolean canGoUp = rc.canMove(up);
        boolean canGoRight = rc.canMove(right);
        boolean canGoDown = rc.canMove(down);
        boolean canGoLeft = rc.canMove(left);

        if (!canGoUp && !canGoLeft) {
            // upper left corner
            System.out.println("found UL");
            found[2] = true;
            return RunResult.FINISHED;
        } else if (!canGoUp && !canGoRight) {
            // upper right
            System.out.println("found UR");
            found[0] = true;
            return RunResult.FINISHED;
        } else if (!canGoDown && !canGoLeft) {
            // lower left
            System.out.println("found LL");
            found[1] = true;
            return RunResult.FINISHED;
        } else if (!canGoDown && !canGoRight) {
            // lower right
            System.out.println("found LR");
            found[3] = true;
            return RunResult.FINISHED;
        }

        goal = -1;
        for (int i = 0; i < found.length; i++) {
            if (!found[i]) {
                goal = i;
                break;
            }
        }

        up = new Direction((float)Math.PI / 2);
        right = new Direction(0);
        down = new Direction((float) Math.PI / -2);
        left = new Direction((float) Math.PI);
        switch (goal) {
            case 0:
                // ur
                if (tryMove(rc, new Direction((float)Math.PI/4), (float)22.5, 1)) {
                    return RunResult.IN_PROGRESS;
                }
                break;
            case 1:
                // ll
                if (tryMove(rc, new Direction((float)(-3*Math.PI/4)), (float)22.5, 1)) {
                    return RunResult.IN_PROGRESS;
                }
                break;
            case 2:
                // ul
                if (tryMove(rc, new Direction((float)(3*Math.PI/4)), (float)22.5, 1)) {
                    return RunResult.IN_PROGRESS;
                }
                break;
            case 3:
                // lr
                if (tryMove(rc, new Direction((float)-Math.PI/4), (float)22.5, 1)) {
                    return RunResult.IN_PROGRESS;
                }
                break;
        }
        return RunResult.SKIPPED;
    }

    /**
     * Attempts to move in a given direction, while avoiding small obstacles direction in the path.
     *
     * @param dir The intended direction of movement
     * @param degreeOffset Spacing between checked directions (degrees)
     * @param checksPerSide Number of extra directions checked on each side, if intended direction was unavailable
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(RobotController rc, Direction dir, float degreeOffset, int checksPerSide) throws GameActionException {

        // First, try intended direction
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        }

        // Now try a bunch of similar angles
        boolean moved = false;
        int currentCheck = 1;

        while(currentCheck<=checksPerSide) {
            // Try the offset of the left side
            if(rc.canMove(dir.rotateLeftDegrees(degreeOffset*currentCheck))) {
                rc.move(dir.rotateLeftDegrees(degreeOffset*currentCheck));
                return true;
            }
            // Try the offset on the right side
            if(rc.canMove(dir.rotateRightDegrees(degreeOffset*currentCheck))) {
                rc.move(dir.rotateRightDegrees(degreeOffset*currentCheck));
                return true;
            }
            // No move performed, try slightly further
            currentCheck++;
        }

        // A move never happened, so return false.
        return false;
    }
}
