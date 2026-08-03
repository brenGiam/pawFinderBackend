package brenda.pawfinder.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDTO(
                @NotBlank(message = "El nombre es obligatorio") String name,

                @NotBlank(message = "El apellido es obligatorio") String lastname,

                @NotBlank(message = "El correo es obligatorio") @Email(message = "El correo no tiene un formato válido") String mail,

                @NotBlank(message = "La contraseña es obligatoria") @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres") String password,

                @NotBlank(message = "La provincia es obligatoria") String province,

                @NotBlank(message = "La ciudad es obligatoria") String city,

                @NotBlank(message = "El teléfono es obligatorio") @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "El teléfono tiene un formato inválido") String phone) {
}