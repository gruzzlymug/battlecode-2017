package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * Created by nobody on 1/11/2017.
 */
public class IsInCornerPredicate implements Predicate {
    boolean isInCorner;

    @Override
    public Predicate test(RobotController rc) throws GameActionException {
        isInCorner = false;

        int numNearbyWalls = 0;
        // TODO confirm: 0 = east, 1 = south, 2 = west, 3 = north
        for (int idxDir = 0; idxDir < 4 && numNearbyWalls < 2; idxDir++) {
            // TODO clean up
            double dir = (float)(idxDir * 0.25) * 2 * Math.PI;
            if (rc.onTheMap(rc.getLocation(), (float)dir)) {
                ++numNearbyWalls;
            }
        }
        isInCorner = (numNearbyWalls == 2);

        return this;
    }

    @Override
    public boolean isTrue() {
        return isInCorner;
    }

    @Override
    public boolean isFalse() {
        return !isInCorner;
    }
}
