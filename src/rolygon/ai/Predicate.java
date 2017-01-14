package rolygon.ai;

import battlecode.common.*;

/**
 * Created by nobody on 1/11/2017.
 */
public interface Predicate {
    public Predicate test(RobotController rc, Context context) throws GameActionException;
    public boolean isTrue();
    public boolean isFalse();
}
