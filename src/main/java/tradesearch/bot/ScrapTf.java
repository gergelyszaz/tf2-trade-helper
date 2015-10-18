package main.java.tradesearch.bot;

import main.java.tradesearch.base.AbstractTradeBot;
import main.java.tradesearch.base.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mad on 9/12/2015.
 */
public class ScrapTf extends AbstractTradeBot {

    public ScrapTf(URL url) {
        super("Scraptf",url);
    }


    @Override
    public void Build() throws IOException {
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
        InputStream is = httpcon.getInputStream();  // throws an IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line="";

        int item_found_state = 0;

        String itemS = "";
        Item item=null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            line=line.replaceAll("<i18n>","").replaceAll("</i18n>","");

            switch (item_found_state) {
                case 0:
                    if(line.contains("data-original-title=\"\"></td>")) {
                        line = br.readLine();
                        line=line.replace("</td>","").replace("<td>", "");


                            //System.out.println(line);
                            item_found_state++;

                            item=new Item(getName());
                            item.name=line.trim()
                                    .replace("Flippin", "Flippin'")
                                    .replace("High Five","The High Five!")
                                    .replace("Box Trot","The Box Trot")
                                    .replace("Battin A", "Battin' a")
                                    .replace("Boston", "The Boston")
                                    .replace("Meet", "The Meet")
                                    .replace("Rock Paper Scissors", "Rock, Paper, Scissors")
                                    .replace("Proletariat", "The Proletariat")
                                    .replace("Killer Solo", "The Killer Solo")
                                    .replace("Schade", "The Schade")
                                    .replace("Genuine Taunt: ", "Genuine ")
                            ;
                            item.URL=url.toString();
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

}
