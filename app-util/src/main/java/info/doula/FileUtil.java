package info.doula;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Created by doula_itc on 2017-08-16.
 */
public class FileUtil {

    public static String readText(String filename) throws IOException {
            return Files.lines(Paths.get(filename))
                    .parallel() // for parallel processing
                    .map(String::trim) // to change line
                    .filter(line -> line.length() > 2) // to filter some lines by a predicate
                    .collect(Collectors.joining()); // to join lines

    }
}
