import java.util.ArrayList;
import java.util.List;

public class Crossword implements CrosswordInterface {
    private int width;
    private int heigh;
    private List<Word> words;

    public Crossword(int width, int heigh, List<Word> words) {
        this.width = width;
        this.heigh = heigh;
        this.words = words;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return heigh;
    }

    @Override
    public List<? extends IWord> getWordsAt(int x, int y) {
        List<Word> outWords = new ArrayList<Word>();
        for (Word w : words) {
            if (CrosswordUtils.isPartOfWord(x, y, w)) {
                outWords.add(w);
            }
        }
        return outWords;
    }

    @Override
    public List<Word> getWords() {
        return words;
    }
}
