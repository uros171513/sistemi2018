package com.sbnz.sbnzproject.serviceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbnz.sbnzproject.SbnzprojectApplication;
import com.sbnz.sbnzproject.model.Disease;
import com.sbnz.sbnzproject.model.User;
import com.sbnz.sbnzproject.repository.DiseaseRepository;
import com.sbnz.sbnzproject.repository.UserRepository;
import com.sbnz.sbnzproject.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	DiseaseRepository diseaseRepository;

	
	private final KieContainer kieContainer;
	
    @Autowired
    public UserServiceImpl(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }
	
	
	@Override
	public User create(User user) {
        return userRepository.save(user);
	}

	@Override
	public Collection<User> getAll() {
		 return userRepository.findAll();
	}

	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public Boolean login(String username, String password) {
		User user = findByUsername(username);
		if(user != null) {
			KieServices ks = KieServices.Factory.get();
			KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
			kbconf.setOption(EventProcessingOption.STREAM);
			KieBase kbase = kieContainer.newKieBase(kbconf);
			KieSession kieSession = kbase.newKieSession();

    		if(!SbnzprojectApplication.kieSessions.containsKey("kieSession-"+username)) {
    			List<Disease> diseases=diseaseRepository.findAll();
        		for(Disease d:diseases) {	
        			kieSession.insert(d);
        		}
    			SbnzprojectApplication.kieSessions.put("kieSession-"+username, kieSession);
    		}
    		if(!SbnzprojectApplication.users.containsKey("currentUser-"+username))
    			SbnzprojectApplication.users.put("currentUser-"+username, user);
    		

    		return true;
		}
		return false;
	}


	@Override
	public Boolean logout(String username) {
		SbnzprojectApplication.kieSessions.remove("kieSession-"+username);
		SbnzprojectApplication.users.remove("currentUser-"+username);
		if(SbnzprojectApplication.kieSessions.get("kieSession-"+username)==null &&
				SbnzprojectApplication.users.get("currentUser-"+username)==null)
			return true;
		return false;
	}


	@Override
	public void delete(Long id) {
		userRepository.delete(id);	
	}

}
