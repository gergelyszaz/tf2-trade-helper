package main.java.tradesearch.bot;

import main.java.tradesearch.base.Item;
import main.java.tradesearch.base.AbstractTradeBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mad on 9/12/2015.
 */
public class Tradetf extends AbstractTradeBot {


    public Tradetf(URL url) {
        super("Trade.tf",url);
    }

    @Override
    public void Build() throws IOException {

        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
        InputStream is = httpcon.getInputStream();  // throws an IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(is));



        String line = "";
        while ((line = br.readLine()) != null) {
            line=line.replace("\\u00dc","U");
            boolean buy = false;
            if (line.contains("$scope.sellers = "))
                buy = false;
            else if (line.contains("$scope.buyers = "))
                buy = true;

            if (line.contains("$scope.sellers = ") || line.contains("$scope.buyers = ")) {
                line = line.replace("$scope.sellers = ", "");
                line = line.trim();
                String[] itemStrings = line.split("\\}, \\{");
                for (String itemString : itemStrings) {
                    Item item = new Item(getName());
                    item.buyPrice = 0;
                    item.sellPrice = 999999999;
                    itemString = itemString.substring(itemString.lastIndexOf("\"price\": {"));
                    itemString = itemString.replace("\"price\": {", "");
                    String[] itemS = itemString.split("\\}, ");
                    item.name = itemS[1].replace("\"name\": ", "").replace("\"", "").replace("}];","").trim();
                    String[] price = itemS[0].replace("\"keys\":", "").replace("\"refs\":", "").replace(".0", "00").replace(".", "").replace("null","0").split(",");
                    //System.out.println(price[0]+";"+price[2]);
                    int sum = Integer.parseInt(price[0].trim()) * Item.KEY_PRICE + Integer.parseInt(price[2].trim());
                    if (items.containsKey(item.name)) {
                        item = items.get(item.name);
                    } else {
                        items.put(item.name, item);
                    }
                    if (buy) item.buyPrice = sum;
                    else item.sellPrice = sum;

                }
            }


        }


    }

}
