import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Console {
    private final BufferedReader buffer;
    private final Predicate<String> regexReponse;

    private Console() {
        this.buffer = new BufferedReader(new InputStreamReader(System.in));
        this.regexReponse = Pattern.compile("^$|y|o|yes|oui", Pattern.CASE_INSENSITIVE).asMatchPredicate();
    }

    private static Console console;

    public BufferedReader getBuffer() {
        return buffer;
    }

    public Predicate<String> getRegexReponse() {
        return regexReponse;
    }

    public static Console getInstance() {
        if (console == null)
            console = new Console();
        return console;
    }

}
