package br.com.prodf.adm.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.prodf.adm.repository.UserRepository;

@RestController
@RequestMapping("/api/menu")
public class MenuController{

    @Autowired
    private UserRepository userRepository;

    
    @RequestMapping(method = RequestMethod.GET, value="/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    public List<Menu> montaMenu( @PathVariable("id") String id ) {

        List<Menu> lst = new ArrayList<Menu>();
        
        // lst.add(new Menu("Inicio", "home", "home" ));
        lst.add(new Menu("Linha do tempo", "timeline", "about" ));
        // lst.add(new Menu("Menu tres", "trending_up", "topics" ));
        lst.add(new Menu("Adicionar uma nota", "note_add", "blog" ));

        return lst;
    }

    private class Menu{
        public String nome;
        public String icon;
        public String acao;

        public Menu(String n, String i, String a){
            nome = n; icon =i; acao = a;
        }

        public String toString(){
            return "{nome: "+nome+", icon: "+icon+", acao: "+acao+"}";
        }
    }
}