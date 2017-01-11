package rolygon.ai;

/**
 * Created by nobody on 1/11/2017.
 * might want one of these that fires once a condition is hit and
 * doesn't stop until another behavior interrupts,
 * and another one that checks the condition every frame.
 */
public class PredicateSelector implements Selector {
    Predicate predicate;
    Behavior behavior;

    public void addPredicate(Predicate predicate) {
        this.predicate = predicate;
    }

    public void addBehavior(Behavior behavior) {
        this.behavior = behavior;
    }
}
