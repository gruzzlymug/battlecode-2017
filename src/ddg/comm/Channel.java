package ddg.comm;

/**
 * Created by nobody on 1/28/2017.
 *
 * 10000 channels
 */
public class Channel {
    // for use when counting
    final public static int GARDENER_SUM = 20;
    final public static int LUMBERJACK_SUM = 21;
    final public static int SCOUT_SUM = 22;
    final public static int SOLDIER_SUM = 23;
    final public static int TANK_SUM = 24;
    // final values stored in these for use in logic
    final public static int GARDENER_COUNT = 30;
    final public static int LUMBERJACK_COUNT = 31;
    final public static int SCOUT_COUNT = 32;
    final public static int SOLDIER_COUNT = 33;
    final public static int TANK_COUNT = 34;

    final public static int MAP_EXT_CHANGED = 99;
    final public static int MAP_EXT_BOTTOM = 100;
    final public static int MAP_EXT_LEFT = 101;
    final public static int MAP_EXT_TOP = 102;
    final public static int MAP_EXT_RIGHT = 103;

    // x * 1000 + y
    final public static int ATTACK_TARGET = 200;

    // initial map will be 10x10 (channels 1000-1099)
    final public static int INFLUENCE_MAP = 1000;
    final public static int PROCESSED_IDS = 2000;
}
