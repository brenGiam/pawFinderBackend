package brenda.pawfinder.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import brenda.pawfinder.enums.MatchState;
import brenda.pawfinder.exception.MatchUserStateNotFoundException;
import brenda.pawfinder.model.MatchUserState;
import brenda.pawfinder.repository.MatchUserStateRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MatchUserStateService {

    private final MatchUserStateRepository matchUserStateRepository;

    @Transactional
    public void updateMatchUserState(Long userId, Long matchId, MatchState newState) {
        MatchUserState matchUserState = matchUserStateRepository.findByUserIdAndMatchId(userId, matchId)
                .orElseThrow(() -> new MatchUserStateNotFoundException(
                        "Estado no encontrado para el usuario: " + userId + " con match: " + matchId));
        matchUserState.setState(newState);
    }
}
