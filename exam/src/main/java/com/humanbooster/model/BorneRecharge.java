package com.humanbooster.model; // Assurez-vous que ce package correspond à votre structure

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entité représentant une borne de recharge électrique.
 * Mappée à la table "bornes_recharge".
 * Champs : id (Long), puissanceKw (Double), etatBorne (EtatBorne).
 * Relations :
 * - Plusieurs BornesRecharge peuvent appartenir à un LieuRecharge (ManyToOne).
 * - Une BorneRecharge peut être concernée par plusieurs Réservations (OneToMany).
 */
@Entity
@Table(name = "bornes_recharge")
public class BorneRecharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrément géré par la BDD
    @Column(name = "id")
    private Long id;

    /**
     * La puissance de la borne, exprimée en kilowatts (kW).
     * Le modèle de données du TP indique "puissance_kW".
     */
    @Column(name = "tarif_horaire", nullable = false)
    private Double tarif_horaire;

    /**
     * L'état actuel de la borne (ex: DISPONIBLE, HORS_SERVICE).
     * Mappé en tant que chaîne de caractères dans la base de données.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "etat_borne", nullable = false, length = 50) // Longueur pour le nom de l'enum
    private EtatBorne etatBorne;

    /**
     * Le lieu de recharge auquel cette borne est physiquement rattachée.
     * Relation Many-to-One : plusieurs bornes peuvent être dans un même lieu.
     * La colonne 'lieu_id' dans la table 'bornes_recharge' sera la clé étrangère.
     * FetchType.LAZY est généralement un bon choix pour les relations @ManyToOne
     * pour éviter de charger le lieu inutilement à chaque fois qu'une borne est chargée.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lieu_id", nullable = false) // La borne doit appartenir à un lieu
    private LieuRecharge lieuRecharge;

    /**
     * Ensemble des réservations associées à cette borne.
     * Relation One-to-Many : une borne peut avoir plusieurs réservations.
     * 'mappedBy = "borne"' indique que l'entité Reservation est propriétaire de la relation.
     */
    @OneToMany(mappedBy = "borne", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Reservation> reservations = new HashSet<>(); // Initialiser pour éviter NullPointerException

    /**
     * Constructeur par défaut requis par JPA/Hibernate.
     */
    public BorneRecharge() {
    }

    /**
     * Constructeur pour créer une nouvelle borne de recharge.
     * @param tarif_horaire le tarif à l'heure.
     * @param etatBorne L'état initial de la borne (voir {@link EtatBorne}).
     * @param lieuRecharge Le {@link LieuRecharge} auquel cette borne est associée.
     */
    public BorneRecharge(Double tarif_horaire, EtatBorne etatBorne, LieuRecharge lieuRecharge) {
        this.tarif_horaire = tarif_horaire;
        this.etatBorne = etatBorne;
        this.lieuRecharge = lieuRecharge;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTarif_horaire() {
        return tarif_horaire;
    }

    public void setTarif_horaire(Double tarif_horaire) {
        this.tarif_horaire = tarif_horaire;
    }

    public EtatBorne getEtatBorne() {
        return etatBorne;
    }

    public void setEtatBorne(EtatBorne etatBorne) {
        this.etatBorne = etatBorne;
    }

    public LieuRecharge getLieuRecharge() {
        return lieuRecharge;
    }

    public void setLieuRecharge(LieuRecharge lieuRecharge) {
        this.lieuRecharge = lieuRecharge;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * Ajoute une réservation à cette borne et établit le lien inverse.
     * @param reservation La réservation à ajouter.
     */
    public void addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setBorne(this);
    }

    /**
     * Supprime une réservation de cette borne et rompt le lien inverse.
     * @param reservation La réservation à supprimer.
     */
    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setBorne(null);
    }

    @Override
    public String toString() {
        return "BorneRecharge [ID=" + id +
                ", Puissance=" + tarif_horaire + " kW" +
                ", État='" + etatBorne + "'" +
                ", Lieu ID=" + (lieuRecharge != null ? lieuRecharge.getId() : "null") +
                "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorneRecharge that = (BorneRecharge) o;
        // Comparaison par ID si les objets sont persistés (ID non null)
        if (id != null) {
            return Objects.equals(id, that.id);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : System.identityHashCode(this);
    }
}
