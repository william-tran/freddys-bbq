package microsec.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("branding")
public class Branding {
    private String restaurantName;
    private String menuTitle;

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

}
