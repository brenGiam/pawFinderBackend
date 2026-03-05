package brenda.pawfinder.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "matches", uniqueConstraints = @UniqueConstraint(columnNames = { "uploaded_pet_id", "matched_pet_id" }))
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploaded_pet_id", nullable = false)
    private Pet uploadedPet;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matched_pet_id", nullable = false)
    private Pet matchedPet;

    @Builder.Default
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchUserState> userStates = new ArrayList<>();

    private LocalDate date;

    private Integer score; // puntuación de la coincidencia, calculada por el sistema

    @PrePersist
    public void prePersist() {
        this.date = LocalDate.now();
    }

}
