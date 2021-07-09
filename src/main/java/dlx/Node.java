package dlx;

public class Node {
    public Node top;
    public Node bottom;
    public Node left;
    public Node right;
    public ColumnNode column;
    
    /**
     * Create a new node that is double linked in each direction and to the column node. It represents a "1" in a sparse
     * matrix.
     */
    Node() {
        this.top = this.bottom = this.left = this.right = this;
    }
    
    /**
     * Create a new node that is double linked in each direction and to the column node. It represents a "1" in a sparse
     * matrix.
     *
     * @param column    the column node this node is assigned to
     */
    Node(ColumnNode column) {
        this();
        this.column = column;
    }
    
    /**
     * Insert a new node below this one by rearranging this and the node below this one.
     *
     * @param bottom    new node to be inserted
     * @return          node that was inserted
     */
    Node insertDown(Node bottom) {
        bottom.bottom = this.bottom;
        bottom.bottom.top = bottom;
        bottom.top = this;
        this.bottom = bottom;
        return bottom;
    }
    
    /**
     * Insert a new node on the right side besides this one by rearranging this and the right node.
     *
     * @param right     new node to be inserted
     * @return          node that was inserted
     */
    Node insertRight(Node right) {
        right.right = this.right;
        right.right.left = right;
        right.left = this;
        this.right = right;
        return right;
    }
    
    /**
     * Make this node invisible to the neighbouring nodes on the left and right by linking over this node.
     */
    void coverLeftRight() {
        this.left.right = this.right;
        this.right.left = this.left;
    }
    
    /**
     * Make this node visible again to its neighbours on the left and right. Since the neighbours are still saved in
     * this node (while this node isn't visible for its previous neighbours) they can be reapplied.
     */
    void discoverLeftRight() {
        this.left.right = this;
        this.right.left = this;
    }
    
    /**
     * Make this node invisible to the neighbouring nodes on the top and bottom by linking over this node.
     */
    void coverTopBottom() {
        this.top.bottom = this.bottom;
        this.bottom.top = this.top;
    }
    
    /**
     * Make this node visible again to its neighbours on the top and bottom. Since the neighbours are still saved in
     * this node (while this node isn't visible for its previous neighbours) they can be reapplied.
     */
    void discoverTopBottom() {
        this.top.bottom = this;
        this.bottom.top = this;
    }
}
