package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mad on 9/12/2015.
 */
public class Tradetf implements TradeBot {
    public HashMap<String, Item> items = new HashMap<>();

    @Override
    public void Build(URL url) throws IOException {

        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
        InputStream is = httpcon.getInputStream();  // throws an IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(is));



        String line = "";
        while ((line = br.readLine()) != null) {
            boolean buy = false;
            if (line.contains("$scope.sellers = "))
                buy = false;
            else if (line.contains("$scope.buyers = "))
                buy = true;

            if (line.contains("$scope.sellers = ") || line.contains("$scope.buyers = ")) {
                System.out.println(line.length());
                line = line.replace("$scope.sellers = ", "");
                line = line.trim();
                String[] itemStrings = line.split("\\}, \\{");
                for (String itemString : itemStrings) {
                    Item item = new Item();
                    item.buyPrice = 999999999;
                    item.sellPrice = 999999999;
                    itemString = itemString.substring(itemString.lastIndexOf("\"price\": {"));
                    itemString = itemString.replace("\"price\": {", "");
                    String[] itemS = itemString.split("\\}, ");
                    item.name = itemS[1].replace("\"name\": ", "").replace("\"", "").replace("}];","").trim();
                    String[] price = itemS[0].replace("\"keys\":", "").replace("\"refs\":", "").replace(".0", "00").replace(".", "").split(",");
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

    @Override
    public Item getItem(String name) {
        return null;
    }

    @Override
    public void ListItems() {
        System.out.println("Trade.tf");
        for (Map.Entry entry : items.entrySet()) {
            System.out.println(entry.getValue());
        }
    }
}
