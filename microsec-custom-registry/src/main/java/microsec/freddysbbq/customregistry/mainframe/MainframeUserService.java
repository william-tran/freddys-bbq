package microsec.freddysbbq.customregistry.mainframe;

import java.util.Optional;

public interface MainframeUserService {
	public Optional<MainframeUser> getUserDetails(String username);
}
