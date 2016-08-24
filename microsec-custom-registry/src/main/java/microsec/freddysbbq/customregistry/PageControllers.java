package microsec.freddysbbq.customregistry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import microsec.freddysbbq.customregistry.provider.AuthenticatedMainframeUser;
import microsec.freddysbbq.customregistry.util.JacksonUtils;

@Controller
public class PageControllers {
	
	@RequestMapping("/login")
	public String login()
	{
		return "login"; 
	}
	
//	@RequestMapping("/")
//	@ResponseBody
//	public AuthenticatedMainframeUser root(AuthenticatedMainframeUser user)
//	{
//		return user; 
//	}
	
	@RequestMapping("/")
	public String root(Model model, AuthenticatedMainframeUser user)
	{
		model.addAttribute("userJson", JacksonUtils.toJSON(user));
		return "home"; 
	}
}
