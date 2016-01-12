import javax.swing.*;
import java.io.IOException;


public class CrosswordReader {
    public static void main(String[] args) throws IOException {
        final JFrame frame = new JFrame("Crossword");
        frame.setContentPane(new MainPanel());
        frame.setBounds(100, 100, 900, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
