package microsec.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Uris to other components
 * 
 * @author will.tran
 *
 */
@ConfigurationProperties("targets")
public class Targets {
    private String scheme;
    private String uaa;
    private String order;
    private String menu;

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getUaa() {
        return uaa;
    }

    public void setUaa(String uaa) {
        this.uaa = uaa;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
