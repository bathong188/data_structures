public class Dfs2dArray {
    static int ROW = 0;
    static int COL = 0;

    public static int explore(int[][] grid, boolean[][] visited, int i, int j, int target, int count) {
        visited[i][j] = true;

        if (grid[i][j] == target)
            count++;
        else return count;

        // Check cell above
        if (i > 0 && visited[i - 1][j] == false) {
            count = explore(grid, visited, i - 1, j, target, count);
        }
        // Check cell below
        if (i < ROW - 1 && visited[i + 1][j] == false) {
            count = explore(grid, visited, i + 1, j, target, count);
        }
        // Check cell left
        if (j > 0 && visited[i][j - 1] == false) {
            count = explore(grid, visited, i, j - 1, target, count);
        }
        // Check cell right
        if (j < COL - 1 && visited[i][j + 1] == false) {
            count = explore(grid, visited, i, j + 1, target, count);
        }

        return count;
    }

    public static int DFS(int[][] grid, int targetRow, int targetCol) {
        int count = 0;
        int target = grid[targetRow][targetCol];
        boolean[][] visited = new boolean[ROW][COL];

        return explore(grid, visited, targetRow, targetCol, target, count);
    }

    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 3},
                {0, 9, 0},
                {0, 0, 7}
        };
        ROW = grid.length;
        COL = grid[0].length;

        System.out.println(DFS(grid, 1, 0));
    }
}
