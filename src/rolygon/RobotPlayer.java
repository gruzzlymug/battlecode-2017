package rolygon;

import battlecode.common.*;
import ddg.ai.*;
import rolygon.ai.*;
import rolygon.ai.comm.Channel;

public strictfp class RobotPlayer {
    static RobotController rc;
    static Team enemyTeam;

    // each robot should have a positional goal
    // each robot should have a functional goal
    // --attack target
    // --water tree
    // --etc
    // these need to work in concert, parallel and coordinated

    public static void run(RobotController rc) throws GameActionException {
        RobotPlayer.rc = rc;
        RobotPlayer.enemyTeam = rc.getTeam().opponent();

        RobotType rtype = rc.getType();
        switch (rtype) {
            case ARCHON:
                Node archonBehaviors = createArchonBehaviors(rc);
                runArchon(new BehaviorTree(archonBehaviors));
                break;
            case GARDENER:
                Node gardenerBehaviors = createGardenerBehaviors();
                runGardener(new BehaviorTree(gardenerBehaviors));
                break;
            case LUMBERJACK:
                Node lumberjackBehaviors = createLumberjackBehaviors();
                BehaviorTree lumberjackTree = new BehaviorTree(lumberjackBehaviors);
                runLumberjack(lumberjackTree);
                break;
            case SCOUT:
                Node scoutBehaviors = createScoutBehaviors();
                runScout(new BehaviorTree(scoutBehaviors));
                break;
            case SOLDIER:
                Node soldierBehaviors = createSoldierBehaviors();
                BehaviorTree soldierTree = new BehaviorTree(soldierBehaviors);
                MapLocation[] enemyArchonLocations = rc.getInitialArchonLocations(enemyTeam);
                soldierTree.addMemory(Key.ENEMY_ARCHON_LOCATIONS, enemyArchonLocations);
                runSoldier(soldierTree);
                break;
            case TANK:
                Node tankBehaviors = createTankBehaviors();
                runTank(new BehaviorTree(tankBehaviors));
                break;
        }
    }

    private static Node createArchonBehaviors(RobotController rc) throws GameActionException {
        PredicateSelector archonDodge = new PredicateSelector();
        archonDodge.addPredicate(new UnderFirePredicate());
        archonDodge.addNode(new DodgeBulletBehavior());

        PredicateSelector archonAvoid = new PredicateSelector();
        archonAvoid.addPredicate(new EnemyInRangePredicate());
        archonAvoid.addNode(new AvoidEnemyBehavior());

        PrioritySelector archonPriorities = new PrioritySelector();
        archonPriorities.addNode(new ArchonThinkBehavior(rc));
        archonPriorities.addNode(archonDodge);
        // TODO remove or fix before using. 644 rounds w/o it
        //archonPriorities.addNode(archonAvoid);
        archonPriorities.addNode(new BuildGardenersBehavior());
        archonPriorities.addNode(new RandomMoveBehavior());

        // perform standard think and then run priorities
//        Sequence archonSequence = new Sequence();
//        archonSequence.addNode(new ArchonThinkBehavior(rc));
//        archonSequence.addNode(archonPriorities);

        return archonPriorities;
    }

    private static Node createGardenerBehaviors() {
        RobotType[] buildOrder = {
            RobotType.SCOUT, RobotType.LUMBERJACK, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.LUMBERJACK, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.LUMBERJACK, RobotType.SOLDIER,
            RobotType.SOLDIER, RobotType.LUMBERJACK, RobotType.SOLDIER,
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

        PrioritySelector gardenerPriorities = new PrioritySelector();
        gardenerPriorities.addNode(gardenerDodge);
        gardenerPriorities.addNode(new ManageForestBehavior());
        BuildOrderBehavior builder = new BuildOrderBehavior();
        builder.setBuildOrder(buildOrder);
        gardenerPriorities.addNode(builder);
        gardenerPriorities.addNode(new RandomMoveBehavior());

        return gardenerPriorities;
    }

    private static Node createLumberjackBehaviors() {
        PrioritySelector lumberjackPriorities = new PrioritySelector();
        PredicateSelector meleeSelector = new PredicateSelector();
        meleeSelector.addPredicate(new EnemyInRangePredicate());
        meleeSelector.addNode(new MeleeAttackBehavior());
        lumberjackPriorities.addNode(meleeSelector);
        lumberjackPriorities.addNode(new ClearCutBehavior());
        lumberjackPriorities.addNode(new RandomMoveBehavior());
        return lumberjackPriorities;
    }

    private static Node createScoutBehaviors() {
        PredicateSelector scoutDodge = new PredicateSelector();
        scoutDodge.addPredicate(new UnderFirePredicate());
        scoutDodge.addNode(new DodgeBulletBehavior());

        Sequence movement = new Sequence();
        movement.addNode(new ScoutMapBehavior(rc));
        RandomMoveBehavior moveScout = new RandomMoveBehavior();
        movement.addNode(moveScout);

        PrioritySelector scoutPriorities = new PrioritySelector();
        scoutPriorities.addNode(new SenseEnvironmentBehavior());
        scoutPriorities.addNode(scoutDodge);
        scoutPriorities.addNode(movement);

        return scoutPriorities;
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
        soldierPriorities.addNode(new BugNavBehavior());
        soldierPriorities.addNode(new RandomMoveBehavior());

        return soldierPriorities;
    }

    private static Node createTankBehaviors() {
        PredicateSelector tankAttack = new PredicateSelector();
        tankAttack.addPredicate(new EnemyInRangePredicate());
        Sequence attackSequence = new Sequence();
        attackSequence.addNode(new RandomMoveBehavior());
        attackSequence.addNode(new RangedAttackBehavior());
        tankAttack.addNode(attackSequence);

        return tankAttack;
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
            rc.broadcast(Channel.GARDENER_SUM, 1+rc.readBroadcast(Channel.GARDENER_SUM));
            common(rc);
            gardenerTree.run(rc);
            Clock.yield();
        }
    }

    static void runLumberjack(BehaviorTree lumberjackTree) throws GameActionException {
        while (true) {
            rc.broadcast(Channel.LUMBERJACK_SUM, 1+rc.readBroadcast(Channel.LUMBERJACK_SUM));
            common(rc);
            lumberjackTree.run(rc);
            Clock.yield();
        }
    }

    static void runScout(BehaviorTree scoutTree) throws GameActionException {
        while (true) {
            rc.broadcast(Channel.SCOUT_SUM, 1+rc.readBroadcast(Channel.SCOUT_SUM));
            common(rc);
            scoutTree.run(rc);
            Clock.yield();
        }
    }

    static void runSoldier(BehaviorTree soldierTree) throws GameActionException {
        while (true) {
            rc.broadcast(Channel.SOLDIER_SUM, 1+rc.readBroadcast(Channel.SOLDIER_SUM));
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
