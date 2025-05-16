//*From Scrabble- Like Game
package scrabblegame;
public class Word implements Comparable<Word> {
    private String text;

    public Word(String text) {
        this.text = text.toLowerCase();
    }

    public String getText() {
        return text;
    }

    @Override
    public int compareTo(Word other) {
        return this.text.compareTo(other.text);
    }
}