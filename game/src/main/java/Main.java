import org.jspace.ActualField;
import org.jspace.SequentialSpace;
import org.jspace.Space;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Space space = new SequentialSpace();
        space.put("Hello World");
    }
}
