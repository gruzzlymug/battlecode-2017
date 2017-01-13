package rolygon.ai;

import battlecode.common.*;

/**
 * A Behavior Tree is ...
 * ... and this is how it works ...
 */
public class BehaviorTree {
    Node root;

    public BehaviorTree(Node rootNode) {
        this.root = rootNode;
    }

    public void run(RobotController rc) throws GameActionException {
        root.run(rc);
    }
}
