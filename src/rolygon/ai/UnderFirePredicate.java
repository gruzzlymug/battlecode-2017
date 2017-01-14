package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * Created by nobody on 1/13/2017.
 */
public class UnderFirePredicate implements Predicate {
    @Override
    public Predicate test(RobotController rc, Context context) throws GameActionException {
        return this;
    }

    @Override
    public boolean isTrue() {
        return false;
    }

    @Override
    public boolean isFalse() {
        return false;
    }
}
