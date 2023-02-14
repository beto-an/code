package assignment;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//Extension of the JTetris class with hold functionality
public class JTetrisWithHold extends JTetris {


    //Simple constructor that adds hold functionality
    JTetrisWithHold() {

        super();

        //The user can press the 'r' key to hold the current piece
        registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                                       tick(Board.Action.HOLD);
                                   }
        },
        "hold", KeyStroke.getKeyStroke('r'), WHEN_IN_FOCUSED_WINDOW);

    }


    //Simple main method for running a Tetris game with hold functionality
    public static void main(String[] args) {
        createGUI(new JTetrisWithHold());
    }


}
