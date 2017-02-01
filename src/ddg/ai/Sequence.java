package ddg.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * A Sequence runs behaviors in series, one after the other, as
 * the previous ones completes.
 */
public class Sequence implements Node {
    public enum Mode {
        REPEAT_ALL,
        REPEAT_LAST,
        SKIP_AFTER_COMPLETE
    }

    // TODO what are optimal storage strategies in terms of bytecode use? count access operations.
    // TODO should these be formally grouped for reusabilty?
    Node[] nodes = new Node[4];
    int size;
    int currentNode;

    Mode mode;

    public Sequence(Mode mode) {
        this.mode = mode;
    }

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
            switch (mode) {
                case REPEAT_ALL:
                    currentNode = 0;
                    break;
                case REPEAT_LAST:
                    currentNode = size - 1;
                    break;
                case SKIP_AFTER_COMPLETE:
                    return RunResult.SKIPPED;
            }
        }

        RunResult result = nodes[currentNode].run(rc, context);
        if (result == RunResult.SKIPPED || result == RunResult.FINISHED) {
            currentNode++;
        }
        if (currentNode >= size) {
            return RunResult.FINISHED;
        }
        return RunResult.IN_PROGRESS;
    }
}
