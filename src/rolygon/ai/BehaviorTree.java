package rolygon.ai;

import battlecode.common.*;

/**
 * Created by nobody on 1/11/2017.
 */
public class BehaviorTree {
    // predicate is optional
    Predicate predicate;
    Behavior behavior;

    public BehaviorTree() {
    }

    public void addPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public void addBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    public void run(RobotController rc) throws GameActionException {
        // rely on short-circuit...
        if (predicate == null || predicate.test(rc).isTrue()) {
            behavior.run(rc);
        }
    }
}
