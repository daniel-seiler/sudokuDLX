package dlx;

public class ColumnNode extends Node {
    private final int name;
    int nodeAmount;
    
    /**
     * Create a specific node for usage as column head.
     *
     * @param name  an integer to identify the column afterwards
     */
    ColumnNode(final int name) {
        super();
        this.name = name;
        this.column = this;
        nodeAmount = 0;
    }
    
    /**
     * Cover the complete column.
     */
    void cover() {
        coverLeftRight();
        for (Node i = this.bottom; i != this; i = i.bottom) {
            for (Node j = i.right; j != i; j = j.right) {
                j.coverTopBottom();
                j.column.nodeAmount--;
            }
        }
    }
    
    /**
     * Discover the complete column.
     */
    void discover() {
        for (Node i = this.bottom; i != this; i = i.bottom) {
            for (Node j = i.right; j != i; j = j.right) {
                j.discoverTopBottom();
                j.column.nodeAmount++;
            }
        }
        discoverLeftRight();
    }
    
    /**
     * Return the previously set name.
     *
     * @return      an integer presentation of this column
     */
    public int getName() {
        return this.name;
    }
}
