package rolygon.ai;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.util.Randomizer;
import rolygon.ai.comm.Channel;

/**
 * Created by nobody on 1/14/2017.
 *
 * Create API to configure behavior.
 * Create piece-wise functions for builds. Allow simple scripting:
 * loops, conditionals (e.g. if enemy forces are X, build Y), etc.
 */
public class BuildOrderBehavior implements Behavior {
    RobotType[] buildOrder;
    int[] buildTarget;
    int currentRobot;

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        // keep track of where started so can bail if nothing needs building
        int startRobot = currentRobot;
        do {
            currentRobot %= buildOrder.length;
            RobotType nextRobot = buildOrder[currentRobot];
            int desired = buildTarget[currentRobot];
            int have = 0;
            switch (nextRobot) {
                case SCOUT:
                    have = rc.readBroadcast(Channel.SCOUT_COUNT);
                    break;
                case SOLDIER:
                    have = rc.readBroadcast(Channel.SOLDIER_COUNT);
                    break;
                case LUMBERJACK:
                    have = rc.readBroadcast(Channel.LUMBERJACK_COUNT);
                    break;
                case TANK:
                    have = rc.readBroadcast(Channel.TANK_COUNT);
                    break;
            }
            if (desired > have) {
                System.out.println("Have " + have + " " + nextRobot + "s, want " + desired);
                Direction dir = Randomizer.getRandomDirection();
                for (int i = 0; i < 12; i++) {
                    if (rc.canBuildRobot(nextRobot, dir)) {
                        rc.buildRobot(nextRobot, dir);
                        currentRobot++;
                        return RunResult.FINISHED;
                    }
                    dir = dir.rotateLeftDegrees(i * 30);
                }
                return RunResult.SKIPPED;
            } else {
                currentRobot++;
            }
        } while (currentRobot != startRobot);
        return RunResult.SKIPPED;
    }

    public void setBuildConfig(RobotType[] buildOrder, int[] buildTarget) {
        this.buildOrder = buildOrder;
        this.buildTarget = buildTarget;
    }
}
