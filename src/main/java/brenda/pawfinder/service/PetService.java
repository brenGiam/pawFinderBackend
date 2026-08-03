package brenda.pawfinder.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import brenda.pawfinder.dto.PetCreateRequestDTO;
import brenda.pawfinder.dto.PetDetailResponseDTO;
import brenda.pawfinder.dto.PetCardResponseDTO;
import brenda.pawfinder.dto.PetResponseDTO;
import brenda.pawfinder.dto.PetUpdateRequestDTO;
import brenda.pawfinder.enums.PetState;
import brenda.pawfinder.enums.Specie;
import brenda.pawfinder.exception.ImageRequiredException;
import brenda.pawfinder.exception.PetNotFoundException;
import brenda.pawfinder.exception.UserNotFoundException;
import brenda.pawfinder.model.Image;
import brenda.pawfinder.model.Pet;
import brenda.pawfinder.model.User;
import brenda.pawfinder.repository.PetRepository;
import brenda.pawfinder.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {
        private final PetRepository petRepository;
        private final UserRepository userRepository;
        private final ImageService imageService;

        @Transactional
        public PetResponseDTO createPet(Long userId, PetCreateRequestDTO petDTO) {

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

                Image image = null;
                if (petDTO.image() != null && !petDTO.image().isEmpty()) {
                        image = imageService.uploadImage(petDTO.image());
                }

                Pet pet = Pet.builder()
                                .specie(petDTO.specie())
                                .state(petDTO.state())
                                .name(petDTO.name()) // Normalizar nombre??
                                .withCollar(petDTO.withCollar())
                                .breed(petDTO.breed())
                                .colors(petDTO.colors())
                                .details(petDTO.details()) // Normalizar detalles??
                                .province(petDTO.province())
                                .city(petDTO.city())
                                .gender(petDTO.gender())
                                .neighborhood(petDTO.neighborhood())// Normalizar barrio??
                                .image(image)
                                .user(user)
                                .build();

                pet = petRepository.save(pet);

                return new PetResponseDTO(pet.getId(), pet.getName(), pet.getSpecie(), pet.getState(),
                                pet.getProvince(),
                                pet.getCity());
        }

        @Transactional
        public PetResponseDTO updatePet(Long petId, PetUpdateRequestDTO petDTO) {

                Pet pet = petRepository.findById(petId)
                                .orElseThrow(() -> new PetNotFoundException("Mascota no encontrada"));

                pet.setState(petDTO.state());
                pet.setName(petDTO.name()); // Normalizar nombre??
                pet.setWithCollar(petDTO.withCollar());
                pet.setBreed(petDTO.breed());
                pet.setColors(petDTO.colors());
                pet.setDetails(petDTO.details()); // Normalizar detalles??
                pet.setProvince(petDTO.province());
                pet.setCity(petDTO.city());
                pet.setGender(petDTO.gender());
                pet.setNeighborhood(petDTO.neighborhood());// Normalizar barrio??

                return new PetResponseDTO(pet.getId(), pet.getName(), pet.getSpecie(), pet.getState(),
                                pet.getProvince(),
                                pet.getCity());
        }

        @Transactional
        public void updatePetImage(Long petId, MultipartFile imageFile) {

                Pet pet = petRepository.findById(petId)
                                .orElseThrow(() -> new PetNotFoundException("Mascota no encontrada"));

                if (imageFile == null || imageFile.isEmpty()) {
                        throw new ImageRequiredException("Debe seleccionar una imagen");
                }

                // Conservar referencia a la imagen anterior
                Image oldImage = pet.getImage();

                // Subir la nueva imagen
                Image newImage = imageService.uploadImage(imageFile);

                // Asociarla a la mascota
                pet.setImage(newImage);

                // Eliminar la imagen anterior (si existía)
                if (oldImage != null) {
                        imageService.deleteImage(oldImage);
                }
        }

        @Transactional
        public void deletePet(Long petId) {
                Pet pet = petRepository.findById(petId)
                                .orElseThrow(() -> new PetNotFoundException("Mascota no encontrada"));

                if (pet.getImage() != null) {
                        imageService.deleteImage(pet.getImage());
                }
                petRepository.delete(pet);
        }

        public Page<PetCardResponseDTO> getAllPetsWithFilters(
                        Specie specie,
                        PetState state,
                        String province,
                        String city,
                        Pageable pageable) {
                return petRepository.searchWithFilters(specie, state, province, city, pageable);
        }

        public PetDetailResponseDTO getPet(Long petId) {
                Pet pet = petRepository.findById(petId)
                                .orElseThrow(() -> new PetNotFoundException("Mascota no encontrada"));

                return new PetDetailResponseDTO(
                                pet.getId(),
                                pet.getName(),
                                pet.getSpecie(),
                                pet.getState(),
                                pet.getWithCollar(),
                                pet.getBreed(),
                                pet.getProvince(),
                                pet.getCity(),
                                pet.getGender(),
                                pet.getDetails(),
                                pet.getNeighborhood(),
                                pet.getImage() != null ? pet.getImage().getUrl() : null,
                                pet.getRegistrationDate(),
                                pet.getUser().getName(),
                                pet.getUser().getLastname(),
                                pet.getUser().getPhone());
        }

}
