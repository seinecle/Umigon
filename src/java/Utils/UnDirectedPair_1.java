package Utils;

public class UnDirectedPair_1<L> {

    private final L left;
    private final L right;

    public UnDirectedPair_1(L left, L right) {
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
        if (!(o instanceof UnDirectedPair_1)) {
            return false;
        }
        UnDirectedPair_1 pairo = (UnDirectedPair_1) o;
        return (this.left.equals(pairo.getLeft())
                && this.right.equals(pairo.getRight()) | (this.left.equals(pairo.right)
                && this.right.equals(pairo.left)));
    }

}