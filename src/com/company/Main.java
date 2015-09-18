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


    public static void Comparer(List<TradeBot> bots)
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
                if ((toSell.buyPrice-toBuy.sellPrice)>=20) {
                    if(toBuy.stock>0&&toSell.stock<toSell.max)
                        if(toSell.origin!="WareHouse")

//                            if(toBuy.sellPrice>=800)
                    if(toBuy.origin!="Wasda"||name.contains("Lime"))
                    System.out.println("Buy "+toBuy.stock+" " + name + " at " + toBuy.origin + " for " + toBuy.sellPrice / 100.00 + " and sell at " + toSell.origin + " for " + toSell.buyPrice / 100.00);
                }
            }
        }
    }

    public static void main(String[] args) {
        List<TradeBot> bots=new ArrayList<>();
        TradeBot bot;
        

        try {
            bots.add(bot=new ScrapTf());
            bot.Build(new URL(args[0]));

            bots.add(bot = new WasdaBot());
            bot.Build(new URL("http://www.tf2outpost.com/trade/26599888"));

            bots.add(bot = new Tradetf());
            bot.Build(new URL("http://www.trade.tf/mybots/index"));//"file:///D://tradetf.txt"));

            bots.add(bot = new ScraptfTrade());
            bot.Build(new URL("file:///D://strangebot.txt"));

            bots.add(bot = new Tf2Vendor());
            bot.Build(new URL("https://tf2vendor.com/browse"));

            bots.add(bot = new WareHouse());
            bot.Build(new URL("https://www.tf2wh.com/priceguide"));
            for(TradeBot b:bots)
            {
               b.ListItems();
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
