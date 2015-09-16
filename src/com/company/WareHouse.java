package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mad on 2015. 09. 15..
 */
public class WareHouse extends AbstractTradeBot {
    public WareHouse() {
        super("WareHouse");
    }

    @Override
    public void Build(URL url) throws IOException {
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
        InputStream is = httpcon.getInputStream();  // throws an IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line="";
        while ((line = br.readLine()) != null) {
            if(line.contains("<tr data-name"))
            {
                Item item=new Item(this.getName());

                line=line.substring(line.indexOf('\"')+1, line.lastIndexOf('\"'));
                line=line.replace(" [BS]", "; Battle Scarred");
                line=line.replace(" [FN]", "; Factory New");
                line=line.replace(" [FT]", "; Field-Tested");
                line=line.replace(" [MW]","; Minimal Wear");
                line=line.replace(" [WW]","; Well-Worn");

                item.name=line;

                br.readLine();

                line=br.readLine();
                line=line.substring(line.indexOf('>')+1,line.lastIndexOf('<'));
                String[] stock=line.split("/");
                item.stock=Integer.parseInt(stock[0]);
                item.max = Integer.parseInt(stock[1]);

                line=br.readLine();
                line=line.substring(line.indexOf('>')+1,line.lastIndexOf('<')).replace(",", "");
                item.sellPrice=Integer.parseInt(line)/25;
                line=br.readLine();
                line=line.substring(line.indexOf('>')+1,line.lastIndexOf('<')).replace(",", "");
                item.buyPrice=Integer.parseInt(line)/25;
items.put(item.name,item);
            }
        }

    }
}
