package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Context;
import ddg.ai.Key;
import ddg.ai.Predicate;

/**
 * Created by nobody on 1/30/2017.
 */
public class ShouldPlantPredicate implements Predicate {
    final private static float minDistance = 7.0F;
    private boolean goodSpot;
    private boolean settled;
    @Override
    public Predicate test(RobotController rc, Context context) throws GameActionException {
        goodSpot = settled || (noNearbyTeammates(rc) && notTooCloseToWall(rc));
        if (goodSpot && !settled) {
            settled = true;
        }
        return this;
    }

    @Override
    public boolean isTrue() {
        return goodSpot;
    }

    @Override
    public boolean isFalse() {
        return !goodSpot;
    }

    private boolean noNearbyTeammates(RobotController rc) {
        boolean noOneToBother = true;
        float minDistance = 7;
        RobotInfo[] nearbyTeammates = rc.senseNearbyRobots(minDistance, rc.getTeam());
        for (RobotInfo robot : nearbyTeammates) {
            RobotType type = robot.getType();
            noOneToBother = noOneToBother && (type != RobotType.GARDENER && type != RobotType.ARCHON);
        }
        return noOneToBother;
    }

    private boolean notTooCloseToWall(RobotController rc) {
        return true;
    }
}
