package ddg.util;

import battlecode.common.Direction;

/**
 * Created by nobody on 1/14/2017.
 */
public class Randomizer {
    public static Direction getRandomDirection() {
        return new Direction((float)Math.random() * 2 * (float)Math.PI);
    }

    public static int rollDie(int sides) {
        return (int)(Math.random() * sides + 1);
    }
}
