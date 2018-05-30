package com.network.common.api.bean.newest;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "channel", strict = false)
public class Newest {
    @ElementList(inline = true, required = false, name = "item", entry = "item")
    public List<NewestItem> item;

    public Newest() {
    }

    @Override
    public String toString() {
        return "Newest{" +
                "item=" + item +
                '}';
    }
}
