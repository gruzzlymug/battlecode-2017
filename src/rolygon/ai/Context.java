package rolygon.ai;

import java.util.HashMap;

/**
 * Created by nobody on 1/14/2017.
 */
public class Context {
    HashMap memory = new HashMap();

    void memorize(String key, Object value) {
        memory.put(key, value);
    }

    Object recall(String key) {
        return memory.get(key);
    }
}
