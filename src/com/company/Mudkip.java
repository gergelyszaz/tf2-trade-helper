package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by mad on 2015. 09. 14..
 */
public class Mudkip extends AbstractTradeBot {

    public Mudkip() {
        super("Mudkip");
    }

    @Override
    public void Build(URL url) throws IOException {
        InputStream is = url.openStream();  // throws an IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = "";
        while ((line = br.readLine()) != null) {
            if (line.contains("wanted-items")) {
                line = br.readLine();
                System.out.println(line);
                String[] lines = line.split("data-name");
//TODO: not ok, these are the backpack.tf prices
                for (String l : lines) {
                    //System.out.println(l);
                    if (l.contains("data-price")) {
                        l = l.substring(l.indexOf("\"") + 1);
                        String name = l.substring(0, l.indexOf("\""));
                        l = l.substring(l.indexOf("data-price"));
                        l=l.substring(l.indexOf("\"") + 1);
                        String priceS = l.substring(0, l.indexOf("\""));
                        System.out.println(name);
                        System.out.println(priceS);
                        int price;
                        if(priceS.contains("Key"))
                        {
                            priceS=priceS.replace("Key","").replace("Keys","").trim();
                            int multiplier=10;
                            if(priceS.contains(".")) {priceS.replace(".",""); multiplier=1;}

                        }
                        if(priceS.contains("Refined")){
                            priceS=priceS.replace("Refined","").trim();
                            int multiplier=100;
                            if(priceS.contains(".")) {priceS.replace(".",""); multiplier=1;}
                            price=multiplier*Integer.parseInt(priceS);

                        }

                    }
                }

                return;
            }


        }

    }
}

