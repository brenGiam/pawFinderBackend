package brenda.pawfinder.dto;

import brenda.pawfinder.enums.PetGender;
import brenda.pawfinder.enums.PetState;
import brenda.pawfinder.enums.Specie;

public record PetCardResponseDTO(
                Long id,
                String name,
                Specie specie,
                PetState state,
                String province,
                String city,
                PetGender gender,
                String imageUrl) {
}