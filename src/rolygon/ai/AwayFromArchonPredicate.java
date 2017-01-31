package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import ddg.ai.Context;
import ddg.ai.Predicate;

/**
 * Created by nobody on 1/30/2017.
 */
public class AwayFromArchonPredicate implements Predicate {
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
        return true;
    }
}
