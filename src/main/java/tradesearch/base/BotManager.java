package main.java.tradesearch.base;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by mad on 2015. 09. 24..
 */
public class BotManager {
    private HashMap<String, List<Item>> items=new HashMap<>();
    private List<TradeBot> bots = new ArrayList<>();



    public List<List<Item>> Search(String name) {
        List<List<Item>> ret = new ArrayList<>();
        name=name.toLowerCase();
        for (String s : items.keySet()) {
            if (s.toLowerCase().contains(name)) {
                ret.add(items.get(s));
            }
        }

        return ret;
    }


    public void AddBot(TradeBot bot) {
        bots.add(bot);
    }


    public void Compare() {
        Set<String> keys = new HashSet<>();


        for (TradeBot tb : bots) {
            keys.addAll(tb.getItemNames());
        }

        for (String name : keys) {
            Item toBuy = null, toSell = null;
            for (TradeBot bot : bots) {
                Item item = bot.getItem(name);
                if (item != null) {
                    if (toBuy == null) toBuy = item;
                    if (toSell == null) toSell = item;
                    if (toBuy.sellPrice > item.sellPrice && item.stock>0)
                        toBuy = item;
                    if (toSell.buyPrice < item.buyPrice && item.max>item.stock)
                        toSell = item;

                }
            }

            if (toBuy != null) {
                toBuy.buy = true;
                toSell.sell = true;

                //    System.out.println(toBuy + " " + toSell);
                if (toSell.buyPrice > 0)
                if ((toSell.buyPrice/(float)toBuy.sellPrice)>=1.0)
                    if ((toSell.buyPrice - toBuy.sellPrice) >= 20)
                        if (toBuy.stock > 0 && toSell.stock < toSell.max)
                         if (toSell.origin != "WareHouse")

                                //                      if(toBuy.sellPrice<=2400)
                           //     if (toBuy.origin != "Wasda")
                                    System.out.println("Buy " + toBuy.stock + " " + name + " at " + toBuy.origin + " for " + toBuy.sellPrice / 100.00 + " and sell at " + toSell.origin + " for " + toSell.buyPrice / 100.00);

            }
        }
    }

    public void Refresh() {
        items.clear();
        for (TradeBot b : bots) {
            try {
                b.Refresh();
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            Set<String> keys = new HashSet<>();
            keys.addAll(b.getItemNames());
            for (String k : keys) {
                if (items.get(k) == null)
                    items.put(k, new ArrayList<>());
                items.get(k).add(b.getItem(k));
            }
        }
    }

}
