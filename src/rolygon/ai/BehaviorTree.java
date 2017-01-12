package rolygon.ai;

import battlecode.common.*;

/**
 * Created by nobody on 1/11/2017.
 */
public class BehaviorTree {
    // predicate is optional
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
