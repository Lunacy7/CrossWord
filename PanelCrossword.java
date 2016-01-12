import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.*;
import java.util.List;

public class PanelCrossword extends JPanel {

    private static final Color SELECTED_WORD = new Color(0x76FF03);
    private static final Color BACKGROUND_WORD = Color.WHITE;
    private static final Color SELECTED_CELL = new Color(0xFF7043);
    private static final Color ANSWEARED_WORD = new Color(0x2962FF);


    private OnSelectionChangeListener listener;
    private JButton[][] grid;
    private char crw[][];
    private Crossword crossword;
    private final Border mBorder = BorderFactory.createLineBorder(Color.BLACK);

    private IWord previousWord;
    private IWord currentWord;
    private int selectedX;
    private int selectedY;

    public PanelCrossword(Crossword crossword) {
        this.crossword = crossword;

        final int cols = crossword.getWidth();
        final int rows = crossword.getHeight();
        crw = new char[rows][cols];
        setLayout(new GridLayout(rows, cols));
        grid = new JButton[rows][cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                grid[y][x] = new JButton();
                grid[y][x].setBorder(mBorder);
                grid[y][x].setEnabled(false);

                grid[y][x].setBackground(Color.BLACK);
                MyActionListener listener = new MyActionListener(y, x);
                grid[y][x].addActionListener(listener);
                grid[y][x].addKeyListener(listener);
                add(grid[y][x]);

            }
        }

        List<Word> wordList = crossword.getWords();
        for (int i = 0; i < wordList.size(); i++) {
            Word word = wordList.get(i);

            List<JButton> currentButtons = getButtonsIWord(word);
            for (int j = 0; j < currentButtons.size(); j++) {
                JButton button = currentButtons.get(j);
//                button.setText(charToString(word.getAnswer(), j));
                button.setEnabled(true);
                button.setBackground(BACKGROUND_WORD);
            }
        }
    }

    public void setListener(OnSelectionChangeListener listener) {
        this.listener = listener;
    }

    private void typeChar(int x, int y, KeyEvent ke) {
        // Set the current yellow cell's value
        final boolean isBackspace = ke.getKeyChar() == KeyEvent.VK_BACK_SPACE;
        grid[y][x].setText(ke.getKeyChar() + "");
        // Move to other cell
        boolean moved = false;
        int step = 1;
        if (isBackspace) {
            step = -1;
        }
        if (currentWord.getDirection() == IWord.HORIZONTAL) {
            if (CrosswordUtils.isPartOfWord(x + step, y, currentWord)) {
                grid[y][x + step].setBackground(SELECTED_CELL);
                grid[y][x + step].requestFocus();
                moved = true;
            }
        } else if (currentWord.getDirection() == IWord.VERTICAL) {
            if (CrosswordUtils.isPartOfWord(x, y + step, currentWord)) {
                grid[y + step][x].setBackground(SELECTED_CELL);
                grid[y + step][x].requestFocus();
                moved = true;
            }
        }
        if (!moved) {
            grid[y][x].setBackground(SELECTED_CELL);
        } else {
            grid[y][x].setBackground(SELECTED_WORD);
        }
    }

    public void verify() {
        for (Word word : crossword.getWords()) {
            List<JButton> buttons = getButtonsIWord(word);
            boolean isCurrect = true;
            for (int i = 0; i < word.getAnswer().length(); i++) {
                if (buttons.get(i).getText().isEmpty()) {
                    isCurrect = false;
                } else if (word.getAnswer().charAt(i) != buttons.get(i).getText().charAt(0)) {
                    isCurrect = false;
                }
            }
            if (isCurrect) {
                for (JButton button : buttons) {
                    button.setBackground(ANSWEARED_WORD);
                    button.setEnabled(false);
                }
            }
        }
    }

    private void selectedOne(int x, int y) {
        List<? extends IWord> words = crossword.getWordsAt(x, y);
        if (words.size() == 0) {
            return;
        }

        int index;
        int selectedIndex = words.indexOf(previousWord); // -1
        if ((selectedX == x) && (selectedY == y)) {
            index = (selectedIndex + 1) % words.size();
        } else {
            index = selectedIndex;
        }
        if (previousWord != null && words.size() == 2 && index == -1) {
            if (previousWord.getDirection() == IWord.HORIZONTAL) {
                index = 0;
            } else {
                index = 1;
            }
        }
        index = Math.max(0, index);

        currentWord = words.get(index);

        if (previousWord != null && previousWord != currentWord) {
            List<JButton> previous = getButtonsIWord(previousWord);
            for (int i = 0; i < previous.size(); i++) {
                JButton button = previous.get(i);
                button.setBackground(button.isEnabled() ? BACKGROUND_WORD : ANSWEARED_WORD);
            }
        }
        List<JButton> currentButtons = getButtonsIWord(currentWord);
        for (int i = 0; i < currentButtons.size(); i++) {
            JButton button = currentButtons.get(i);
            button.setBackground(SELECTED_WORD);
        }
        grid[y][x].setBackground(SELECTED_CELL);

        selectedX = x;
        selectedY = y;
        if (listener != null) {
            listener.selectionChanged(previousWord, currentWord);
        }

        previousWord = currentWord;

        // getButtonsForWord(currentWord) - > List<JButton>
        // for each button in list:
        // button.setBackground (green)
        // button.setForeground(red)
        // button.setFont(AwesomeFont)

    }

    private List<JButton> getButtonsIWord(IWord currentWord) {
        List<JButton> buttons = new ArrayList<JButton>();
        for (int j = 0; j < currentWord.getLength(); j++) {
            JButton button;
            if (currentWord.getDirection() == currentWord.HORIZONTAL) {
                button = grid[currentWord.getStartY()][currentWord.getStartX() + j];
            } else {
                button = grid[currentWord.getStartY() + j][currentWord.getStartX()];
            }
            buttons.add(button);

        }
        return buttons;
    }


    private static String charToString(String str, int index) {
        return String.valueOf(str.charAt(index));
    }

    public static interface OnSelectionChangeListener {
        public void selectionChanged(IWord oldWord, IWord newWord);
    }


    private class MyActionListener implements ActionListener, KeyListener {
        public final int x, y;

        public MyActionListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Clicked on " + x + ":" + y);
            selectedOne(y, x);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            System.out.println("Typed on " + x + ":" + y + " " + e.getKeyChar());
            typeChar(y, x, e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                System.out.println("Typed on " + x + ":" + y + " " + e.getKeyChar());
                typeChar(y, x, e);
            }
        }
    }
}