package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JBrainTetrisModel extends JTetris {

    protected void setBoard(Board board) {
        this.board = board;
    }

    public void addNewPiece() {
    }

    JBrainTetrisModel() {
        super();
        setPreferredSize(new Dimension(WIDTH * PIXELS + 2, (HEIGHT + TOP_SPACE) * PIXELS + 2));
        gameOn = false;
        Rewarder.changeWeights(-2.76, -0.09, -0.25, 0.76);
        board = new TetrisBoard(WIDTH, HEIGHT + TOP_SPACE);
        TetrisModel tm = TetrisModel.initialize(30000);

        registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!gameOn) {
                    return;
                }
                for (int i = 0; i < 10; i++) {
                    count++;
                    countLabel.setText(Integer.toString(count));
                    board = tm.currentState.getTetrisBoard();
                    tm.getNextState();
                    tick(board);
                }
            }
        }, "drop", KeyStroke.getKeyStroke(' '), WHEN_IN_FOCUSED_WINDOW);

    }

    public static void main(String[] args) {
        createGUI(new JBrainTetrisModel());
    }

    public void tick(Board nextBoard) {
        if (!gameOn) {
            return;
        }

        if (nextBoard.getMaxHeight() > 20) {
            stopGame();
        }
        this.board = nextBoard;
        repaint();
    }

}
