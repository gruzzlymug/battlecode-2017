package rolygon;

import battlecode.common.*;
import rolygon.ai.*;

public strictfp class RobotPlayer {
    static RobotController rc;
    static double twoPi = 2 * Math.PI;

    static BehaviorTree scoutTree = new BehaviorTree();

    public static void run(RobotController rc) throws GameActionException {
        RobotPlayer.rc = rc;

        Predicate isInCorner = new IsInCornerPredicate();
        scoutTree.addPredicate(isInCorner);
        Behavior seekCorner = new SeekCornerBehavior();
        scoutTree.addBehavior(seekCorner);

        RobotType rtype = rc.getType();
        switch (rtype) {
            case ARCHON:
                runArchon();
                break;
            case GARDENER:
                runGardener();
                break;
            case LUMBERJACK:
                runLumberjack();
                break;
            case SCOUT:
                runScout();
                break;
            case SOLDIER:
                break;
            case TANK:
                break;
        }
    }

    private static void runArchon() throws GameActionException {
        while (true) {
            Direction dir = randomDirection();
            if (rc.canBuildRobot(RobotType.GARDENER, dir)) {
                rc.buildRobot(RobotType.GARDENER, dir);
            }
            Clock.yield();
        }
    }

    private static void runGardener() throws GameActionException  {
        while (true) {
            Direction dir = randomDirection();
            if (rc.canBuildRobot(RobotType.LUMBERJACK, dir)) {
                rc.buildRobot(RobotType.LUMBERJACK, dir);
            } else if (rc.canBuildRobot(RobotType.SCOUT, dir)) {
                rc.buildRobot(RobotType.SCOUT, dir);
            }
            Clock.yield();
        }
    }

    static void runLumberjack() throws GameActionException {
        while (true) {
            Clock.yield();
        }
    }

    static void runScout() throws GameActionException {
        while (true) {
            scoutTree.run(rc);
            Clock.yield();
        }
    }

    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }
}
