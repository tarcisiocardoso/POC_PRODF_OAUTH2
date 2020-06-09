package br.com.prodf.adm.service;

import java.util.List;
import java.util.Optional;

import br.com.prodf.adm.model.User;

public interface IUserService {
    User create(User o);
    Optional<User> findById(String id);
    User findByLogin(String login);
    List<User> findAll();
    User update(User o);
    void delete(Long id);
}