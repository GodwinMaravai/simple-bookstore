package be.kata.persistence.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query(value = "select u.* from T_USER u where u.name = :name", nativeQuery = true)
    UserEntity findUserEntityByName(@Param("name") String name);
}
