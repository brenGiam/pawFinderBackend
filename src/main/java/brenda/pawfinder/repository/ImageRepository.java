package brenda.pawfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import brenda.pawfinder.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
