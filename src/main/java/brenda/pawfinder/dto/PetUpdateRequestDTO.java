package brenda.pawfinder.dto;

import java.util.List;

import brenda.pawfinder.enums.PetGender;
import brenda.pawfinder.enums.PetState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PetUpdateRequestDTO(
                @NotBlank(message = "Ingrese estado") PetState state,
                String name,
                @NotNull(message = "Indique si tenía collar") Boolean withCollar,
                String breed,
                @NotEmpty(message = "Debe seleccionar al menos un color") List<String> colors,
                String details,
                @NotBlank(message = "Ingrese provincia") String province,
                @NotBlank(message = "Ingrese ciudad") String city,
                @NotBlank(message = "Ingrese género") PetGender gender,
                String neighborhood) {
}