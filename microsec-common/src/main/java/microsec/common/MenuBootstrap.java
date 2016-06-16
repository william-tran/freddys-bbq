package microsec.common;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import microsec.freddysbbq.menu.model.v1.MenuItem;

@ConfigurationProperties("menuBootstrap")
public class MenuBootstrap {
    private List<MenuItem> items;

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}
