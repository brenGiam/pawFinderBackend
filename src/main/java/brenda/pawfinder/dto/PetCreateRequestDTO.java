package brenda.pawfinder.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import brenda.pawfinder.enums.PetGender;
import brenda.pawfinder.enums.PetState;
import brenda.pawfinder.enums.Specie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PetCreateRequestDTO(
                @NotBlank(message = "Ingrese especie") Specie specie,
                @NotBlank(message = "Ingrese estado (perdido/encontrado)") PetState state,
                String name,
                @NotNull(message = "Indique si tenía collar") Boolean withCollar,
                String breed,
                @NotEmpty(message = "Debe seleccionar al menos un color") List<String> colors,
                String details,
                @NotBlank(message = "Ingrese provincia") String province,
                @NotBlank(message = "Ingrese ciudad") String city,
                @NotBlank(message = "Ingrese género") PetGender gender,
                String neighborhood,
                MultipartFile image,
                Long userId) {

}
