package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.util.Randomizer;
import rolygon.ai.comm.Channel;
import rolygon.ai.comm.Value;

import java.awt.*;

/**
 * Created by nobody on 1/11/2017.
 */
public class SeekCornerBehavior implements Behavior {
    static Direction[] goals = {
        new Direction((float)Math.PI / 2),
        new Direction(0),
        new Direction((float) Math.PI / -2),
        new Direction((float) Math.PI)
    };
    int idxGoal;
    int numTries;

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

    MapLocation origin;
    int offsetDeg;

    // one for each corner, starting in UR, going opp, up, opp: LL, UL, LR
    boolean[] found = { false, false, false, false };
    int goal;
    boolean goingToOrigin;

    int limits[] = {100, 100, 0, 0};

    public void initialize(RobotController rc) {
        origin = rc.getLocation();
        offsetDeg = Randomizer.rollDie(360);
        idxGoal = 0;
        numTries = 0;
    }

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        MapLocation robotLocation = rc.getLocation();
        if (goingToOrigin) {
            // TODO arbitrary test
            if (robotLocation.distanceTo(origin) > 3.0) {
                tryMove(rc, robotLocation.directionTo(origin), 45, 1);
            } else {
                goingToOrigin = false;
            }
        } else {
            if (numTries > 4) {
                goingToOrigin = true;
                idxGoal++;
                if (idxGoal >= goals.length) {
                    offsetDeg = Randomizer.rollDie(360);
                    idxGoal %= goals.length;
                }
            }
            Direction toGoal = goals[idxGoal].rotateLeftDegrees(offsetDeg);
            if (tryMove(rc, toGoal, 45, 1)) {
                findExtents(rc);
                numTries = 0;
            } else {
                numTries++;
            }
        }

        return RunResult.IN_PROGRESS;
    }

    private void findExtents(RobotController rc) throws GameActionException {
        MapLocation loc = rc.getLocation();
        float mapLeft = rc.readBroadcast(Channel.MAP_EXT_LEFT);
        float mapRight = rc.readBroadcast(Channel.MAP_EXT_RIGHT);
        float mapTop = rc.readBroadcast(Channel.MAP_EXT_TOP);
        float mapBottom = rc.readBroadcast(Channel.MAP_EXT_BOTTOM);
        boolean needToNotify = false;
        if (loc.x < mapLeft) {
            rc.broadcast(Channel.MAP_EXT_LEFT, (int)Math.floor(loc.x));
            needToNotify = true;
        }
        if (loc.x > mapRight) {
            rc.broadcast(Channel.MAP_EXT_RIGHT, (int)Math.ceil(loc.x));
            needToNotify = true;
        }
        if (loc.y > mapTop) {
            rc.broadcast(Channel.MAP_EXT_TOP, (int)Math.ceil(loc.y));
            needToNotify = true;
        }
        if (loc.y < mapBottom) {
            rc.broadcast(Channel.MAP_EXT_BOTTOM, (int)Math.floor(loc.y));
            needToNotify = true;
        }
        if (needToNotify) {
            rc.broadcast(Channel.MAP_EXT_CHANGED, Value.TRUE);
        }
    }

    //@Override
    public RunResult run2(RobotController rc, Context context) throws GameActionException {
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
                if (tryMove(rc, new Direction((float)Math.PI/4), (float)45, 1)) {
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
