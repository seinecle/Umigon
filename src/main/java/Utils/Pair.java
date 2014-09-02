/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;


/**
 *
 * @author C. Levallois
 */
public class Pair<T,L> {

    private final T left;
    private final L right;

    public Pair(T left, L right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public L getRight() {
        return right;
    }

    @Override
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pair<T, L> other = (Pair<T, L>) obj;
        if (this.left != other.left && (this.left == null || !this.left.equals(other.left))) {
            return false;
        }
        if (this.right != other.right && (this.right == null || !this.right.equals(other.right))) {
            return false;
        }
        return true;
    }

    
}
