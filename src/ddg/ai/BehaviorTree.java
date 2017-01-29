package ddg.ai;

import battlecode.common.*;
import rolygon.ai.comm.Channel;

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
        // set up context
        int packedAttack = rc.readBroadcast(Channel.ATTACK_TARGET);
        if (packedAttack != 0) {
            int y = packedAttack % 1000;
            int x = (packedAttack - y) / 1000;
            MapLocation attackTarget = new MapLocation(x, y);
            context.memorize(Key.ATTACK_TARGET, attackTarget);
        } else {
            context.forget(Key.ATTACK_TARGET);
        }
        // run the behaviors
        root.run(rc, context);
    }
}
