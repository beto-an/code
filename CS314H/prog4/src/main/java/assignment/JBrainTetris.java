package assignment;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//Class for running a GUI of Tetris for the brain to play
public class JBrainTetris extends JTetris {


    //Store the brain being used
    private Brain brain;

    //Flag representing if the AI should play automatically

    private final boolean doInfiniteMoves = true;

    //Flag representing if the AI is currently playing automatically
    private boolean isInfiniteModeOn = false;

    //Timer for the AI to make moves, if playing automatically
    private javax.swing.Timer currTimer;

    //Delay for the timer
    private final int msDelay = 1;


    //Constructor for the Tetris game played by AI
    JBrainTetris(Brain brain) {

        super();
        this.brain = brain;

        //The user can press the 'r' key to hold the current piece
        registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                                       tick(Board.Action.HOLD);
                                   }
        },
        "hold", KeyStroke.getKeyStroke('r'), WHEN_IN_FOCUSED_WINDOW);

        //Add a new action: pressing space will trigger the AI to move, either once or continuously based on above flags
        registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (doInfiniteMoves) {
                    //If the infinite moves flag is true, then tell the AI to continuously play moves
                    isInfiniteModeOn = doInfiniteMoves;
                } else {
                    //If no infinite moves, then play one move
                    nextComputerMove();
                }
            }
        },
        "counterclockwise", KeyStroke.getKeyStroke(' '), WHEN_IN_FOCUSED_WINDOW);

        //Timer on repeat that tells AI to perform a move if the infinite move mode is on
        currTimer = new javax.swing.Timer(msDelay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isInfiniteModeOn) nextComputerMove();
            }
        });

        currTimer.start();

    }


    //Method for getting the next move from the AI
    public void nextComputerMove() {

        Board.Action nextAction = brain.nextMove(board);

        //If the action is null, then the game is over, so turn off continuous moves
        if (nextAction == null) isInfiniteModeOn = false;
        else tick(nextAction);

    }


    //Main method for running the Tetris game
    public static void main(String[] args) {
        createGUI(new JBrainTetris(new SimpleBrain()));
    }


}
