public class Word implements IWord {
    private int x;
    private int y;
    private int dir;
    private String answear;
    private String question;

    public Word(int x, int y, int dir, String answear, String question) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.answear = answear;
        this.question = question;
    }

    @Override
    public int getStartX() {

        return x;
    }

    @Override
    public int getStartY() {
        return y;
    }

    @Override
    public int getDirection() {
        return dir;
    }

    @Override
    public int getLength() {
        return answear.length();
    }

    @Override
    public String getAnswer() {
        return answear;
    }

    @Override
    public String getHint() {
        return question;
    }
}
