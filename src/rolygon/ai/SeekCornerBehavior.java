package rolygon.ai;

import battlecode.common.*;

/**
 * Created by nobody on 1/11/2017.
 */
public class SeekCornerBehavior implements Behavior {
    @Override
    public void run(RobotController rc) throws GameActionException {
        if (rc.onTheMap(rc.getLocation(), 1)) {
            // completely on the map
        } else {
            // must be partially off the map
        }
    }
}
