import java.util.*;

public class InferenceForwardChaining {
    
    public static Set<String> doForwardChaining(List<Rule> rules, Set<String> facts)
    {
        // Apply forward chaining with loop detection
        Set<String> inferredFacts = new HashSet<>();
        while (true) {
            boolean inferred = false;
            for (Rule rule : rules) {
                if (facts.containsAll(rule.getAntecedent()) && !inferredFacts.contains(rule.getConsequent())) {
                    facts.add(rule.getConsequent());
                    inferredFacts.add(rule.getConsequent());
                    inferred = true;
                    break;
                }
            }
            if (!inferred) {
                break;
            }
        }
        return inferredFacts;
    }
    
}