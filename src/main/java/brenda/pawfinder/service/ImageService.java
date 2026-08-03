package brenda.pawfinder.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import brenda.pawfinder.exception.ImageDeleteException;
import brenda.pawfinder.exception.ImageUploadException;
import brenda.pawfinder.model.Image;
import brenda.pawfinder.repository.ImageRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final Cloudinary cloudinary;
    private final ImageRepository imageRepository;

    @Transactional
    public Image uploadImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar una imagen");
        }

        try {
            Map<?, ?> response = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.emptyMap());

            return imageRepository.save(
                    Image.builder()
                            .url((String) response.get("secure_url"))
                            .publicId((String) response.get("public_id"))
                            .build());

        } catch (IOException e) {
            throw new ImageUploadException("No se pudo subir la imagen a Cloudinary", e);
        }
    }

    @Transactional
    public void deleteImage(Image image) {

        if (image == null) {
            return;
        }

        try {
            cloudinary.uploader().destroy(
                    image.getPublicId(),
                    ObjectUtils.emptyMap());

        } catch (IOException e) {
            throw new ImageDeleteException("No se pudo eliminar la imagen de Cloudinary", e);
        }
    }
}
