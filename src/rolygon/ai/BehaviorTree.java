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
        Behavior behavior = evaluate(rc);
        if (behavior != null) {
            behavior.run(rc);
        }
    }

    private Behavior evaluate(RobotController rc) throws GameActionException {
        return root.evaluate(rc);
    }
}
