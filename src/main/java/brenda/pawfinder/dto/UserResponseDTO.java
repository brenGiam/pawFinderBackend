package brenda.pawfinder.dto;

public record UserResponseDTO(
                Long id,
                String name,
                String lastname,
                String mail,
                String province,
                String city,
                String phone) {
}