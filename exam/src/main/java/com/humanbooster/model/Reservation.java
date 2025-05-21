package com.humanbooster.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Entité représentant une réservation d'une borne de recharge par un utilisateur.
 * Mappée à la table "reservations".
 * Champs conformes à l'image "image_6db47a.png":
 * id, dateDebut, dateFin, statut.
 * Relations :
 * - Plusieurs Réservations peuvent être faites par un Utilisateur (ManyToOne).
 * - Plusieurs Réservations peuvent concerner une BorneRecharge (ManyToOne).
 */
@Entity
@Table(name = "reservations") // Nom de la table en base de données
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrément géré par la BDD
    @Column(name = "id")
    private Long id;

    /**
     * Date et heure de début du créneau pour lequel la borne est réservée.
     */
    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    /**
     * Date et heure de fin du créneau pour lequel la borne est réservée.
     */
    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    /**
     * Statut actuel de la réservation (ex: EN_ATTENTE, ACCEPTEE, REFUSEE).
     * Utilise l'énumération StatutReservation.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 50)
    private StatutReservation statut; // Utilisation de l'énumération StatutReservation

    /**
     * L'utilisateur qui a effectué cette réservation.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    /**
     * La borne de recharge concernée par cette réservation.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borne_id", nullable = false)
    private BorneRecharge borne;

    /**
     * Constructeur par défaut requis par JPA/Hibernate.
     */
    public Reservation() {
    }

    /**
     * Constructeur pour créer une nouvelle réservation.
     * Le statut est initialisé à EN_ATTENTE par défaut.
     *
     * @param dateDebut       La date et heure de début du créneau réservé.
     * @param dateFin         La date et heure de fin du créneau réservé.
     * @param utilisateur     L'{@link Utilisateur} effectuant la réservation.
     * @param borne           La {@link BorneRecharge} concernée par la réservation.
     */
    public Reservation(LocalDateTime dateDebut, LocalDateTime dateFin, Utilisateur utilisateur, BorneRecharge borne) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.utilisateur = utilisateur;
        this.borne = borne;
        this.statut = StatutReservation.EN_ATTENTE; // Statut par défaut lors de la création
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public StatutReservation getStatut() {
        return statut;
    }

    public void setStatut(StatutReservation statut) {
        this.statut = statut;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public BorneRecharge getBorne() {
        return borne;
    }

    public void setBorne(BorneRecharge borne) {
        this.borne = borne;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String utilisateurInfo = (utilisateur != null && utilisateur.getId() != null) ? "Utilisateur ID: " + utilisateur.getId() : "Utilisateur: (non chargé ou null)";
        String borneInfo = (borne != null && borne.getId() != null) ? "Borne ID: " + borne.getId() : "Borne: (non chargée ou null)";

        return "Reservation [ID=" + id +
                ", Début=" + (dateDebut != null ? dateDebut.format(formatter) : "N/A") +
                ", Fin=" + (dateFin != null ? dateFin.format(formatter) : "N/A") +
                ", Statut=" + statut +
                ", " + utilisateurInfo +
                ", " + borneInfo +
                "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        if (id != null) { // Si l'ID est défini (entité persistée), il est le critère principal
            return Objects.equals(id, that.id);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : System.identityHashCode(this);
    }
}
