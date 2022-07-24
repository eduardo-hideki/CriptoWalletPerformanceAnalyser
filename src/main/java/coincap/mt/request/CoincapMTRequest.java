package coincap.mt.request;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class CoincapMTRequest {
    protected String address;

    public CoincapMTRequest() {
        this.address = "https://api.coincap.io/v2/assets";
    }

    // This method gets the response body from the requests made to the
    // Coincap API. Since it will run mainly on multithreading, the exception
    // handling have been dealt here
    protected JSONObject getResponseBody(String requestOption){

        JSONObject jsonObject = new JSONObject();
        try{
            URI uri = new URI(this.address+requestOption);
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            jsonObject = (JSONObject) JSONValue
                    .parse(response.body());
        } catch (Exception e) {
            System.out.println("Something went wrong while making the request! Stack trace:");
            e.printStackTrace();
        }
        return jsonObject;
    }

    // This method makes the request to get the asset identifier.
    public String fetchCurrencyId(String symbol) {
        JSONObject jsonObject = getResponseBody("?search="+symbol);
        return this.fetchSpecificField(jsonObject, "id");
    }

    // This method makes the request to get the asset current price in USD.
    public String fetchCurrentPrice(String currencyId) {
        JSONObject jsonObject = getResponseBody("/"
                + currencyId
                + "/history?interval=d1&start=1617753600000&end=1617753601000");
        return this.fetchSpecificField(jsonObject, "priceUsd");
    }

    // This method gets the response body from a request and parses it in order to find the
    // value for the desired field. The field "data" from the response body may yield a
    // JSON array or a JSON object, depending on the request. For the JSON array case,
    // since I am assuming the information data extracted from the CSV file is correct,
    // the first JSON object in the array is the desired result
    protected String fetchSpecificField(JSONObject argJSONObject, String field){
        JSONObject jsonObject;
        if (argJSONObject.get("data") instanceof JSONArray){
            JSONArray jsonArray = (JSONArray) argJSONObject.get("data");
            jsonObject = (JSONObject) jsonArray.get(0);
        } else {
            jsonObject = (JSONObject) argJSONObject.get("data");
        }

        return (String) jsonObject.get(field);
    }
}
