package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;

/**
 * Created by nobody on 1/13/2017.
 */
public class RangedAttackBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        RobotInfo[] nearbyEnemies = (RobotInfo[]) context.recall("nearby_enemies");
        //System.out.println(nearbyEnemies.length + " nearby enemies!");
        if (nearbyEnemies.length > 0) {
            MapLocation here = rc.getLocation();
            MapLocation goal = nearbyEnemies[0].getLocation();
            Direction toGoal = here.directionTo(goal);
            float howFar = here.distanceTo(goal);
            if (howFar > 9) {
                if (rc.canMove(toGoal)) {
                    rc.move(toGoal);
                }
            } else {
                if (rc.canMove(toGoal.opposite())) {
                    toGoal.opposite();
                }
            }
//            if (rc.canFireTriadShot()) {
//                // ...Then fire a bullet in the direction of the enemy.
//                rc.fireTriadShot(toGoal);
//            }
        }
        // And we have enough bullets, and haven't attacked yet this turn...
        if (rc.canFireSingleShot()) {
            // ...Then fire a bullet in the direction of the enemy.
            rc.fireSingleShot(rc.getLocation().directionTo(nearbyEnemies[0].location));
        }
        return RunResult.FINISHED;
    }
}
