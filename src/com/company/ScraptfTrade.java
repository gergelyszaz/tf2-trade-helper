package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by mad on 2015. 09. 14..
 */
public class ScraptfTrade extends AbstractTradeBot {
    public ScraptfTrade() {
        super("Scrap.tf");
    }

    @Override
    public void Build(URL url) throws IOException {
        InputStream is = url.openStream();  // throws an IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line="";
        int state=0;
        String name="";
        int price=0;
        while ((line = br.readLine()) != null) {
            line=line.trim();

            line=line.replace("<span class='rarityrare'>", "");
            line=line.replace("</span>", "");


            switch(state) {
                case 0:
                if (line.contains("data-title")) {
                    line=line.replace("data-title=\"<span class='quality11'>","");
                    line=line.replace("data-title=<span class='raritymythical'>","");
                    line=line.replace("data-title=<span class='raritycommon'>","");
                    line=line.replace("data-title=<span class='raritylegendary'>","");
                    line=line.replace("<span class='raritymythical'>","");
                    line=line.replace("<span class='raritycommon'>","");
                    line=line.replace("<span class='rarityuncommon'>","");
                    line=line.replace("<span class='raritylegendary'>","");
                    line=line.replace("&amp;apos;","'");
                    line=line.replace(" (","; ");
                    line=line.replace(")","");

                    line=line.replace("data-title=","");

                    line=line.replace("\"","");
                    name=line.trim();
                }
                case 1:
                    if(line.contains("-cost"))
                    {
                        line=line.replace("data-cost-stranges=","");
                        line=line.replace("data-cost-skins=","");
                        line=line.replace("data-bptf-cost=","");

                        int hat=0;
                        if(line.contains("-hat-")) {
                            hat=1;
                        }
                        line = line.replace("data-hat-cost=", "");
                        String p=line.replace("\"","").trim();
                        price=Integer.parseInt(p)*100/9;
                        Item item=new Item(this.getName());
                        item.name=name; item.sellPrice=price;
                        item.buyPrice=hat*(price-55);
                        if(items.get(name)!=null)
                        {
                           if(items.get(name).sellPrice>item.sellPrice)
                           {
                               items.get(name).sellPrice=item.sellPrice;
                           }
                        }
                        else
                        items.put(name,item);
                    }
            }
        }
    }
}
