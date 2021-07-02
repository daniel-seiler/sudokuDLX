# Sudoku DLX
Who hasn't made a sudoku solver?  

This project is neither optimized, nor is it well written. It uses Algorithm X by Donald Knuth for solving exact cover problems with dancing links technique. Java was only elected because of a prerequisite for an assignment.

## How does it work?
### TL;DR
A matrix is created which contains all constraints and possibilities. Every column is a constraint, and if a row satisfies this constraint it will contain a 1, otherwise a 0. This results in a sparse matrix. An efficient way of handling such a matrix is by using double linked lists, where every node represents a 1 (dancing links).  

To solve the double linked list, we have to select a set of rows that contain exactly one 1 for every constraint.

### Constraints
1. Only 1 instance of a number can be in a row
2. Only 1 instance of a number can be in a column
3. Only 1 instance of a number can be in a block
4. There can be only one number in a cell

The rows represent every possible position for every number. Every row has four 1s, representing a position for this number satisfying the four constraints. A default cover matrix is created in the [sudoku class](/src/Sudoku.java). All entries which already exist in the given sudoku are removed from the cover matrix. The only thing left to do is to search for a solution that satisfies all constraints.  

### Dancing Links
Since we are going to use a backtracking algorithm, a lot of operations like removing rows and columns (and reversing) have to be performed. A better way of handling the sparse matrix is to convert it to a double linked list. This makes all operations a lot easier and faster.

### Algorithm X
This algorithm searches a solution for the given cover matrix, source: [Dancing Links](https://arxiv.org/abs/cs/0011047)  

> If *A* is empty, the problem is solved; terminate successfully.  
> Otherwise choose a column, *c* (deterministically).  
> Choose a row, *r*, such that *A*[*r*, *c*] = 1 (nondeterministically).  
> Include *r* in the partial solution.  
> For each *j* such that *A*[*r*, *j*] = 1,  
> &emsp;&emsp;delete column *j* from matrix *A*;  
> &emsp;&emsp;for each *i* such that *A*[*i*, *j*] = 1,  
> &emsp;&emsp;&emsp;&emsp;delete row *i* from matrix *A*.  
> Repeat this algorithm recursively on the reduced matrix *A*.  
