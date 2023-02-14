package assignment;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JTetrisWithHold extends JTetris {

    JTetrisWithHold() {

        super();
        registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                                       tick(Board.Action.HOLD);
                                   }
        },
        "hold", KeyStroke.getKeyStroke('r'), WHEN_IN_FOCUSED_WINDOW);
    }

    public static void main(String[] args) {
        createGUI(new JTetrisWithHold());
    }

}
