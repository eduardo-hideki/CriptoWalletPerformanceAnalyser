package genesis.cwp;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AssetDetailsTest {
    AssetDetails adTester = new AssetDetails("BTC",
            new BigDecimal("0.51234"),
            new BigDecimal("28000.1234"));

    AssetDetails adTesterEth = new AssetDetails("ETH",
            new BigDecimal("0.51234"),
            new BigDecimal("28000.1234"));

    @Test
    public void testRun() {
        adTester.run();
        assertEquals("Should be BTC", "BTC", adTester.getCurrencySymbol());
        assertEquals("Should be bitcoin", "bitcoin", adTester.getAssetId());
        assertEquals("Should be 0.51234", new BigDecimal("0.51234"), adTester.getCurrentQuantity());
        assertEquals("Should be 28000.1234", new BigDecimal("28000.1234"), adTester.getCurrencyPurchasePrice());
        assertEquals("Should be 56999.9728", new BigDecimal("56999.9728"), adTester.getCurrentPrice());
        assertEquals("Should be 29203.37", new BigDecimal("29203.37"), adTester.getCurrentPosition());
        assertEquals("Should be 2.04", new BigDecimal("2.04"), adTester.getAssetPerformance());
    }

    @Test
    public void testCompareTo() {
        adTester.run();
        adTesterEth.run();
        assertEquals("Should be -1", -1, adTester.compareTo(adTesterEth));
    }
}