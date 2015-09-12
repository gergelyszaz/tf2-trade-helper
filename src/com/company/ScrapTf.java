package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mad on 9/12/2015.
 */
public class ScrapTf implements TradeBot {
    public HashMap<String, Item> items=new HashMap<>();


    @Override
    public void Build(URL url) throws IOException {
        InputStream is = url.openStream();  // throws an IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line="";

        int item_found_state = 0;

        String itemS = "";
        Item item=null;
        while ((line = br.readLine()) != null) {
            line = line.trim();


            switch (item_found_state) {
                case 0:
                    if(line.contains("data-original-title=\"\"></td>"))
                    {
                        line=br.readLine();
                        line=line.replace("</td>","");
                        line=line.replace("<td>","");

                            //System.out.println(line);
                            item_found_state++;

                            item=new Item(getName());
                            item.name=line.trim();
                            items.put(item.name,item);
                    }

                    break;
                case 1:
                    if (line.contains("<td>")) {
                        item_found_state++;
                        int sell = Item.ParsePrice(line);
                        item.sellPrice=sell;
                        //System.out.println("Sell: " + sell);
                    }
                    break;
                case 2:
                    if (line.contains("<td>")) {
                        item_found_state++;
                        int buy = Item.ParsePrice(line);
                        item.buyPrice=buy;
                        // System.out.println("Buy: " + buy);
                    }

                case 3:
                    if (line.contains(("<div rel=\"tooltip\" title=\""))) {
                        line = line.replace("<div rel=\"tooltip\" title=\"", "");
                        String[] stockMax = line.split("\"")[0].split("/");
                        int stock=0; int max=0;
                        if(stockMax.length>=2) {
                            stock = Integer.parseInt(stockMax[0]);
                            max = Integer.parseInt((stockMax[1]));
                        }
                        item.stock=stock;
                        item.max=max;


                        item_found_state = 0;
                    }

                    break;
            }}
        is.close();
    }

    @Override
    public Item getItem(String name) {
        return items.get(name);
    }

    @Override
    public void ListItems() {
        System.out.println(getName());
        for(Map.Entry entry: items.entrySet())
        {
            System.out.println(entry.getValue());
        }
    }

    @Override
    public String getName() {
        return "Scrap.tf";
    }

    @Override
    public Set<String> getItemNames() {
        return items.keySet();
    }


}
