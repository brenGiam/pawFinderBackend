package brenda.pawfinder.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import brenda.pawfinder.dto.UserCreateRequestDTO;
import brenda.pawfinder.dto.UserResponseDTO;
import brenda.pawfinder.dto.UserUpdateRequestDTO;
import brenda.pawfinder.exception.UserAlreadyExistsException;
import brenda.pawfinder.exception.UserNotFoundException;
import brenda.pawfinder.model.User;
import brenda.pawfinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO createUser(UserCreateRequestDTO userDTO) {

        if (userRepository.existsByMail(userDTO.mail())) {
            throw new UserAlreadyExistsException("Usuario ya registrado con el correo: " + userDTO.mail());
        }

        User user = User.builder()
                .name(userDTO.name())
                .lastname(userDTO.lastname())
                .mail(userDTO.mail())
                .password(passwordEncoder.encode(userDTO.password()))
                .province(userDTO.province())
                .city(userDTO.city())
                .phone(userDTO.phone())
                .build();

        user = userRepository.save(user);

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getMail(),
                user.getProvince(),
                user.getCity(),
                user.getPhone());
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO userDTO) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));

        user.setName(userDTO.name());
        user.setLastname(userDTO.lastname());
        user.setProvince(userDTO.province());
        user.setCity(userDTO.city());
        user.setPhone(userDTO.phone());

        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getLastname(),
                user.getMail(),
                user.getProvince(),
                user.getCity(),
                user.getPhone());
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }
}
