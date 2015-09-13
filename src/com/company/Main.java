package com.company;



import javafx.collections.transformation.SortedList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Main {

    public static void Comparer(TradeBot[] bots)
    {
        Set<String> keys=new HashSet<>();

        for(TradeBot tb:bots)
        {
            keys.addAll(tb.getItemNames());
        }

        for(String name:keys)
        {
            List<Item> items=new ArrayList<>();
            Item toBuy=null, toSell=null;
            for(TradeBot bot:bots)
            {
                Item item=bot.getItem(name);
                if(bot.getItem(name)!=null) {
                    if(toBuy==null) toBuy=item;
                    if(toSell==null) toSell=item;
                    if(toBuy.sellPrice>item.sellPrice)
                        toBuy=item;
                    if(toSell.buyPrice<item.buyPrice)
                        toSell=item;

                    items.add(bot.getItem(name));
                }
            }

            if(items.size()>=1) {


            //    System.out.println(toBuy + " " + toSell);
                if (toBuy.sellPrice < toSell.buyPrice) {
                    if(toBuy.stock>0&&toSell.stock<toSell.max)
                    if(toBuy.origin!="Wasdabot"||name.contains("Lime"))
                    System.out.println("Buy "+toBuy.stock+" " + name + " at " + toBuy.origin + " for " + toBuy.sellPrice / 100.00 + " and sell at " + toSell.origin + " for " + toSell.buyPrice / 100.00);
                }
            }
        }
    }

    public static void main(String[] args) {
        TradeBot[] bots=new TradeBot[3];
        bots[0]=new ScrapTf();
        bots[1]=new WasdaBot();
        bots[2]=new Tradetf();
        try {
            bots[0].Build(new URL(args[0]));
            bots[1].Build(new URL("http://www.tf2outpost.com/trade/26599888"));
            bots[2].Build(new URL("http://www.trade.tf/mybots/index"));//"file:///D://tradetf.txt"));
            for(TradeBot bot:bots)
            {
               // bot.ListItems();
            }
            Comparer(bots);



        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
        }
    }
}
