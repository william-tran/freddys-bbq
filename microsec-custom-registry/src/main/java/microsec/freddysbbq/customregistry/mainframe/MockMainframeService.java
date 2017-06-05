package microsec.freddysbbq.customregistry.mainframe;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class MockMainframeService implements MainframeUserService {

	private Map<String,MainframeUser> users = new HashMap<>();
	
	@PostConstruct
	private void init()
	{
		// setup frank
		MainframeUser frank = new MainframeUser();
		frank.setFirstName("Frank");
		frank.setLastName("Underwood");
		frank.setUsername("frank");
		frank.setPassword("demo");
		frank.setEmail("frank@whitehouse.gov");
		frank.setEmployee(false);
		users.put("frank", frank);
		
		// setup freddy
		MainframeUser freddy = new MainframeUser();
		freddy.setUsername("freddy");
		freddy.setPassword("demo");
		freddy.setFirstName("Freddy");
		freddy.setLastName("Hayes");
		freddy.setEmail("freddy@freddybbq.com");
		freddy.setEmployee(true);
		
		users.put("freddy", freddy);
		
	}
	
	@Override
	public Optional<MainframeUser> getUserDetails(String username) {
		return Optional.ofNullable(this.users.get(username));
	}

}
