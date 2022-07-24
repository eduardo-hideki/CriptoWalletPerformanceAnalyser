package genesis.cwp;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TaskExecutor  {
    protected String filePath;
    protected List<AssetDetails> assetList;
    public TaskExecutor(String fPath) {
        this.filePath = fPath;
    }

    // This method is responsible for calling the methods that:
    //   - read the CSV file and store the data in memory;
    //   - run multiple threads for the requests as per requirement;
    //   - analyses total, worst and best asset performances.
    // Just to be safe, it throws Exception.
    public void runAnalysis() throws Exception {
        this.assetList = this.buildAssetDetailsList();
        this.runMultithreadedRequests();
        this.analyseResults();
    }

    // This method reads the information from CSV file (supposing there's no issue in that information)
    // and returns an iterable object contaning details from each asset from the wallet CSV description.
    // Because of Scanner, it requires the FileNotFoundException to be a throwable.
    protected List<AssetDetails> buildAssetDetailsList() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(this.filePath));
        sc.useDelimiter(",");
        // skips first line, the file "header"
        sc.nextLine();
        // creates the iterable as an synchronizedList in order to be thread safe
        List<AssetDetails> assetList = Collections.synchronizedList(new ArrayList<AssetDetails>());
        while(sc.hasNextLine()){
            String[] assetBasicInformation = sc.nextLine().split(",");
            assetList.add(new AssetDetails(assetBasicInformation[0],
                    new BigDecimal(assetBasicInformation[1]),
                    new BigDecimal(assetBasicInformation[2])));
        }
        sc.close();
        return assetList;
    }

    // This method runs the technical requirement of most threads running at once, with maximum
    // number of threads = 3.
    // Because of the thread usage, it requires InterruptedException to be a throwable.
    protected void runMultithreadedRequests() throws InterruptedException {
        for (int i = 0; i < this.assetList.size(); i+=3) {
            this.assetList.get(i).start();
            if (i+1 < this.assetList.size()) {
                this.assetList.get(i + 1).start();
                if (i+2 < this.assetList.size()) {
                    this.assetList.get(i + 2).start();
                }
            }
            this.assetList.get(i).join();
            if (i+1 < this.assetList.size()) {
                this.assetList.get(i + 1).join();
                if (i+2 < this.assetList.size()) {
                    this.assetList.get(i + 2).join();
                }
            }
        }
    }

    // This method simply does the analysis after all the required information is
    // gathered and prints the result to the console.
    protected void analyseResults() {
        BigDecimal total = this.assetList.stream()
                .map(x -> x.getCurrentPosition())
                .reduce(BigDecimal.ZERO,
                        (subtotal, element) -> subtotal.add(element));
        Collections.sort(this.assetList);
        System.out.println("-----------------------------");
        System.out.println(MessageFormat.format("total={0},best_asset={1},best_performance={2},worst_asset={3},worst_performance={4}",
                total,
                this.assetList.get(0).getCurrencySymbol(),
                this.assetList.get(0).getAssetPerformance(),
                this.assetList.get(this.assetList.size()-1).getCurrencySymbol(),
                this.assetList.get(this.assetList.size()-1).getAssetPerformance()));
    }
}
