package rolygon.ai;

import battlecode.common.*;

/**
 * A Behavior Tree is ...
 * ... and this is how it works ...
 */
public class BehaviorTree {
    Node root;

    public BehaviorTree() {
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public void run(RobotController rc) throws GameActionException {
        root.run(rc);
    }
}
