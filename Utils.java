import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    static List<String> readLinesFromFile(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        br.close();
        return lines;
    }

    static void writeLinesToFile(String filePath, List<String> lines) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        for (String line : lines) {
            bw.write(line);
            bw.newLine();
        }
        bw.close();
    }
}