package ddg.util;

import battlecode.common.Direction;

/**
 * Created by nobody on 1/14/2017.
 */
public class RandomDirection {
    public static Direction getDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }
}
