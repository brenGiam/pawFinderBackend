package brenda.pawfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import brenda.pawfinder.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

}
