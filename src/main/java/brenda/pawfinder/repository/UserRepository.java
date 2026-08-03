package brenda.pawfinder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import brenda.pawfinder.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMail(String mail);

    boolean existsByMail(String mail);

}
