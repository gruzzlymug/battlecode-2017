package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * Created by nobody on 1/11/2017.
 * might want one of these that fires once a condition is hit and
 * doesn't stop until another behavior interrupts,
 * and another one that checks the condition every frame.
 */
public class PredicateSelector implements Selector {
    Predicate predicate;
    Behavior behavior;

    public void addPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public void addBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    @Override
    public void run(RobotController rc) throws GameActionException {
        try {
            if (predicate.test(rc).isFalse()) {
                // TODO turn these into return values
                // DIDN'T RUN
                return;
            }

            behavior.run(rc);

            // TODO consider (eventual) behavior run return value
            if (predicate.test(rc).isTrue()) {
                // COMPLETED
                System.out.println("completed");
                return;
            }
        } catch (GameActionException exception) {
            // FAILURE
            System.out.println("failed");
        }
    }
}
