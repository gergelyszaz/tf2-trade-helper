package tf.tradesearch.base;

/**
 * Created by mad on 9/12/2015.
 */
public class Item implements Comparable<Item>{
    public static int KEY_PRICE=16;
    public int buyPrice;
    public int sellPrice;
    public String origin;
    public String name;
    public int stock;
    public int max;
    public int temp; //for temporary stuff
    public boolean buy=false;
    public boolean sell=false;

    public Item(String origin)
    {
        this.origin=origin;
        stock=1; max=2;
        buyPrice=0;
        sellPrice=999999999;
    }

    @Override
    public String toString() {
        return name+ " | "+buyPrice+ " | "+sellPrice+" | "+origin;
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

    @Override
    public int compareTo(Item item) {
        if(!this.name.equalsIgnoreCase(item.name)) return 0;
        int buy=this.buyPrice-item.sellPrice;
        int sell=item.buyPrice-this.sellPrice;
        return buy>0?buy:sell>0?-sell:0;
    }
}
