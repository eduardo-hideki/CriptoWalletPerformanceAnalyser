package genesis.cwp;

import java.io.FileNotFoundException;

public class Main {
    private static String filename;

    // the main method may take one argument, it being the file name (though it must exist
    // in the path src/main/resources).
    public static void main(String[] args) {
        if (args.length == 0) {
            filename = "teste.csv";
        } else {
            filename = args[0];
        }
        TaskExecutor te = new TaskExecutor("src/main/resources/"+filename);

        try {
            te.runAnalysis();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File not found! Stack trace:");
            fnfe.printStackTrace();
        } catch (InterruptedException ie) {
            System.out.println("Request interrupted! Stack trace:");
            ie.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something went wrong! Stack trace:");
            e.printStackTrace();
        }
    }
}