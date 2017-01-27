package rolygon.ai;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.util.Randomizer;

/**
 * Created by nobody on 1/14/2017.
 *
 * Create API to configure behavior.
 * Create piece-wise functions for builds. Allow simple scripting:
 * loops, conditionals (e.g. if enemy forces are X, build Y), etc.
 */
public class BuildOrderBehavior implements Behavior {
    RobotType[] buildOrder;
    int currentRobot;

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        if (currentRobot >= buildOrder.length) {
            return RunResult.SKIPPED;
        }
        // TODO improve test and selection of direction
        Direction dir = Randomizer.getRandomDirection();
        RobotType nextRobot = buildOrder[currentRobot];
        if (nextRobot == null) {
            // plant a tree
            for (int i = 0; i < 20; i++) {
                if (rc.canPlantTree(dir)) {
                    rc.plantTree(dir);
                    currentRobot++;
                    return RunResult.FINISHED;
                } else {
                    dir = dir.rotateLeftDegrees(10);
                }
            }
        } else if (rc.canBuildRobot(nextRobot, dir)) {
            rc.buildRobot(nextRobot, dir);
            currentRobot++;
            return RunResult.FINISHED;
        }
        return RunResult.SKIPPED;
    }

    public void setBuildOrder(RobotType[] buildOrder) {
        this.buildOrder = buildOrder;
    }
}
