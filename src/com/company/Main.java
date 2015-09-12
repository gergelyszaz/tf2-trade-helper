package com.company;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;

        Hashtable<String, Item> wasdabot = new Hashtable<>();
        List<Item> scrapbot = new ArrayList<>();
        int KEY_PRICE = 16;

        try {

            url = new URL("http://www.tf2outpost.com/trade/26599888");
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

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
                            buyPrice += KEY_PRICE * Integer.parseInt(s) * 100;
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
                            sellPrice += KEY_PRICE * Integer.parseInt(s) * 100;
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

            //Scrap.tf parse
            System.out.println(args[0]);
            url = new URL(args[0]);

            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            int item_found_state = 0;

            String itemS = "";
            Item item=null;
            while ((line = br.readLine()) != null) {
                line = line.trim();


                switch (item_found_state) {
                    case 0:
                        for (Map.Entry entry : wasdabot.entrySet()) {
                            if (line.contains("<td>" + entry.getKey().toString() + "</td>")) {
                                //System.out.println(line);
                                item_found_state++;

                                item=new Item();
                                item.name=entry.getKey().toString();
                                break;
                            }
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

                            item_found_state = 0;

                            int profit=item.buyPrice-wasdabot.get(item.name).sellPrice;

                            if( profit>0 && (max-stock) >0 )
                            {
                             //   System.out.println("Scrap.tf will buy "+(max-stock)+" pieces of "+item.name+ " earning "+profit/100.00+ " each. (Buy/Sell): "+item.buyPrice+"/"+wasdabot.get(item.name).sellPrice);
                            }

                            profit=wasdabot.get(item.name).buyPrice-item.sellPrice;
                            if(profit>0 && stock >0){
                                System.out.println("Wasdabot will buy "+(stock)+" pieces of "+item.name+ " earning "+profit/100.00+ " each. (Buy/Sell): "+wasdabot.get(item.name).buyPrice+"/"+item.sellPrice);
                            }
                            //System.out.println("Will buy: " + (max - stock));
                        }

                        break;
                }
            }

        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                // nothing to see here
            }
        }
    }
}
