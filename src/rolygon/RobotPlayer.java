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
                PredicateSelector archonDodge = new PredicateSelector();
                archonDodge.addPredicate(new UnderFirePredicate());
                archonDodge.addNode(new DodgeBulletBehavior());

                PrioritySelector archonPriorities = new PrioritySelector();
                archonPriorities.addNode(archonDodge);
                archonPriorities.addNode(new RandomMoveBehavior());

                BehaviorTree archonTree = new BehaviorTree(archonPriorities);
                runArchon(archonTree);
                break;
            case GARDENER:
                PredicateSelector gardenerDodge = new PredicateSelector();
                gardenerDodge.addPredicate(new UnderFirePredicate());
                gardenerDodge.addNode(new DodgeBulletBehavior());

                PrioritySelector gardenerPriorities = new PrioritySelector();
                gardenerPriorities.addNode(gardenerDodge);
                gardenerPriorities.addNode(new RandomMoveBehavior());

                BehaviorTree gardenerTree = new BehaviorTree(gardenerPriorities);
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
                PredicateSelector soldierDodge = new PredicateSelector();
                soldierDodge.addPredicate(new UnderFirePredicate());
                soldierDodge.addNode(new DodgeBulletBehavior());

                PredicateSelector soldierAttack = new PredicateSelector();
                soldierAttack.addPredicate(new EnemyInRangePredicate());
                Sequence attackSequence = new Sequence();
                attackSequence.addNode(new RandomMoveBehavior());
                attackSequence.addNode(new RangedAttackBehavior());
                soldierAttack.addNode(attackSequence);

                PrioritySelector soldierPriorities = new PrioritySelector();
                soldierPriorities.addNode(soldierDodge);
                soldierPriorities.addNode(soldierAttack);
                soldierPriorities.addNode(new RandomMoveBehavior());

                BehaviorTree soldierTree = new BehaviorTree(soldierPriorities);
                runSoldier(soldierTree);
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
            if (rc.canBuildRobot(RobotType.SOLDIER, dir)) {
                rc.buildRobot(RobotType.SOLDIER, dir);
            }
//            else
//            if (rc.canBuildRobot(RobotType.SCOUT, dir)) {
//                rc.buildRobot(RobotType.SCOUT, dir);
//            }

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

    static void runSoldier(BehaviorTree soldierTree) throws GameActionException {
        while (true) {
            common(rc);
            soldierTree.run(rc);
            Clock.yield();
        }
    }

    static void runTank(BehaviorTree tankTree) throws GameActionException {
        while (true) {
            common(rc);
            tankTree.run(rc);
            Clock.yield();
        }
    }

    static void common(RobotController rc) {
        //if (rc.getRoundNum() > 250) {
        //    rc.disintegrate();
        //}
    }

    // deprecated
    static Direction randomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }
}
