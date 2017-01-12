package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * Created by nobody on 1/11/2017.
 */
public class Sequence implements Node {
    Behavior[] behaviors = new Behavior[1];
    int size;

    public void addBehavior(Behavior behavior) {
        behaviors[size++] = behavior;
    }

    @Override
    public void run(RobotController rc) throws GameActionException {
        behaviors[0].run(rc);
    }
}
