package microsec.freddysbbq.admin_app;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import microsec.common.Branding;
import microsec.common.DumpTokenEndpointConfig;
import microsec.freddysbbq.menu.model.v1.MenuItem;
import microsec.freddysbbq.order.model.v1.Order;

@SpringBootApplication
@Controller
@EnableOAuth2Sso
@EnableDiscoveryClient
@EnableCircuitBreaker
@Import(DumpTokenEndpointConfig.class)
public class AdminApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        if (securityProperties.isRequireSsl()) {
            http.requiresChannel().anyRequest().requiresSecure();
        }

        http.authorizeRequests()
                .expressionHandler(new OAuth2WebSecurityExpressionHandler())
                .anyRequest().access("#oauth2.hasScope('menu.write')");
    }

    @Autowired
    @Qualifier("loadBalancedOauth2RestTemplate")
    private OAuth2RestTemplate restTemplate;

    @Bean
    public Branding branding() {
        return new Branding();
    }

    @LoadBalanced
    @Bean
    public OAuth2RestTemplate loadBalancedOauth2RestTemplate(
            OAuth2ProtectedResourceDetails resource,
            OAuth2ClientContext oauth2Context) {
        OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(resource, oauth2Context);
        return oauth2RestTemplate;
    }

    @RequestMapping("/")
    public String index(Model model, Principal principal) {
        model.addAttribute("username", principal.getName());
        return "index";
    }

    @HystrixCommand
    @RequestMapping("/menuItems")
    public String menu(Model model) throws Exception {
        PagedResources<MenuItem> menu = restTemplate
                .exchange(
                        "//menu-service/menuItems",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedResources<MenuItem>>() {
                        })
                .getBody();
        model.addAttribute("menu", menu.getContent());
        return "menu";
    }

    @RequestMapping("/menuItems/new")
    public String newMenuItem(Model model) throws Exception {
        model.addAttribute(new MenuItem());
        return "menuItem";
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST, value = "/menuItems/new/")
    public String saveNewMenuItem(@ModelAttribute MenuItem menuItem) throws Exception {
        restTemplate
                .postForEntity("//menu-service/menuItems/", menuItem, Void.class);
        return "redirect:..";
    }

    @HystrixCommand
    @RequestMapping("/menuItems/{id}")
    public String viewMenuItem(Model model, @PathVariable String id) throws Exception {
        Resource<MenuItem> item = restTemplate
                .exchange(
                        "//menu-service/menuItems/{id}",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<Resource<MenuItem>>() {
                        }, id)
                .getBody();
        model.addAttribute("menuItem", item.getContent());
        return "menuItem";
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST, value = "/menuItems/{id}")
    public String saveMenuItem(@PathVariable String id, @ModelAttribute MenuItem menuItem) throws Exception {
        restTemplate.put("//menu-service/menuItems/{id}", menuItem, id);
        return "redirect:..";
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST, value = "/menuItems/{id}/delete")
    public String deleteMenuItem(@PathVariable String id, @ModelAttribute MenuItem menuItem) throws Exception {
        restTemplate.delete("//menu-service/menuItems/{id}", id);
        return "redirect:..";
    }

    @HystrixCommand
    @RequestMapping("/orders/")
    public String viewOrders(Model model) {
        PagedResources<Order> orders = restTemplate
                .exchange(
                        "//order-service/orders",
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<PagedResources<Order>>() {
                        })
                .getBody();
        model.addAttribute("orders", orders.getContent());
        return "orders";
    }

    @HystrixCommand
    @RequestMapping(method = RequestMethod.POST, value = "/orders/{id}/delete")
    public String deleteOrder(@PathVariable String id) {
        restTemplate.delete("//order-service/orders/{id}", id);
        return "redirect:..";
    }

}
