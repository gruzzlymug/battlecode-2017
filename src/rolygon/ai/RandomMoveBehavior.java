package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.util.Randomizer;

/**
 * Created by nobody on 1/12/2017.
 */
public class RandomMoveBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        Direction dir = Randomizer.getRandomDirection();
        if (rc.getType() == RobotType.SOLDIER) {
            boolean shouldSeekArchon = (Randomizer.rollDie(8) == 3);
            if (shouldSeekArchon) {
                MapLocation[] enemyArchonLocations = (MapLocation[])context.recall("enemy_archon_locations");
                int numArchons = enemyArchonLocations.length;
                int idxArchon = rc.getID() % numArchons;
                dir = rc.getLocation().directionTo(enemyArchonLocations[idxArchon]);
            }
        }
        if (rc.canMove(dir) && !rc.hasMoved()) {
            rc.move(dir);
        }
        return RunResult.FINISHED;
    }
}
