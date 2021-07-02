import dlx.DancingLinks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class Sudoku {
    private final int EMPTY_CELL = 0;
    private final int CONSTRAINTS = 4;
    private final int MIN_VALUE = 1;
    private final int COVER_START_INDEX = 1;
    
    private int size;
    private int boxSize;
    
    /**
     * Take a sudoku, solve it and print it in terminal.
     *
     * @param grid      sudoku to be solved, size must be n*n
     */
    public void solve(int[][] grid) {
        this.size = grid.length;
        this.boxSize = (int) Math.sqrt(grid.length);
        
        System.out.println("Unsolved sudoku:");
        
        printGrid(grid);
        
        System.out.println("\nStarting to convert sudoku...");
        long startTime = System.nanoTime();
        DancingLinks dlx = new DancingLinks(convertInCoverMatrix(grid), this.size);
        long endTime = System.nanoTime();
        System.out.println("Finished converting after " + (endTime - startTime) / 1000000 + " milliseconds.\n");
        
        System.out.println("Starting to search for solution...");
        startTime = System.nanoTime();
        dlx.algorithmX(0);
        endTime = System.nanoTime();
        System.out.println("Solution found after \033[1;92m" + (endTime - startTime) / 1000 + "\033[0m microseconds.\n");
        
        System.out.println("Solved sudoku:");
        
        printGrid(dlx.toMatrix());
        
        System.out.println("\033[1;96m" + "=============================================\n" + "\033[0m");
    }
    
    /**
     * Solve a sudoku that is stored as file.
     *
     * @param path              path to the sudoku
     * @throws IOException      thrown if the file isn't found
     */
    public void solveFromFile(String path) throws IOException {
        String content = Files.readString(Path.of(path));
        Scanner s = new Scanner(content);
        int size = s.nextInt();
        int[][] sudoku = new int[size][size];
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                sudoku[row][column] = s.nextInt();
            }
        }
        solve(sudoku);
    }
    
    /**
     * Print one sudoku grid to the terminal.
     *
     * @param grid      sudoku to be printed
     */
    private void printGrid(int[][] grid) {
        final int SIZE = grid.length;
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) {
                    output.append("\033[41m").append(grid[i][j]).append("\033[0m");
                } else {
                    output.append(grid[i][j]);
                }
                output.append(" ");
            }
            output.append("\n");
        }
        
        System.out.println(output);
    }
    
    /**
     * Convert a sudoku to a cover matrix. First create the default cover matrix and then delete the already filled out
     * entries.
     *
     * @param grid      sudoku to be converted
     * @return          binary matrix to be solved
     */
    private boolean[][] convertInCoverMatrix(int[][] grid) {
        boolean[][] coverMatrix = createCoverMatrix();
        
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int column = COVER_START_INDEX; column <= size; column++) {
                int n = grid[row - 1][column - 1];
                
                if (n != EMPTY_CELL) {
                    for (int num = MIN_VALUE; num <= size; num++) {
                        if (num != n) {
                            Arrays.fill(coverMatrix[indexInCoverMatrix(row, column, num)], false);
                        }
                    }
                }
            }
        }
        
        return coverMatrix;
    }
    
    /**
     * Get the index inside the cover matrix for a given row, column and number.
     *
     * @param row       the row of a number
     * @param column    the column of a number
     * @param num       the number itself
     * @return          index for the cover matrix
     */
    private int indexInCoverMatrix(int row, int column, int num) {
        return (row - 1) * size * size + (column - 1) * size + (num - 1);
    }
    
    /**
     * Create a primitive cover matrix with 4 constraints:
     *      - one number in each cell
     *      - one of each number in all rows
     *      - one of each number in all columns
     *      - one of each number in all boxes
     *
     * @return      a default binary cover matrix
     */
    private boolean[][] createCoverMatrix() {
        boolean[][] coverMatrix = new boolean[size * size * size][size * size * CONSTRAINTS];
        
        int header = 0;
        header = createCellConstraints(coverMatrix, header);
        header = createRowConstraints(coverMatrix, header);
        header = createColumnConstraints(coverMatrix, header);
        createBoxConstraints(coverMatrix, header);
        
        return coverMatrix;
    }
    
    /**
     * Create the box constraints.
     *
     * @param matrix    the binary matrix to be expanded
     * @param header    the current column
     * @return          the current column after the constraints are added
     */
    private int createBoxConstraints(boolean[][] matrix, int header) {
        for (int row = COVER_START_INDEX; row <= size; row += boxSize) {
            for (int column = COVER_START_INDEX; column <= size; column += boxSize) {
                for (int n = COVER_START_INDEX; n <= size; n++, header++) {
                    for (int rowDelta = 0; rowDelta < boxSize; rowDelta++) {
                        for (int columnDelta = 0; columnDelta < boxSize; columnDelta++) {
                            int index = indexInCoverMatrix(row + rowDelta, column + columnDelta, n);
                            matrix[index][header] = true;
                        }
                    }
                }
            }
        }
        
        return header;
    }
    
    
    /**
     * Create the column constraints.
     *
     * @param matrix    the binary matrix to be expanded
     * @param header    the current column
     * @return          the current column after the constraints are added
     */
    private int createColumnConstraints(boolean[][] matrix, int header) {
        for (int column = COVER_START_INDEX; column <= size; column++) {
            for (int n = COVER_START_INDEX; n <= size; n++, header++) {
                for (int row = COVER_START_INDEX; row <= size; row++) {
                    int index = indexInCoverMatrix(row, column, n);
                    matrix[index][header] = true;
                }
            }
        }
        
        return header;
    }
    
    
    /**
     * Create the row constraints.
     *
     * @param matrix    the binary matrix to be expanded
     * @param header    the current column
     * @return          the current column after the constraints are added
     */
    private int createRowConstraints(boolean[][] matrix, int header) {
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int n = COVER_START_INDEX; n <= size; n++, header++) {
                for (int column = COVER_START_INDEX; column <= size; column++) {
                    int index = indexInCoverMatrix(row, column, n);
                    matrix[index][header] = true;
                }
            }
        }
        
        return header;
    }
    
    
    /**
     * Create the cell constraints.
     *
     * @param matrix    the binary matrix to be expanded
     * @param header    the current column
     * @return          the current column after the constraints are added
     */
    private int createCellConstraints(boolean[][] matrix, int header) {
        for (int row = COVER_START_INDEX; row <= size; row++) {
            for (int column = COVER_START_INDEX; column <= size; column++, header++) {
                for (int n = COVER_START_INDEX; n <= size; n++) {
                    int index = indexInCoverMatrix(row, column, n);
                    matrix[index][header] = true;
                }
            }
        }
        
        return header;
    }
}
