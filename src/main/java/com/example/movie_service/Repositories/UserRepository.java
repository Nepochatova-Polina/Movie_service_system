package com.example.movie_service.Repositories;

import com.example.movie_service.Entities.UserEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByNickName(String nickName);

    @NonNull
    @Modifying
    @Query(value = "select * from users", nativeQuery = true)
    List<User> findAll();

    @Transactional
    @Modifying
    @Query(value = """
                   insert into users(first_name, last_name, nick_name,date_of_birth, password)
                   values (?,?,?,?,?)""", nativeQuery = true)
    void addUser(String firstName, String lastName, String nickName, String birthday,String password);

    @Transactional
    @Modifying
    @Query(value = """
                   insert into user_role(user_id, role)
                   values (?,?)""", nativeQuery = true)
    void addUserRole(long user_id,String role);
}
