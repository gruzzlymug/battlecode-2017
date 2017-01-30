package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.ai.Key;
import ddg.util.Randomizer;

/**
 * Created by nobody on 1/12/2017.
 */
public class RandomMoveBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        Direction dir = Randomizer.getRandomDirection();
        RobotType type = rc.getType();
        boolean shouldSeekArchon = false;
        if (type == RobotType.SOLDIER) {
            shouldSeekArchon = (Randomizer.rollDie(8) == 3);
        } else if (type == RobotType.LUMBERJACK) {
            shouldSeekArchon = (Randomizer.rollDie(12) == 3);
        }
        if (shouldSeekArchon) {
            MapLocation[] enemyArchonLocations = (MapLocation[])context.recall(Key.ENEMY_ARCHON_LOCATIONS);
            int numArchons = enemyArchonLocations.length;
            int idxArchon = rc.getID() % numArchons;
            dir = rc.getLocation().directionTo(enemyArchonLocations[idxArchon]);
        }
        if (rc.canMove(dir) && !rc.hasMoved()) {
            rc.move(dir);
        }
        return RunResult.FINISHED;
    }
}
