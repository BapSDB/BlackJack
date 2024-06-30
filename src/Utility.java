import java.util.Random;

public class Utility {
    private final Random random;

    private Utility () {
        this.random = new Random();
    }

    private static Utility utility;

    public static Utility getInstance() {
        if (utility == null)
            utility = new Utility();
        return utility;
    }

    public Random getRandom() {
        return random;
    }
}
