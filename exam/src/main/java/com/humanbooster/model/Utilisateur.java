package com.humanbooster.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entité représentant un utilisateur du système.
 * Mappée à la table "utilisateurs".
 * Champs conformes à l'image "4. Modèle de données à mapper":
 * id, email, motDePasse, codeValidation, valide, role.
 * Relation : Un Utilisateur peut avoir plusieurs Réservations.
 */
@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "mot_de_passe", nullable = false, length = 255) // Longueur augmentée pour les hashs potentiels
    private String motDePasse;

    @Column(name = "code_validation", length = 36, nullable = true) // UUID ou code court
    private String codeValidation;

    @Column(name = "valide", nullable = false)
    private boolean valide = false; // Par défaut, un nouvel utilisateur n'est pas validé

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 50)
    private RoleUtilisateur role;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Reservation> reservations = new HashSet<>();

    /**
     * Constructeur par défaut requis par JPA/Hibernate.
     */
    public Utilisateur() {
    }

    /**
     * Constructeur pour créer un nouvel utilisateur.
     * @param email L'adresse email de l'utilisateur (doit être unique).
     * @param motDePasse Le mot de passe de l'utilisateur (devrait être haché avant persistance).
     * @param role Le rôle de l'utilisateur (ex: UTILISATEUR, ADMINISTRATEUR).
     */
    public Utilisateur(String email, String motDePasse, RoleUtilisateur role) {
        this.email = email;
        this.motDePasse = motDePasse; // En production, hacher ce mot de passe
        this.role = role;
        this.valide = false; // Un nouvel utilisateur doit généralement être validé
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getCodeValidation() {
        return codeValidation;
    }

    public void setCodeValidation(String codeValidation) {
        this.codeValidation = codeValidation;
    }

    public boolean isValide() { // Convention de nommage pour les booléens
        return valide;
    }

    public void setValide(boolean valide) {
        this.valide = valide;
    }

    public RoleUtilisateur getRole() {
        return role;
    }

    public void setRole(RoleUtilisateur role) {
        this.role = role;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setUtilisateur(this);
    }

    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setUtilisateur(null);
    }

    @Override
    public String toString() {
        return "Utilisateur [ID=" + id +
                ", Email='" + email + '\'' +
                ", Valide=" + valide +
                ", Rôle=" + role +
                ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        if (id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id != null ? id : email);
    }
}
