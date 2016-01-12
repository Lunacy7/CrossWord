import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainPanel extends JPanel {
    // Controls panel ui
    private final JButton mVerifyButton = new JButton("Check");
    private final JButton mBrowseButton = new JButton("Load Crossword");
    private final JLabel mHintLabel = new JLabel();
    private final JPanel mControlsPanel = new JPanel(new BorderLayout());

    private final JFileChooser mFileChooser;

    public MainPanel() {
        this.setLayout(new BorderLayout());

        final FileFilter textType = new FileNameExtensionFilter("Text (.txt)", "txt");
        mFileChooser = new JFileChooser(new File("D://CrossWord"));
        mFileChooser.addChoosableFileFilter(textType);

        // Setup the hint visuals
        mHintLabel.setText("Hint...");
        mHintLabel.setVerticalTextPosition(JLabel.BOTTOM);
        mHintLabel.setHorizontalTextPosition(JLabel.CENTER);
        mHintLabel.setLocation(150, 200);

        mControlsPanel.add(mBrowseButton, BorderLayout.NORTH);
        mControlsPanel.add(mVerifyButton, BorderLayout.SOUTH);
        mControlsPanel.add(mHintLabel, BorderLayout.CENTER);
        mControlsPanel.setPreferredSize(new Dimension(250, 300));

        this.add(mControlsPanel, BorderLayout.EAST);

        // Choose to crossword files
        mBrowseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int retVal = mFileChooser.showOpenDialog(getParent());
                if (retVal == JFileChooser.APPROVE_OPTION) {
                    String jsonString = "";
                    try {
                        jsonString = CrosswordUtils.readFile(mFileChooser.getSelectedFile().getPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    JSONObject rootObject = new JSONObject(jsonString);
                    JSONArray words = rootObject.getJSONArray("words");

                    java.util.List<Word> wordlist = new ArrayList<>();
                    for (int i = 0; i < words.length(); i++) {
                        JSONArray word = words.getJSONArray(i);
                        Word newWord = new Word(word.getInt(0), word.getInt(1), word.getInt(2), word.getString(3), word.getString(4));
                        wordlist.add(newWord);
                    }

                    System.out.println(wordlist.size());
                    final Crossword crossword = new Crossword(18, 10, wordlist);
                    java.util.List<? extends IWord> l = crossword.getWordsAt(10, 3);

                    final PanelCrossword crosswordPanel = new PanelCrossword(crossword);
                    crosswordPanel.setListener(new PanelCrossword.OnSelectionChangeListener() {
                        @Override
                        public void selectionChanged(IWord oldWord, IWord newWord) {
                            mHintLabel.setText(newWord.getHint());
                        }
                    });
                    crosswordPanel.setPreferredSize(new Dimension(400, 300));
                    mVerifyButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            crosswordPanel.verify();
                        }
                    });
                    java.util.List<Component> toremove = new ArrayList<>();
                    for (Component c : getComponents()) {
                        if (c instanceof PanelCrossword) {
                            toremove.add(c);
                        }
                    }
                    for (Component c : toremove) {
                        remove(c);
                    }
                    add(crosswordPanel, BorderLayout.CENTER);
                    revalidate();
                }
            }
        });
    }
}
