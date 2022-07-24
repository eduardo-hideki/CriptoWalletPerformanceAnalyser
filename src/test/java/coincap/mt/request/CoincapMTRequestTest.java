package coincap.mt.request;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoincapMTRequestTest {
    CoincapMTRequest cmtrTester = new CoincapMTRequest();
    @Test
    public void testFetchCurrencyId() {
        assertEquals("Must be bitcoin", "bitcoin", cmtrTester.fetchCurrencyId("BTC"));
    }

    @Test
    public void testFetchCurrentPrice() {
        assertEquals("Must be 56999.9728252053067291", "56999.9728252053067291", cmtrTester.fetchCurrentPrice("bitcoin"));
    }
}