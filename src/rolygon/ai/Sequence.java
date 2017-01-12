package rolygon.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;

/**
 * A Sequence runs behaviors in series, one after the other, as
 * the previous ones completes.
 */
public class Sequence implements Node {
    // TODO what are optimal storage strategies in terms of bytecode use? count access operations.
    Node[] nodes = new Node[1];
    int size;
    int currentBehavior;

    public void addBehavior(Node behavior) {
        if (size >= nodes.length) {
            return;
        }
        nodes[size++] = behavior;
    }

    @Override
    public void run(RobotController rc) throws GameActionException {
        nodes[currentBehavior].run(rc);
    }
}
