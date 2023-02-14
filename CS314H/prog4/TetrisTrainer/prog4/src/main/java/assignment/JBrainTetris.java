package assignment;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JBrainTetris extends JTetris {

    Brain brain;

    private final boolean doInfiniteMoves = true;
    private boolean isInfiniteModeOn = false;
    javax.swing.Timer currTimer;
    private final int msDelay = 1;

    JBrainTetris(Brain brain) {

        super();

        this.brain = brain;

        registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (doInfiniteMoves) {
                    isInfiniteModeOn = doInfiniteMoves;
                } else {
                    nextComputerMove();
                }
            }
        },
        "counterclockwise", KeyStroke.getKeyStroke(' '), WHEN_IN_FOCUSED_WINDOW);

        currTimer = new javax.swing.Timer(msDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isInfiniteModeOn) nextComputerMove();
            }
        });

        currTimer.start();

    }

    public void nextComputerMove() {
        Board.Action nextAction = brain.nextMove(board);
        if (nextAction == null) isInfiniteModeOn = false;
        else tick(nextAction);
    }

    public static void main(String[] args) {
        createGUI(new JBrainTetris(new SimpleBrain()));
    }

}
