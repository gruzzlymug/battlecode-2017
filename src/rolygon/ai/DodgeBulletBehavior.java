package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;

public class DodgeBulletBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        BulletInfo bullet = (BulletInfo) context.recall("bullet");
        Direction towards = bullet.getDir();

        if (rc.canMove(towards.rotateRightDegrees(90))) {
            rc.move(towards.rotateRightDegrees(90));
        } else if (rc.canMove(towards.rotateLeftDegrees(90))) {
            rc.move(towards.rotateLeftDegrees(90));
        }

        return RunResult.FINISHED;
    }
}
