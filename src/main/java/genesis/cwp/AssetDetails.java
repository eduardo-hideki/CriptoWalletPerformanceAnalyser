package genesis.cwp;

import coincap.mt.request.CoincapMTRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

// This class was created with multithreading and sortable arrays capabilities
// in mind, thus the Thread extension and the Comparable implementation.
public class AssetDetails extends Thread implements Comparable<AssetDetails> {
    protected String currencySymbol;
    protected BigDecimal currencyQuantity;
    protected BigDecimal currencyPurchasePrice;
    protected String assetId;
    protected BigDecimal currentPrice;
    protected BigDecimal currentPosition;
    protected BigDecimal assetPerformance;

    public AssetDetails(String currencySymbol, BigDecimal currencyQuantity, BigDecimal currencyPurchasePrice)  {
        this.currencySymbol = currencySymbol;
        this.currencyQuantity = currencyQuantity.setScale(5, RoundingMode.HALF_UP);
        this.currencyPurchasePrice = currencyPurchasePrice.setScale(4, RoundingMode.HALF_UP);
    }

    // getters and setters
    public String getCurrencySymbol(){
        return this.currencySymbol;
    }

    public BigDecimal getCurrentQuantity() {
        return this.currencyQuantity;
    }

    public BigDecimal getCurrencyPurchasePrice() {
        return this.currencyPurchasePrice;
    }

    public String getAssetId() {
        return this.assetId;
    }

    public BigDecimal getCurrentPrice(){
        return this.currentPrice;
    }

    public BigDecimal getCurrentPosition(){
        return this.currentPosition;
    }

    public BigDecimal getAssetPerformance(){
        return assetPerformance;
    }

    // Method required for the multithreading capability. Prints logs to the console
    // regarding timestamp of the requests made and the computational time to calculate
    // the asset position as well as its performance.
    @Override
    public void run() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        System.out.println("Submitted request for asset " + this.currencySymbol + " at " + dateFormat.format(now));
        this.assetId = new CoincapMTRequest().fetchCurrencyId(this.currencySymbol);
        this.currentPrice = new BigDecimal(new CoincapMTRequest().fetchCurrentPrice(this.assetId)).setScale(4, RoundingMode.HALF_UP);
        this.currentPosition = this.currencyQuantity.multiply(this.currentPrice).setScale(2, RoundingMode.HALF_UP);
        this.assetPerformance = this.currentPrice.divide(this.currencyPurchasePrice, 2, RoundingMode.HALF_UP);
        now = new Date();
        System.out.println("Finished request for asset " + this.currencySymbol + " at " + dateFormat.format(now));
    }

    // Method to implement the Comparable interface. The case when assets have the same performance
    // have been deliberately ignored, but could be treated with a secondary sorting rule.
    @Override
    public int compareTo(AssetDetails assetDetail){
        BigDecimal comparison = this.assetPerformance.max(assetDetail.assetPerformance);
        if (comparison.equals(this.assetPerformance)){
            return -1;
        }
        return 1;
    }
}
