import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public final class CrosswordUtils {
    private CrosswordUtils() {
        // prevent instantiation
    }

    /**
     * Read an entire file and return it as a string
     *
     * @param pathname the path to the file
     * @return the file contents as a string
     * @throws IOException if something goes wrong..
     */
    public static String readFile(String pathname) throws IOException {
        String string = "";
        try {
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(pathname), "UTF8"));
            String line;
            while ((line = bufferReader.readLine()) != null) {
                string += line + "\n";
            }
            bufferReader.close();
        } catch (Exception e) {
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }
        return string;
    }

    public static boolean isPartOfWord(int col, int row, IWord w) {
        if (w.getDirection() == IWord.VERTICAL) {
            if (w.getStartX() != col) {
                return false;
            } else if (w.getStartY() <= row && (w.getStartY() + w.getLength()) > row) {
                return true;
            }
        }
        if (w.getDirection() == IWord.HORIZONTAL) {
            if (w.getStartY() != row) {
                return false;
            } else if (w.getStartX() <= col && (w.getStartX() + w.getLength()) > col) {
                return true;
            }
        }
        return false;
    }
}
