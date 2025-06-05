package Project;

public class KnightSudoku {
    private static final int BOARD_SIZE = 120;
    private int[] board;
    private int lastMove;
    private int currentNumber;
    
    public KnightSudoku() {
        board = new int[BOARD_SIZE];
        lastMove = -1;
        currentNumber = 1;
    }
    
    private int getIndex(int row, int col) {
        return row * 15 + col;
    }
    
    private int[] getCoordinates(int index) {
        return new int[]{index / 15, index % 15};
    }
    
    private boolean isValidKnightMove(int prevIndex, int newIndex) {
        if (prevIndex == -1) return true;
        
        int[] prevPos = getCoordinates(prevIndex);
        int[] newPos = getCoordinates(newIndex);
        
        int rowDiff = Math.abs(newPos[0] - prevPos[0]);
        int colDiff = Math.abs(newPos[1] - prevPos[1]);
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
    
    private boolean isValidPosition(int index) {
        if (index < 0 || index >= BOARD_SIZE) return false;
        int[] pos = getCoordinates(index);
        return pos[0] >= 0 && pos[0] < 8 && pos[1] >= 0 && pos[1] < 8;
    }
    
    public boolean makeMove(int row, int col) {
        int index = getIndex(row, col);
        if (!isValidPosition(index)) return false;
        if (board[index] != 0) return false;
        if (!isValidKnightMove(lastMove, index)) return false;
        board[index] = currentNumber;
        lastMove = index;
        currentNumber++;
        
        return true;
    }
    public boolean isValidMove(int row, int col) {
        int index = getIndex(row, col);
        return isValidPosition(index) && board[index] == 0 && isValidKnightMove(lastMove, index);
    }
    
    public int[] undoMove() {
        if (currentNumber > 1) {
            board[lastMove] = 0;
            currentNumber--;
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (board[i] == currentNumber - 1) {
                    int[] pos = getCoordinates(i);
                    lastMove = i;
                    return pos;
                }
            }
            lastMove = -1;
        }
        return null;
    }
    
    public boolean isGameComplete() {
        return currentNumber > 64;
    }
    

    
    public int getCurrentNumber() {
        return currentNumber;
    }
    
    public int[] getBoard() {
        return board;
    }
    
}