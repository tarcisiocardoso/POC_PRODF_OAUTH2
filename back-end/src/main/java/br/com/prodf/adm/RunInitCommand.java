package br.com.prodf.adm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.gson.Gson;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import br.com.prodf.adm.model.User;
import br.com.prodf.adm.service.UserService;

@Component
public class RunInitCommand implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ResourceLoader resourceLoader;
    Gson g = new Gson();

    @Override
    public void run(String... args) throws Exception {
        System.out.println( ">>>>RunInitCommand<<<<" );
        
        populaUserAdmSeNaoExiste();

        System.out.println( ">>>>FIM<<<<" );
    }

    private void populaUserAdmSeNaoExiste() {
        User user = userService.findByLogin("adm");
        if( user == null ){
            String json = loadRecurso("userAdm.json");
            user = g.fromJson(json, User.class);
            user.setId("123456");    
            System.out.println(">>>>>>>>>>>>>>>>"+ user.getPassword() );
            // System.out.println(passwordEncoder.encode(user.getPassword()));

            user.setPassword( passwordEncoder.encode(user.getPassword()));
            user = userService.create(user);
        }
    }

    private String loadRecurso(String recurso) {
        Resource rs = resourceLoader.getResource("classpath:data/"+recurso);
        String ret = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(rs.getInputStream()));
            while (reader.ready()) {
                ret += "\n"+ reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

}