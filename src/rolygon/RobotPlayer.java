package rolygon;

import battlecode.common.*;
import rolygon.ai.*;

public strictfp class RobotPlayer {
    static RobotController rc;
    static double twoPi = 2 * Math.PI;

    // each robot should have a positional goal
    // each robot should have a functional goal
    // --attack target
    // --water tree
    // --etc
    // these need to work in concert, parallel and coordinated

    public static void run(RobotController rc) throws GameActionException {
        RobotPlayer.rc = rc;

        RobotType rtype = rc.getType();
        switch (rtype) {
            case ARCHON:
                RandomMoveBehavior moveArchon = new RandomMoveBehavior();
                BehaviorTree archonTree = new BehaviorTree(moveArchon);
                runArchon(archonTree);
                break;
            case GARDENER:
                RandomMoveBehavior moveGardener = new RandomMoveBehavior();
                BehaviorTree gardenerTree = new BehaviorTree(moveGardener);
                runGardener(gardenerTree);
                break;
            case LUMBERJACK:
                runLumberjack();
                break;
            case SCOUT:
                Sequence movement = new Sequence();
                Behavior seekCorner = new SeekCornerBehavior();
                movement.addNode(seekCorner);
                RandomMoveBehavior moveScout = new RandomMoveBehavior();
                movement.addNode(moveScout);
                BehaviorTree scoutTree = new BehaviorTree(movement);
                runScout(scoutTree);
                break;
            case SOLDIER:
                break;
            case TANK:
                break;
        }
    }

    private static void runArchon(BehaviorTree archonTree) throws GameActionException {
        while (true) {
            common(rc);
            Direction dir = randomDirection();
            if (rc.canBuildRobot(RobotType.GARDENER, dir)) {
                rc.buildRobot(RobotType.GARDENER, dir);
            }
            archonTree.run(rc);
            Clock.yield();
        }
    }

    private static void runGardener(BehaviorTree gardenerTree) throws GameActionException  {
        while (true) {
            common(rc);
            Direction dir = randomDirection();
//            if (rc.canBuildRobot(RobotType.LUMBERJACK, dir)) {
//                rc.buildRobot(RobotType.LUMBERJACK, dir);
//            } else
            if (rc.canBuildRobot(RobotType.SCOUT, dir)) {
                rc.buildRobot(RobotType.SCOUT, dir);
            }

            gardenerTree.run(rc);
            Clock.yield();
        }
    }

    static void runLumberjack() throws GameActionException {
        while (true) {
            common(rc);
            Clock.yield();
        }
    }

    static void runScout(BehaviorTree scoutTree) throws GameActionException {
        while (true) {
            common(rc);
            scoutTree.run(rc);
            Clock.yield();
        }
    }

    static void common(RobotController rc) {
        if (rc.getRoundNum() > 250) {
            rc.disintegrate();
        }
    }

    // deprecated
    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }
}
