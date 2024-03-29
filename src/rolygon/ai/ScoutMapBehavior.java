package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.loco.Mover;
import ddg.util.Randomizer;
import ddg.comm.Channel;
import ddg.comm.Value;

/**
 * Created by nobody on 1/11/2017.
 */
public class ScoutMapBehavior implements Behavior {
    static Direction[] goals = {
        new Direction((float)Math.PI / 2),
        new Direction(0),
        new Direction((float) Math.PI / -2),
        new Direction((float) Math.PI)
    };

    MapLocation origin;
    int offsetDeg;
    int idxGoal;
    int numTries;
    boolean goingToOrigin;

    private ScoutMapBehavior() {
        // prevent default construction
    }

    public ScoutMapBehavior(RobotController rc) {
        origin = rc.getLocation();
        offsetDeg = Randomizer.rollDie(30) * 12;
    }

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        MapLocation robotLocation = rc.getLocation();
        Direction toGoal;
        if (goingToOrigin) {
            toGoal = robotLocation.directionTo(origin);
            if (numTries > 4) {
                // can't make it back to origin, reset.
                origin = robotLocation;
                goingToOrigin = false;
                offsetDeg = Randomizer.rollDie(30) * 12;
                numTries = 0;
            } else if (robotLocation.distanceTo(origin) > 15.0) {
                if (Mover.tryMove(rc, toGoal, 30, 2)) {
                    numTries = 0;
                } else {
                    numTries++;
                }
            } else {
                // made it back
                goingToOrigin = false;
            }
        } else {
            if (numTries > 4) {
                goingToOrigin = true;
                idxGoal++;
                if (idxGoal >= goals.length) {
                    offsetDeg = Randomizer.rollDie(30) * 12;
                    idxGoal %= goals.length;
                }
            }
            toGoal = goals[idxGoal].rotateLeftDegrees(offsetDeg);
            if (Mover.tryMove(rc, toGoal, 30, 2)) {
                findExtents(rc);
                numTries = 0;
            } else {
                numTries++;
            }
        }

        RobotInfo[] robots = rc.senseNearbyRobots( -1, rc.getTeam().opponent());
        if (robots.length > 0) {
            Direction toTarget = rc.getLocation().directionTo(robots[0].location);
            boolean safeShot = Math.abs(toGoal.degreesBetween(toTarget)) > 30;
            if (safeShot && rc.canFireSingleShot()) {
                rc.fireSingleShot(toTarget);
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
}
