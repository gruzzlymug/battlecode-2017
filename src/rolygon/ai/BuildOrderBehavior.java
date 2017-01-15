package rolygon.ai;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import ddg.util.RandomDirection;

/**
 * Created by nobody on 1/14/2017.
 */
public class BuildOrderBehavior implements Behavior {
    RobotType[] buildOrder = new RobotType[]{
        RobotType.SCOUT, RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
    };
    int currentRobot;

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        if (currentRobot > buildOrder.length) {
            System.out.println("NOTHING LEFT TO BUILD");
            return RunResult.SKIPPED;
        }

        // TODO improve test and selection of direction
        Direction dir = RandomDirection.getDirection();
        if (rc.canBuildRobot(buildOrder[currentRobot], dir)) {
            rc.buildRobot(buildOrder[currentRobot], dir);
            currentRobot++;
            // TODO is it OK to return finished when build order not done 100%?
            return RunResult.FINISHED;
        }

        return RunResult.SKIPPED;
    }

    public void setBuildOrder(RobotType[] buildOrder) {
        this.buildOrder = buildOrder;
    }
}
