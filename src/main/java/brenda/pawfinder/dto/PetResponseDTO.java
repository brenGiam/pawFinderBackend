package brenda.pawfinder.dto;

import brenda.pawfinder.enums.PetState;
import brenda.pawfinder.enums.Specie;

public record PetResponseDTO(
        Long id,
        String name,
        Specie specie,
        PetState state,
        String province,
        String city) {

}
