package Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class KnightSudokuGUI extends JFrame {
    private static final int BOARD_SIZE = 8;
    private JButton[][] cells;
    private KnightSudoku game;
    private JLabel statusLabel;
    private Color defaultBackground;
    private static final Color HIGHLIGHT_COLOR = new Color(255, 255, 150); 
    public KnightSudokuGUI() {
        game = new KnightSudoku();
        cells = new JButton[BOARD_SIZE][BOARD_SIZE];
        
        setTitle("Độc Mã Sudoku - Chế độ người chơi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());        
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells[i][j] = new CustomButton();
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                cells[i][j].setPreferredSize(new Dimension(60, 60));
                
                final int row = i;
                final int col = j;
                
                cells[i][j].addActionListener(e -> handleCellClick(row, col));
                boardPanel.add(cells[i][j]);
                
                if ((i + j) % 2 == 0) {
                    cells[i][j].setBackground(Color.LIGHT_GRAY);
                } else {
                    cells[i][j].setBackground(Color.WHITE);
                }
            }
        }
        defaultBackground = cells[0][0].getBackground();
        JPanel controlPanel = new JPanel();
        JButton undoButton = new JButton("Hoàn tác");
        undoButton.addActionListener(e -> handleUndo());
        
        statusLabel = new JLabel("Số tiếp theo: 1");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        controlPanel.add(statusLabel);
        controlPanel.add(undoButton);
        
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private class CustomButton extends JButton {
        private boolean hasRedDot = false;
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (hasRedDot) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.RED);
                int size = 12;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                g2d.fillOval(x, y, size, size);
            }
        }
        
        public void setRedDot(boolean show) {
            hasRedDot = show;
            repaint();
        }
    }
    
    private void highlightPossibleMoves(int currentRow, int currentCol) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                cells[i][j].setBackground((i + j) % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                ((CustomButton)cells[i][j]).setRedDot(false);
            }
        }
        if (currentRow >= 0 && currentCol >= 0) {
            ((CustomButton)cells[currentRow][currentCol]).setRedDot(true);            
            int[] rowMoves = {-2, -2, -1, -1, 1, 1, 2, 2};
            int[] colMoves = {-1, 1, -2, 2, -2, 2, -1, 1};
            
            for (int i = 0; i < 8; i++) {
                int newRow = currentRow + rowMoves[i];
                int newCol = currentCol + colMoves[i];
                
                if (isValidPosition(newRow, newCol) && game.isValidMove(newRow, newCol)) {
                    cells[newRow][newCol].setBackground(HIGHLIGHT_COLOR);
                }
            }
        }
    }
    
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE;
    }
    
    private void handleCellClick(int row, int col) {
        if (game.makeMove(row, col)) {
            updateBoard();
            highlightPossibleMoves(row, col);
            if (game.isGameComplete()) {
                JOptionPane.showMessageDialog(this, "Chúc mừng! Bạn đã hoàn thành trò chơi!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nước đi không hợp lệ!");
        }
    }
    
    private void handleUndo() {
        int[] lastPosition = game.undoMove();
        if (lastPosition != null) {
            updateBoard();
            highlightPossibleMoves(lastPosition[0], lastPosition[1]);
        }
    }
    
    private void updateBoard() {
        int[] board = game.getBoard();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int index = i * 15 + j;
                int value = board[index];
                cells[i][j].setText(value > 0 ? String.valueOf(value) : "");
            }
        }
        statusLabel.setText("Số tiếp theo: " + game.getCurrentNumber());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new KnightSudokuGUI().setVisible(true);
        });
    }
}