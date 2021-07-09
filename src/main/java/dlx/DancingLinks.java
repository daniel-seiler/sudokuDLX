package dlx;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DancingLinks {
    private ColumnNode header;
    public List<Node> answer;
    public List<Node> result;
    
    /**
     * Create a new list, the dancing links. It is a way of saving a sparse matrix with nodes representing everything
     * that is not 0. This class can solve a given boolean matrix with brute force and return its result.
     *
     * @param matrix given binary matrix
     */
    public DancingLinks(final boolean[][] matrix) {
        this.header = toList(matrix);
        this.answer = new ArrayList<>();
        this.result = new ArrayList<>();
    }
    
    /**
     * Convert a binary matrix to a list of nodes.
     *
     * @param matrix    input matrix; should be a sparse one
     * @return          header node of the list
     */
    ColumnNode toList(final boolean[][] matrix) {
            ColumnNode header = new ColumnNode(-1);
        
            for (int i = 0; i < matrix[0].length; i++) {
                ColumnNode newColumn = new ColumnNode(i);
                header = (ColumnNode) header.insertRight(newColumn);
            }
        
            header = header.right.column;
        
            for (boolean[] row : matrix) {
                Node prevLeft = null;
                ColumnNode cursor = header;
                
                for (int j = 0; j < matrix[0].length; j++) {
                    cursor = (ColumnNode) cursor.right;
                    
                    if (row[j]) {
                        Node newNode = new Node(cursor);
                    
                        if (prevLeft == null) {
                            prevLeft = newNode;
                        }
                        
                        cursor.top.insertDown(newNode);
                        prevLeft = prevLeft.insertRight(newNode);
                        cursor.nodeAmount++;
                    }
                }
            }
        
            header.nodeAmount = matrix[0].length;
        
            return header;
    }
    
    /**
     * Execute the algorithm x. Recursively:
     *      - if the list is empty the algorithm has finished
     *      - choose a column and cover it
     *      - cover all rows that contain a node and are inline with nodes from the column
     *      - add the column to the temporary answer list
     *      - repeat
     *      - if there are no "1" but still columns left:
     *          - recover the previously removed column
     *          - remove it from the temporary answer list
     *
     * @param k     recursion depth
     */
    public void algorithmX(int k) {
        //System.out.println("Algorithm depth: " + k);
        if (this.header.right == this.header) {
            this.result = new LinkedList<>(this.answer);
        } else {
            ColumnNode columnNode = nextMinNode();
            columnNode.cover();
            
            for (Node rowNode = columnNode.bottom; rowNode != columnNode; rowNode = rowNode.bottom) {
                this.answer.add(rowNode);
                
                for (Node it = rowNode.right; it != rowNode; it = it.right) {
                    it.column.cover();
                }
                
                algorithmX(++k);
                
                rowNode = answer.remove(answer.size() - 1);
                columnNode = rowNode.column;
                
                for (Node it = rowNode.left; it != rowNode; it = it.left) {
                    it.column.discover();
                }
            }
            
            columnNode.discover();
        }
    }
    
    /**
     * Find the next node to be covered by the algorithm x. It is decided by the lowest node count, in some cases this
     * improves performance.
     *
     * @return      next node to be covered
     */
    private ColumnNode nextMinNode() {
        int minAmount = Integer.MAX_VALUE;
        ColumnNode returnNode = null;
        for (ColumnNode cursor = (ColumnNode) this.header.right; cursor != header; cursor = (ColumnNode) cursor.right) {
            if (minAmount > cursor.nodeAmount) {
                minAmount = cursor.nodeAmount;
                returnNode = cursor;
            }
        }
        return returnNode;
    }
}
