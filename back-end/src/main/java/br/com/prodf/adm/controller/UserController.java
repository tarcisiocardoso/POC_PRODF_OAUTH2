package br.com.prodf.adm.controller;

import br.com.prodf.adm.exception.ResourceNotFoundException;
import br.com.prodf.adm.model.User;
import br.com.prodf.adm.repository.UserRepository;
import br.com.prodf.adm.security.CurrentUser;
import br.com.prodf.adm.security.UserPrincipal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user/me")
//    @PreAuthorize("hasRole('managers')")
    public User getCurrentUser(@CurrentUser UserDetails userPrincipal) {
    	System.out.println("userPrincipal: "+ userPrincipal );
    	
        return userRepository.findById(userPrincipal.getUsername() )
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getUsername() ));

        // User user = userRepository.findById(userPrincipal.getId());
        // if( user == null ){
        //     throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        // }
        // return user;
    }

    @GetMapping("/api/login/{login}, method = RequestMethod.GET")
    @PreAuthorize("hasRole('USER')")
    public User getLogin(@PathVariable("login") String login) {
        // return userRepository.findById(userPrincipal.getId())
        //         .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        User user = userRepository.findByLogin(login);
        if( user == null ){
            throw new ResourceNotFoundException("User", "id", login);
        }
        return user;
    }

    @PostMapping("/api/formMinhaConta")
    public void criarConta(@Valid @RequestBody User user){
        System.out.println(">>>>criarConta<<<<");
        System.out.println( user );

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);
    }

    @PostMapping("/api/perfil")
    public void trocarPerfil(@Valid @RequestBody User user){       

        User u = userRepository.findById(user.getId()).get();

        u.perfis = user.perfis;      
       
    
        userRepository.save(u);
    }

    @PostMapping("/api/trocaSenha")
    public void trocaSenha(@Valid @RequestBody User user){
        System.out.println(">>>>LALALALA<<<<"+ user.getLogin()+user.getPassword());
        System.out.println( user );


        User obj = userRepository.findByLogin(user.getLogin());

        obj.setPassword(user.getPassword());


        obj.setPassword(passwordEncoder.encode(user.getPassword()));


        System.out.println(">>>>>>>>>>>>>>>>2<<<<<<<<<<<<<<<<<<<<<<");
        userRepository.save(obj);
    }

}
