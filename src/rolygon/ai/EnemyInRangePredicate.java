package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Context;
import ddg.ai.Key;
import ddg.ai.Predicate;
import rolygon.ai.env.InfluenceMap;

/**
 * Created by nobody on 1/13/2017.
 *
 * Context Interface
 * out - "nearby_enemies" - array of sensed enemy robots
 */
public class EnemyInRangePredicate implements Predicate {
    boolean foundEnemies;

    @Override
    public Predicate test(RobotController rc, Context context) throws GameActionException {
        foundEnemies = false;

        Team enemy = rc.getTeam().opponent();
        RobotInfo[] robots = rc.senseNearbyRobots(-1, enemy);
        foundEnemies = robots.length > 0;
        context.memorize(Key.NEARBY_ENEMIES, robots);

        InfluenceMap.setController(rc);
        InfluenceMap.addRobots(robots);

        return this;
    }

    @Override
    public boolean isTrue() {
        return foundEnemies;
    }

    @Override
    public boolean isFalse() {
        return !foundEnemies;
    }
}
