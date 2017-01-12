package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * Created by nobody on 1/11/2017.
 */
public class IsInCornerPredicate implements Predicate {
    @Override
    public Predicate test(RobotController rc) throws GameActionException {
        return this;
    }

    @Override
    public boolean isTrue() {
        return false;
    }

    @Override
    public boolean isFalse() {
        return true;
    }
}
