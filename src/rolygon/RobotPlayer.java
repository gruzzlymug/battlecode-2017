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

                PredicateSelector archonAvoid = new PredicateSelector();
                archonAvoid.addPredicate(new EnemyInRangePredicate());
                archonAvoid.addNode(new AvoidEnemyBehavior());

                PrioritySelector archonPriorities = new PrioritySelector();
                archonPriorities.addNode(archonDodge);
                // TODO remove or fix before using. 644 rounds w/o it
                //archonPriorities.addNode(archonAvoid);
                archonPriorities.addNode(new ArchonBehavior());
                archonPriorities.addNode(new RandomMoveBehavior());

                BehaviorTree archonTree = new BehaviorTree(archonPriorities);
                runArchon(archonTree);
                break;
            case GARDENER:
                PredicateSelector gardenerDodge = new PredicateSelector();
                gardenerDodge.addPredicate(new UnderFirePredicate());
                gardenerDodge.addNode(new DodgeBulletBehavior());

                PredicateSelector gardenerAvoid = new PredicateSelector();
                gardenerAvoid.addPredicate(new EnemyInRangePredicate());
                gardenerAvoid.addNode(new AvoidEnemyBehavior());

                PrioritySelector gardenerPriorities = new PrioritySelector();
                gardenerPriorities.addNode(gardenerDodge);
                // TODO remove or fix before using. 644 rounds w/o it
                //gardenerPriorities.addNode(gardenerAvoid);
                gardenerPriorities.addNode(new BuildOrderBehavior());
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
            archonTree.run(rc);
            Clock.yield();
        }
    }

    private static void runGardener(BehaviorTree gardenerTree) throws GameActionException  {
        while (true) {
            common(rc);
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
}
