package brenda.pawfinder.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import brenda.pawfinder.enums.PetGender;
import brenda.pawfinder.enums.PetState;
import brenda.pawfinder.enums.Species;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Species specie; // perro/gato, con radio button

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_state", nullable = false)
    private PetState state; // perdido/encontrado, con radio button

    private String name; // lo dejo como opcional porque si es una mascota encontrada, no van a saber el
                         // nombre

    @Column(name = "with_collar", nullable = false)
    private boolean withCollar; // debería estar en NO como predeterminado, con radio button

    private String breed; // debería estar en SIN RAZA como predeterminado, lista desplegable con todas
                          // las razas

    @ElementCollection
    @CollectionTable(name = "pet_color", joinColumns = @JoinColumn(name = "pet_id"))
    @Column(name = "color", nullable = false)
    private List<String> colors = new ArrayList<>(); // el usuario puede elegir varios colores, con checkbox

    private String details; // el usuario puede escribir todas las características que crea necesarias

    @Column(nullable = false)
    private String province; // lista desplegable con provincias

    @Column(nullable = false)
    private String city; // lista desplegable con ciudades, que se actualiza según la provincia
                         // seleccionada

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetGender gender;

    private String neighborhood; // barrio en el que se perdió o fue encontrado

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "deleted_at")
    private LocalDate deletedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) // por el momento voy a permitir subir una sola foto
                                                                // pero lo dejo asi para futuro
    @JoinColumn(name = "pet_id")
    private List<Image> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "uploadedPet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Match> matches = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.registrationDate = LocalDate.now();
        if (this.breed == null || this.breed.isBlank())
            this.breed = "SIN RAZA";
    }

    public void softDelete() {
        this.active = false;
        this.deletedAt = LocalDate.now();
    }
}
