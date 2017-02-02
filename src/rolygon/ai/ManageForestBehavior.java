package rolygon.ai;

import battlecode.common.*;
import ddg.ai.Behavior;
import ddg.ai.Context;
import ddg.util.Randomizer;

/**
 * Created by nobody on 1/16/2017.
 */
public class ManageForestBehavior implements Behavior {
    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        // plant a tree
        int roundNum = rc.getRoundNum();
        if ((roundNum > 79) && (roundNum % 40 == 0)) {
            System.out.println("want to plant...");
            int inc = 60;
            int numTries = (360 / inc) - 1;
            Direction dir = new Direction((float)Math.PI / -2.0F);
            for (int i = 0; i < numTries; i++) {
                if (rc.canPlantTree(dir)) {
                    System.out.println("...tree");
                    rc.plantTree(dir);
                    return RunResult.FINISHED;
                } else {
                    dir = dir.rotateLeftDegrees(inc);
                }
            }
        }
        // water nearby trees
        TreeInfo[] trees = rc.senseNearbyTrees(rc.getType().sensorRadius, rc.getTeam());
        TreeInfo[] threeWorst = selectDyingTrees(trees, 3);
        if (threeWorst.length > 0 && threeWorst[0].getHealth() > (GameConstants.BULLET_TREE_MAX_HEALTH - GameConstants.WATER_HEALTH_REGEN_RATE)) {
            return RunResult.SKIPPED;
        }

        // take the 3 worst and try to water one of them
        for (TreeInfo tree : threeWorst) {
            MapLocation treeLocation = tree.getLocation();
            Direction toTree = rc.getLocation().directionTo(treeLocation);
            if (rc.canMove(toTree) && !rc.hasMoved()) {
                rc.move(toTree);
            }
            if (rc.canWater(treeLocation)) {
                rc.water(treeLocation);
                return RunResult.FINISHED;
            }
        }
        return RunResult.SKIPPED;
    }

    // select K trees with the lowest health.
    // probably overkill, just water anything less than healthy.
    private static TreeInfo[] selectDyingTrees(TreeInfo[] trees, int k) {
        if (trees.length < k) {
            return trees;
        }

        for (int i = 0; i < k; i++) {
            int minIndex = i;
            float minValue = trees[i].getHealth();
            for (int j = i + 1; j < trees.length; j++) {
                float jh = trees[j].getHealth();
                if (jh < minValue) {
                    minIndex = j;
                    minValue = jh;
                    TreeInfo temp = trees[i];
                    trees[i] = trees[minIndex];
                    trees[minIndex] = temp;
                }
            }
        }
        return trees;
    }
}
