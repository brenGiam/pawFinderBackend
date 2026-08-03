package brenda.pawfinder.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import brenda.pawfinder.dto.PetCardResponseDTO;
import brenda.pawfinder.enums.PetGender;
import brenda.pawfinder.enums.PetState;
import brenda.pawfinder.enums.Specie;
import brenda.pawfinder.model.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {

  @Query("""
          SELECT new com.brenda.pawfinder.dto.PetCardResponseDTO(p.id, p.name, p.specie, p.state, p.province, p.city, p.gender, i.url)
          FROM Pet p
          LEFT JOIN p.image i
          WHERE p.state <> 'REENCONTRADO'
            AND (:specie IS NULL OR p.specie = :specie)
            AND (:state IS NULL OR p.state = :state)
            AND (:province IS NULL OR p.province = :province)
            AND (:city IS NULL OR p.city = :city)
            AND (p.active = true)
          ORDER BY p.registrationDate DESC
      """)
  Page<PetCardResponseDTO> searchWithFilters(
      @Param("specie") Specie specie,
      @Param("state") PetState state,
      @Param("province") String province,
      @Param("city") String city,
      Pageable pageable);

  @Query("SELECT p FROM Pet p WHERE " +
      "p.id <> :newPetId AND " +
      "p.specie = :specie AND " +
      "p.state = :state AND " +
      "p.province = :province AND " +
      "p.city = :city AND " +
      "p.gender = :gender AND " +
      "p.active = true")
  List<Pet> searchPossibleMatches(
      @Param("newPetId") Long newPetId,
      @Param("specie") Specie specie,
      @Param("state") PetState state,
      @Param("province") String province,
      @Param("city") String city,
      @Param("gender") PetGender gender);

}
