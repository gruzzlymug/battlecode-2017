package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * A Sequence runs behaviors in series, one after the other, as
 * the previous ones completes.
 */
public class Sequence implements Node {
    // TODO what are optimal storage strategies in terms of bytecode use? count access operations.
    Node[] nodes = new Node[4];
    int size;
    int currentBehavior;

    public void addNode(Node behavior) {
        if (size >= nodes.length) {
            System.out.println("WARNING: Attempted to add too many nodes");
            return;
        }
        nodes[size++] = behavior;
    }

    @Override
    public RunResult run(RobotController rc) throws GameActionException {
        if (currentBehavior >= size) {
            return RunResult.SKIPPED;
        }
        RunResult result = nodes[currentBehavior].run(rc);
        if (result == RunResult.SKIPPED || result == RunResult.FINISHED) {
            currentBehavior++;
        }
        if (currentBehavior >= size) {
            return RunResult.FINISHED;
        }
        return RunResult.IN_PROGRESS;
    }
}
