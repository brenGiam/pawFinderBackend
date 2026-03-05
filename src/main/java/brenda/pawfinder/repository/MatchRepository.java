package brenda.pawfinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import brenda.pawfinder.model.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {

}
