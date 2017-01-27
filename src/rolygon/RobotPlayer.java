package rolygon;

import battlecode.common.*;
import ddg.ai.*;
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
                Node archonBehaviors = createArchonBehaviors();
                runArchon(new BehaviorTree(archonBehaviors));
                break;
            case GARDENER:
                Node gardenerBehaviors = createGardenerBehaviors();
                runGardener(new BehaviorTree(gardenerBehaviors));
                break;
            case LUMBERJACK:
                BehaviorTree lumberjackTree = new BehaviorTree(new ClearCutBehavior());
                runLumberjack(lumberjackTree);
                break;
            case SCOUT:
                Node scoutBehaviors = createScoutBehaviors();
                runScout(new BehaviorTree(scoutBehaviors));
                break;
            case SOLDIER:
                Node soldierBehaviors = createSoldierBehaviors();
                runSoldier(new BehaviorTree(soldierBehaviors));
                break;
            case TANK:
                break;
        }
    }

    private static Node createArchonBehaviors() {
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
        archonPriorities.addNode(new BuildGardenersBehavior());
        archonPriorities.addNode(new RandomMoveBehavior());

        // perform standard think and then run priorities
        Sequence archonSequence = new Sequence();
        archonSequence.addNode(new ArchonThinkBehavior());
        archonSequence.addNode(archonPriorities);

        return archonSequence;
    }

    private static Node createGardenerBehaviors() {
        RobotType[] buildOrder = {
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.SOLDIER, RobotType.SOLDIER,
        };

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
        gardenerPriorities.addNode(new ManageForestBehavior());
        BuildOrderBehavior builder = new BuildOrderBehavior();
        builder.setBuildOrder(buildOrder);
        gardenerPriorities.addNode(builder);
        gardenerPriorities.addNode(new RandomMoveBehavior());

        return gardenerPriorities;
    }

    private static Node createScoutBehaviors() {
        Sequence movement = new Sequence();
        Behavior seekCorner = new SeekCornerBehavior();
        movement.addNode(seekCorner);
        RandomMoveBehavior moveScout = new RandomMoveBehavior();
        movement.addNode(moveScout);
        return movement;
    }

    private static Node createSoldierBehaviors() {
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

        return soldierPriorities;
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

    static void runLumberjack(BehaviorTree lumberjackTree) throws GameActionException {
        while (true) {
            common(rc);
            lumberjackTree.run(rc);
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
//        if (rc.getRoundNum() > 250) {
//            rc.disintegrate();
//        }
    }
}
