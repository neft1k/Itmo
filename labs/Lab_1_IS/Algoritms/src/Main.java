import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        List<List<String>> lines = new ArrayList<>();

        while (input.hasNextLine()) {
            String line = input.nextLine().trim();
            if (line.isEmpty()) {
                lines.add(Collections.emptyList());
            } else {
                lines.add(Arrays.asList(line.split("\\s+")));
            }        }
        for (int j = lines.size() - 1; j >= 0; j--) {
            List<String> line = lines.get(j);
            for (int i = line.size() - 1; i >= 0; i--) {
                System.out.print(line.get(i));
                if (i > 0){
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

    }
}