package rolygon.ai;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import ddg.ai.Behavior;
import ddg.ai.Context;

/**
 * Created by nobody on 1/14/2017.
 *
 * Note this is AvoidENEMY singular, just to keep it simple and save time.
 *
 * Using this might result in the enemy being able to herd and massacre friendly units
 *
 * Context Interface
 * in - "nearby_enemies" - array of enemy RobotInfo, avoid the first one
 */
public class AvoidEnemyBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        RobotInfo[] enemies = (RobotInfo[]) context.recall("nearby_enemies");
        if (enemies.length > 0) {
            Direction awayFromEnemy = enemies[0].getLocation().directionTo(rc.getLocation());
            if (rc.canMove(awayFromEnemy)) {
                rc.move(awayFromEnemy);
            }
            return RunResult.FINISHED;
        }
        return RunResult.SKIPPED;
    }
}
