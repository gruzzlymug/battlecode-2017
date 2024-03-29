package ddg.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import ddg.ai.Context;
import ddg.ai.Node;
import ddg.ai.Predicate;
import ddg.ai.Selector;

/**
 * Created by nobody on 1/11/2017.
 * might want one of these that fires once a condition is hit and
 * doesn't stop until another node interrupts,
 * and another one that checks the condition every frame.
 */
public class PredicateSelector implements Selector {
    Predicate predicate;
    Node node;

    public void addPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public void addNode(Node node) {
        this.node = node;
    }

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        try {
            if (predicate.test(rc, context).isFalse()) {
                return RunResult.SKIPPED;
            }

            node.run(rc, context);

            if (predicate.test(rc, context).isTrue()) {
                return RunResult.FINISHED;
            }
        } catch (GameActionException exception) {
            System.out.println("failed");
            return RunResult.FAILED;
        }
        return RunResult.IN_PROGRESS;
    }
}
