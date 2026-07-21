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
@Table(name = "matches", uniqueConstraints = @UniqueConstraint(columnNames = { "newPet_id", "matchedPet_id" }))
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mascota que origina la búsqueda de coincidencias. La que acaba de ser
    // publicada.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newPet_id", nullable = false)
    private Pet newPet;

    // Mascota candidata encontrada por el algoritmo de coincidencias. La que se
    // sugiere como posible match.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matchedPet_id", nullable = false)
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

    // Helpers para que ambos lados de la relación se mantengan sincronizados.
    public void addUserState(MatchUserState userState) {
        if (!userStates.contains(userState)) {
            userStates.add(userState);
            userState.setMatch(this);
        }
    }

    public void removeUserState(MatchUserState userState) {
        if (userStates.remove(userState)) {
            userState.setMatch(null);
        }
    }

}
