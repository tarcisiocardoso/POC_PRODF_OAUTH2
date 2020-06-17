package br.com.prodf.adm.security;


import br.com.prodf.adm.exception.ResourceNotFoundException;
import br.com.prodf.adm.model.User;
import br.com.prodf.adm.repository.UserRepository;
import br.com.prodf.adm.security.jwt.Person;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.support.LdapUtils;
import javax.naming.ldap.LdapName;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    
	@Autowired
	LdapTemplate ldapTemplate;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {
                
                System.out.println(">>CustomUserDetailsService - loadUserByUsername<< 1: "+login);
        // User user = userRepository.findByEmail(login)
        //         .orElseThrow(() ->
        //                 new UsernameNotFoundException("User not found with email : " + login)
        // );
        
        User user = userRepository.findByLogin(login);
        System.out.println(">>CustomUserDetailsService - loadUserByUsername<< 2" + user );
        if( user == null ){
            System.out.println(">>CustomUserDetailsService - loadUserByUsername<< 3");
            throw new UsernameNotFoundException("User not found with email : " + login);
            
        }
        System.out.println(">>CustomUserDetailsService - loadUserByUsername<< 5");
        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
//        User user = userRepository.findById(id).orElseThrow(
//            () -> new ResourceNotFoundException("User", "id", id)
//        );
//
		List<Person> lstP = findByUID(id);

		lstP.forEach(p -> System.out.println(p));
    	
//        return UserPrincipal.create( new User(id, "", new ArrayList<>()) );
		
		return new org.springframework.security.core.userdetails.User(id, "", new ArrayList<>());
    }
    private List<Person> findByUID(String uid) {
		return ldapTemplate.search(query()
                .where("uid").is(uid),
                PERSON_CONTEXT_MAPPER);
	}
    
    private final static ContextMapper<Person> PERSON_CONTEXT_MAPPER = new AbstractContextMapper<Person>() {
        @Override
		public Person doMapFromContext(DirContextOperations context) {
			Person person = new Person();

            LdapName dn = LdapUtils.newLdapName(context.getDn());
			person.setCountry(LdapUtils.getStringValue(dn, 0));
			person.setCompany(LdapUtils.getStringValue(dn, 1));
			person.setFullName(context.getStringAttribute("cn"));
			person.setLastName(context.getStringAttribute("sn"));
			person.setDescription(context.getStringAttribute("description"));
			person.setPhone(context.getStringAttribute("telephoneNumber"));

			return person;
		}
	};
}