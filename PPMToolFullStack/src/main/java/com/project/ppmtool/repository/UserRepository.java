package com.project.ppmtool.repository;

import com.project.ppmtool.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUserName(String username);

    User getById(Long id);
}
