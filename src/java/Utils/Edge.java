/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.Objects;


/**
 *
 * @author C. Levallois
 */
public class Edge {

    private String id;
    private String label;
    private String source;
    private String target;
    private boolean directed;
    private float weight;

    public Edge(String source, String target, boolean directed) {
        this.source = source;
        this.target = target;
        this.directed = directed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public boolean isDirected() {
        return directed;
    }

    public String isDirectedToString() {
        if (directed) {
            return "directed";
        } else {
            return "undirected";
        }
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.source);
        hash = 59 * hash + Objects.hashCode(this.target);
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
        final Edge other = (Edge) obj;
        if (directed) {
            if (!Objects.equals(this.source, other.source)) {
                return false;
            }
            if (!Objects.equals(this.target, other.target)) {
                return false;
            }
        } else {
            if (!Objects.equals(this.source, other.source) & !Objects.equals(this.source, other.target)) {
                return false;
            }
            if (!Objects.equals(this.target, other.target) & !Objects.equals(this.target, other.source)) {
                return false;
            }


        }
        return true;
    }
}
