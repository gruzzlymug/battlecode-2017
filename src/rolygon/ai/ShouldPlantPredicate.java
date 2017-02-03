package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Context;
import ddg.ai.Predicate;
import ddg.comm.Channel;
import ddg.loco.Mover;

import java.util.Map;

/**
 * Created by nobody on 1/30/2017.
 */
public class ShouldPlantPredicate implements Predicate {
    final private static float minDistance = 7.0F;
    private boolean isAway;
    @Override
    public Predicate test(RobotController rc, Context context) throws GameActionException {
        MapLocation[] archonLocations = rc.getInitialArchonLocations(rc.getTeam());
        MapLocation robotLocation = rc.getLocation();
        // the further the game goes, the further these guys oughta go...
        float percentDone = rc.getRoundNum() / GameConstants.GAME_DEFAULT_ROUNDS;
        float handicap = percentDone * 0.25F;
        float scaledDistance = minDistance + handicap;
        isAway = true;
        if (archonLocations.length > 0) {
            MapLocation closest = archonLocations[0];
            for (MapLocation archonLocation : archonLocations) {
                float howFar = robotLocation.distanceTo(archonLocation);
                if (howFar < robotLocation.distanceTo(closest)) {
                    closest = archonLocation;
                }
                isAway = isAway && (howFar > scaledDistance);
            }
        }
        return this;
    }

    @Override
    public boolean isTrue() {
        return isAway;
    }

    @Override
    public boolean isFalse() {
        return !isAway;
    }
}
