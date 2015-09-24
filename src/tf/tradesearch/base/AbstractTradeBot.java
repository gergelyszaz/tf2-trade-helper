package tf.tradesearch.base;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mad on 2015. 09. 14..
 */
public abstract class AbstractTradeBot implements TradeBot {
    protected HashMap<String, Item> items = new HashMap<>();
    protected String name;
    protected URL url;

    public AbstractTradeBot(String name, URL url) {
        this.name = name;
        this.url = url;
    }


    public void Refresh() throws IOException {
        items.clear();
        Build();
    }

    protected abstract void Build() throws IOException;


    @Override
    public Item getItem(String name) {
        return items.get(name);
    }

    @Override
    public void ListItems() {
        System.out.println(getName());
        for (Map.Entry entry : items.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getItemNames() {
        return items.keySet();
    }
}
