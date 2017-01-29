package ddg.ai;

import java.util.HashMap;

/**
 * Created by nobody on 1/14/2017.
 */
public class Context {
    HashMap memory = new HashMap();

    public void memorize(String key, Object value) {
        memory.put(key, value);
    }

    public Object recall(String key) {
        Object thing = memory.get(key);
//        System.out.println("R: " + key + ": " + thing);
        return memory.get(key);
    }

    public void forget(String key) {
        memory.remove(key);
    }
}
