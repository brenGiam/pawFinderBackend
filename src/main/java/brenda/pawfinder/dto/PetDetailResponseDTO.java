package brenda.pawfinder.dto;

import java.time.LocalDate;

import brenda.pawfinder.enums.PetGender;
import brenda.pawfinder.enums.PetState;
import brenda.pawfinder.enums.Specie;

public record PetDetailResponseDTO(
        Long id,
        String name,
        Specie specie,
        PetState state,
        boolean withCollar,
        String breed,
        String province,
        String city,
        PetGender gender,
        String details,
        String neighborhood,
        String imageUrl,
        LocalDate registrationDate,
        String userName,
        String userLastName,
        String phone

) {

}
