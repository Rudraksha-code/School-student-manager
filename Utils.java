import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Utils {

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

    static ArrayList<Integer> parseDetails(String details, CourseManager courseManager) {
        ArrayList<Integer> values = new ArrayList<>(Collections.nCopies(courseManager.getCourses().size(), 0));
        details = details.replace("{", "").replace("}", "").trim();
        String[] entries = details.split(", ");
        for (String entry : entries) {
            String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                String courseName = keyValue[0].trim();
                int value = Integer.parseInt(keyValue[1].trim());
                int index = courseManager.getCourseIndex(courseName);
                if (index != -1) {
                    values.set(index, value);
                }
            }
        }
        return values;
    }
}