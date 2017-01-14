package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * A Sequence runs behaviors in series, one after the other, as
 * the previous ones completes.
 */
public class Sequence implements Node {
    // TODO what are optimal storage strategies in terms of bytecode use? count access operations.
    // TODO should these be formally grouped for reusabilty?
    Node[] nodes = new Node[4];
    int size;
    int currentNode;

    public void addNode(Node node) {
        if (size >= nodes.length) {
            System.out.println("WARNING: Attempted to add too many nodes");
            return;
        }
        nodes[size++] = node;
    }

    @Override
    public RunResult run(RobotController rc, Context context) throws GameActionException {
        if (currentNode >= size) {
            return RunResult.SKIPPED;
        }
        RunResult result = nodes[currentNode].run(rc, context);
        if (result == RunResult.SKIPPED || result == RunResult.FINISHED) {
            currentNode++;
        }
        if (currentNode >= size) {
            currentNode = 0;
            return RunResult.FINISHED;
        }
        return RunResult.IN_PROGRESS;
    }
}
