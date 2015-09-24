package tf.tradesearch.bot;

import tf.tradesearch.base.AbstractTradeBot;
import tf.tradesearch.base.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mad on 2015. 09. 15..
 */
public class Tf2Vendor extends AbstractTradeBot {
    public Tf2Vendor(URL url) {
        super("Tf2Vendor",url);
    }

    @Override
    public void Build() throws IOException {
        HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
        httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
        InputStream is = httpcon.getInputStream();  // throws an IOException
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line="";
        while ((line = br.readLine()) != null) {

            if (line.contains("scrap=")) {
                Item item=new Item(this.getName());

                line=line.replace("\"","");

                line=line.substring(line.indexOf("scrap="),line.indexOf("classes="));
                String[] itemS=line.split("name=");
                item.sellPrice=Integer.parseInt(itemS[0].replace("scrap=","").trim())*100/9;
                String[] nameS=itemS[1].split("quality=");

                item.name=(nameS[1].contains("Unique")?"":(nameS[1].trim()+" "))+nameS[0].trim();
                items.put(item.name,item);
            }
        }
    }
}
