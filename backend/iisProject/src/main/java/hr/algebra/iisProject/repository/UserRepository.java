package hr.algebra.iisProject.repository;

import hr.algebra.iisProject.model.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo, Long> {
    UserInfo findByUsername(String username);
}
