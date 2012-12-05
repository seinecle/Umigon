package Utils;

public class NonComparablePair<L> {

    private final L left;
    private final L right;

    public NonComparablePair(L left, L right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
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
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof NonComparablePair)) {
            return false;
        }
        NonComparablePair pairo = (NonComparablePair) o;
        return (this.left.equals(pairo.getLeft())
                && this.right.equals(pairo.getRight()) | (this.left.equals(pairo.right)
                && this.right.equals(pairo.left)));
    }
}