package brenda.pawfinder.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import brenda.pawfinder.model.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {

        @Query("SELECT COUNT(m) > 0 FROM Match m WHERE " +
                        "(m.newPet.id = :newPetId AND m.matchedPet.id = :matchedPetId) OR " +
                        "(m.newPet.id = :matchedPetId AND m.matchedPet.id = :newPetId)")
        boolean existsByPet(@Param("newPetId") Long newPetId,
                        @Param("matchedPetId") Long matchedPetId);

        @Query("SELECT m FROM Match m WHERE " +
                        "m.newPet.id = :petId OR " +
                        "m.matchedPet.id = :petId " +
                        "ORDER BY m.score DESC")
        Page<Match> searchMatches(
                        @Param("petId") Long petId,
                        Pageable pageable);

}
