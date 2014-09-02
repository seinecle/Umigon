package Heuristics;

import com.google.common.collect.Multimap;
import java.util.Set;
import javax.ejb.Stateless;

/**
 *
 * @author C. Levallois
 */
@Stateless
public class Heuristic {

    private String term;
    private Multimap<String, Set<String>> mapFeatures;
    private String rule;

    public Heuristic() {
    }
    
    public void generateNewHeuristic(String term, Multimap mapFeatures, String rule) {
        this.term = term;
        this.mapFeatures = mapFeatures;
        this.rule = rule;
    }
    

    public String getTerm() {
        return term;
    }

    public Multimap<String, Set<String>> getMapFeatures() {
        return mapFeatures;
    }

    public String getRule() {
        return rule;
    }
    
    
    
    

    @Override
    public String toString() {
        return "Heuristic{" + "term=" + term + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.term != null ? this.term.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Heuristic other = (Heuristic) obj;
        if ((this.term == null) ? (other.term != null) : !this.term.equals(other.term)) {
            return false;
        }
        return true;
    }

}
