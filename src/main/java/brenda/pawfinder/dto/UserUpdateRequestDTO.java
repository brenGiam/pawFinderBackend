package brenda.pawfinder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserUpdateRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String name,

        @NotBlank(message = "El apellido es obligatorio") String lastname,

        @NotBlank(message = "La provincia es obligatoria") String province,

        @NotBlank(message = "La ciudad es obligatoria") String city,

        @NotBlank(message = "El teléfono es obligatorio") @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "El teléfono tiene un formato inválido") String phone) {
}