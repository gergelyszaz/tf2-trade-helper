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
        TradeBot[] bots=new TradeBot[3];
        bots[0]=new ScrapTf();
        bots[1]=new WasdaBot();
        bots[2]=new Tradetf();
        try {
            //bots[0].Build(new URL(args[0]));
           // bots[1].Build(new URL("http://www.tf2outpost.com/trade/26599888"));
            bots[2].Build(new URL("http://www.trade.tf/mybots/index"));//"file:///D://tradetf.txt"));
            for(TradeBot bot:bots)
            {
                bot.ListItems();
            }



        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
        }
    }
}
