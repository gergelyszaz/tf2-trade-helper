package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mad on 9/12/2015.
 */
public class WasdaBot implements TradeBot {
    HashMap<String, Item> wasdabot=new HashMap<>();
    @Override
    public void Build(URL url) throws IOException {


        InputStream is = url.openStream();  // throws an IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line="";
        while ((line = br.readLine()) != null) {

            if (
                    line.startsWith("<span style=\"color:#88ddff;\">BUY")
                    ) {
                String[] items = line.split("</span> <span style=\"color:#5f9ab2;\">SELL: ");
                String buyString = items[0].replace("<span style=\"color:#88ddff;\">BUY: ", "");
                items = items[1].split("</span> ");
                String sellString = items[0];
                String name = items[1].replace("<br />", "");
                name = name.replace("</div>", "");
                name = name.substring(10);

                int buyPrice = 0;

                String[] buyS = buyString.split(" \\+ ");


                for (String s : buyS) {
                    if (s.contains("ref")) {
                        s = s.replace(" ref", "");


                        if (s.contains(".")) {
                            s = s.replace(".", "");
                            buyPrice += Integer.parseInt(s);
                        } else {
                            buyPrice += Integer.parseInt(s) * 100;
                        }
                    }
                    if (s.contains("key")) {
                        s = s.replace(" keys", "");
                        s = s.replace(" key", "");
                        buyPrice += Item.KEY_PRICE * Integer.parseInt(s) * 100;
                    }
                }

                int sellPrice = 0;


                String[] sellS = sellString.split(" \\+ ");

                for (String s : sellS) {
                    if (s.contains("ref")) {
                        s = s.replace(" ref", "");
                        if (s.contains(".")) {
                            s = s.replace(".", "");
                            sellPrice += Integer.parseInt(s);
                        } else {
                            sellPrice += Integer.parseInt(s) * 100;
                        }


                    }
                    if (s.contains("key")) {
                        s = s.replace(" keys", "");
                        s = s.replace(" key", "");
                        sellPrice += Item.KEY_PRICE * Integer.parseInt(s) * 100;
                    }
                }


                Item item = new Item();
                item.name = name;
                item.buyPrice = buyPrice;
                item.sellPrice = sellPrice;

                if (!wasdabot.containsKey(item.name)) {
                    wasdabot.put(item.name, item);
                }
                //System.out.println(item);
            }
        }
        is.close();
    }

    @Override
    public Item getItem(String name) {
        return wasdabot.get(name);
    }

    @Override
    public void ListItems() {
        System.out.println("Wasdabot");
        for(Map.Entry entry: wasdabot.entrySet())
        {
            System.out.println(entry.getValue());
        }
    }
}
