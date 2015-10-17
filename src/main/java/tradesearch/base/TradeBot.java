package main.java.tradesearch.base;

import java.io.IOException;
import java.util.Set;

/**
 * Created by mad on 9/12/2015.
 */
public interface TradeBot {

    public void Refresh() throws IOException;
    public Item getItem(String name);
    public void ListItems();
    public String getName();
    public Set<String> getItemNames();

}
