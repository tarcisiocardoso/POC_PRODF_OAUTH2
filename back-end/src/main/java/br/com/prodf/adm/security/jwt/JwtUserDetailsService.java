package br.com.prodf.adm.security.jwt;


import java.util.ArrayList;
import java.util.List;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import javax.naming.directory.Attributes;
import javax.naming.NamingException;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;

import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.support.LdapUtils;
import javax.naming.ldap.LdapName;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Autowired
	LdapTemplate ldapTemplate;

	@SuppressWarnings({ "rawtypes", "serial", "unchecked" })
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println(">>>JwtUserDetailsService.loadUserByUsername<<<");

		if( username != null && !username.isEmpty() ){
			// List lst = getPersonNamesByLastName(username);
			// lst.forEach(item -> System.out.println(item));

			List<Person> lstP = findByUID(username);

			lstP.forEach(p -> System.out.println(p));
		}
		
//		GrantedAuthorityDefaults g = new GrantedAuthorityDefaults(new SimpleGrantedAuthority("ROLE_USER") );

		
		ArrayList lst = new ArrayList<GrantedAuthority>() {{
			add( new SimpleGrantedAuthority("ROLE_USER") );
		}};
		return new User(username, "", lst);

		

		// if ("springuser".equals(username)) {
		// // if( "ben".equals(username)){
		// 	return new User("springuser", "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6",
		// 			new ArrayList<>());
		// }else if ("ben".equals(username)) {
		// 	// if( "ben".equals(username)){
		// 		return new User("ben", "$2a$10$c6bSeWPhg06xB1lvmaWNNe4NROmZiSpYhlocU/98HNr2MhIOiSt36",
		// 				new ArrayList<>());
		// } else {
		// 	throw new UsernameNotFoundException("User not found with username: " + username);
		// }
	}

	private List<Person> findAll() {
		return ldapTemplate.search(query()
				.where("objectclass").is("person"),
                PERSON_CONTEXT_MAPPER);
	}

	private List<Person> findByUID(String uid) {
		return ldapTemplate.search(query()
                .where("uid").is(uid),
                PERSON_CONTEXT_MAPPER);
	}

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List getPersonNamesByLastName(String uid) {
		AndFilter filter = new AndFilter();
		filter.and(new EqualsFilter("objectclass", "person"));
		filter.and(new EqualsFilter("uid", uid));
		return ldapTemplate.search(
		   "", filter.encode(),
		   new AttributesMapper() {
			  public Object mapFromAttributes(Attributes attrs)
				 throws NamingException {
				 return attrs.get("cn").get();
			  }
		   });
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
