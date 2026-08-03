package brenda.pawfinder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import brenda.pawfinder.model.MatchUserState;

public interface MatchUserStateRepository extends JpaRepository<MatchUserState, Long> {

    Optional<MatchUserState> findByUserIdAndMatchId(Long userId, Long matchId);
}
