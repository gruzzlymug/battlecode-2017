package ddg.ai;

import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import ddg.ai.Context;
import ddg.ai.Node;
import ddg.ai.Selector;

public class PrioritySelector implements Selector {
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
        RunResult result = RunResult.SKIPPED;
        for (int idxNode = 0; idxNode < size && result == RunResult.SKIPPED; ++idxNode) {
            result = nodes[idxNode].run(rc, context);
        }
        return result;
    }
}
