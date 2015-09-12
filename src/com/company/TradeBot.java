package com.company;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

/**
 * Created by mad on 9/12/2015.
 */
public interface TradeBot {
    public void Build(URL url) throws IOException;
    public Item getItem(String name);
    public void ListItems();
    public String getName();
    public Set<String> getItemNames();

}
