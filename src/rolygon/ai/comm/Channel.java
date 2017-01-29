package rolygon.ai.comm;

/**
 * Created by nobody on 1/28/2017.
 *
 * 10000 channels
 */
public class Channel {
    final public static int GARDENER_SUM = 20;
    final public static int LUMBERJACK_SUM = 21;
    final public static int SCOUT_SUM = 22;
    final public static int SOLDIER_SUM = 23;

    final public static int MAP_EXT_CHANGED = 99;
    final public static int MAP_EXT_BOTTOM = 100;
    final public static int MAP_EXT_LEFT = 101;
    final public static int MAP_EXT_TOP = 102;
    final public static int MAP_EXT_RIGHT = 103;

    // initial map will be 10x10 (channels 1000-1099)
    final public static int INFLUENCE_MAP = 1000;
    final public static int PROCESSED_IDS = 2000;
}
