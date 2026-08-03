package brenda.pawfinder.dto;

import brenda.pawfinder.enums.MatchState;
import brenda.pawfinder.enums.PetState;

public record MatchCardResponseDTO(Long id, Long matchedPetId, String imageUrl, PetState state,
                String city,
                MatchState matchState, Integer score) {

}
