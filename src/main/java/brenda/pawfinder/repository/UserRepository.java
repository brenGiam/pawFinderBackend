package brenda.pawfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import brenda.pawfinder.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
