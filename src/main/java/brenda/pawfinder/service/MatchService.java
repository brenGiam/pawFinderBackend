package brenda.pawfinder.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import brenda.pawfinder.dto.MatchCardResponseDTO;
import brenda.pawfinder.enums.MatchState;
import brenda.pawfinder.enums.PetState;
import brenda.pawfinder.exception.PetNotFoundException;
import brenda.pawfinder.model.Match;
import brenda.pawfinder.model.MatchUserState;
import brenda.pawfinder.model.Pet;
import brenda.pawfinder.repository.MatchRepository;
import brenda.pawfinder.repository.PetRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final MatchRepository matchRepository;
    private final PetRepository petRepository;
    private static final int MINIMUM_SCORE = 5; // Umbral de compatibilidad

    @Transactional
    public void createMatch(Long newPetId) {

        Pet newPet = petRepository.findById(newPetId)
                .orElseThrow(() -> new PetNotFoundException("Mascota no encontrada"));

        // Buscar mascotas con estado opuesto (PERDIDA <-> ENCONTRADA)
        PetState oppositeState = newPet.getState() == PetState.PERDIDO ? PetState.ENCONTRADO : PetState.PERDIDO;

        // Filtrar mascotas candidatas según especie, estado, provincia, ciudad y género
        // y con diferente id para que no haga match consigo misma
        List<Pet> possibleMatches = petRepository.searchPossibleMatches(
                newPet.getId(),
                newPet.getSpecie(),
                oppositeState,
                newPet.getProvince(),
                newPet.getCity(),
                newPet.getGender());

        List<Match> createdMatches = new ArrayList<>();

        for (Pet candidate : possibleMatches) {

            Integer score = calculateScore(newPet, candidate);

            if (score >= MINIMUM_SCORE) {

                if (!matchRepository.existsByPet(newPet.getId(), candidate.getId())) {

                    // Crear el Match solamente cuando supera el umbral y todavía no existe.
                    Match match = Match.builder()
                            .newPet(newPet)
                            .matchedPet(candidate)
                            .score(score)
                            .build();

                    // Estado del dueño de la mascota recién publicada
                    MatchUserState ownerNewPet = MatchUserState.builder()
                            .user(newPet.getUser())
                            .build();

                    // Estado del dueño de la mascota candidata
                    MatchUserState ownerCandidate = MatchUserState.builder()
                            .user(candidate.getUser())
                            .build();

                    // Sincronizar ambos lados de la relación
                    match.addUserState(ownerNewPet);
                    match.addUserState(ownerCandidate);

                    createdMatches.add(matchRepository.save(match));
                }
            }
        }
    }

    public Page<MatchCardResponseDTO> getAllMatchesByPet(Long petId,
            Pageable pageable) {

        Pet pet = petRepository.findById(petId).orElseThrow(() -> new PetNotFoundException("Mascota no encontrada"));
        Long ownerId = pet.getUser().getId();

        Page<Match> matches = matchRepository.searchMatches(petId, pageable);

        return matches.map(match -> {

            Pet otherPet = getOtherPet(match, petId);

            return new MatchCardResponseDTO(
                    match.getId(),
                    otherPet.getId(),
                    otherPet.getImage() != null ? otherPet.getImage().getUrl() : null,
                    otherPet.getState(),
                    otherPet.getCity(),
                    determineMatchState(match, ownerId),
                    match.getScore());
        });
    }

    // Métodos auxiliares
    private Integer calculateScore(Pet newPet, Pet candidate) {
        Integer score = 0;

        // Filtrado más en detalle que aumenta el criterio del match
        if (compatibleBreed(newPet.getBreed(), candidate.getBreed())) {
            score += 3;
        }

        if (compatibleColors(newPet.getColors(), candidate.getColors())) {
            score += 2;
        }

        if (newPet.getWithCollar().equals(candidate.getWithCollar())) {
            score += 1;
        }

        return score;
    }

    private boolean compatibleBreed(String breed1, String breed2) {
        if (breed1 == null || breed2 == null)
            return false;

        if (breed1.equalsIgnoreCase(breed2))
            return true;

        return breed1.equalsIgnoreCase("SIN RAZA") || breed2.equalsIgnoreCase("SIN RAZA");

    }

    private boolean compatibleColors(List<String> colors1, List<String> colors2) {
        if (colors1 == null || colors2 == null || colors1.isEmpty() || colors2.isEmpty()) {
            return false;
        }

        return colors1.stream()
                .anyMatch(color1 -> colors2.stream()
                        .anyMatch(color2 -> color1.equalsIgnoreCase(color2)));
    }

    private Pet getOtherPet(Match match, Long petId) {
        if (match.getNewPet().getId().equals(petId)) {
            return match.getMatchedPet();
        } else {
            return match.getNewPet();
        }
    }

    private MatchState determineMatchState(Match match, Long ownerId) {

        return match.getUserStates().stream()
                .filter(userState -> userState.getUser().getId().equals(ownerId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No se encontró el estado del usuario para este match"))
                .getState();
    }
}
