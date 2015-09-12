package com.company;

import java.util.Objects;

/**
 * Created by mad on 9/12/2015.
 */
public class Item{
    public static int KEY_PRICE=16;
    public int buyPrice;
    public int sellPrice;
    public String name;

    @Override
    public String toString() {
        return name+ " | "+buyPrice+ " | "+sellPrice;
    }

    public static int ParsePrice(String line)
    {

        int ret=0;
        line=line.replace("<td>","");
        String[] priceS=line.split(", ");
        for(String s:priceS)
        {
            if(s.contains("key"))
            {
                s=s.replace("keys","");
                s=s.replace("key","");
                ret+=Integer.parseInt(s.trim())*KEY_PRICE*100;
            }
            if(s.contains("ref"))
            {
                int multiplier=1;
                s=s.replace(" refined","");
                if(s.contains("."))
                {
                    s=s.replace(".","");
                }
                else
                {
                    multiplier=100;
                }
                ret+=Integer.parseInt(s.trim())*multiplier;
            }
        }
        return ret;
    }
}
