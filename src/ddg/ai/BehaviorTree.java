package ddg.ai;

import battlecode.common.*;

/**
 * A Behavior Tree is ...
 * ... and this is how it works ...
 */
public class BehaviorTree {
    Node root;
    Context context = new Context();

    public BehaviorTree(Node rootNode) {
        this.root = rootNode;
    }

    public void addMemory(String key, Object value) {
        context.memorize(key, value);
    }

    public void run(RobotController rc) throws GameActionException {
        root.run(rc, context);
    }
}
