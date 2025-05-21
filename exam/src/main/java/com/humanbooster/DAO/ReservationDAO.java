package com.humanbooster.DAO;

import com.humanbooster.model.BorneRecharge;
import com.humanbooster.model.Reservation;
import com.humanbooster.model.Utilisateur;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interface Data Access Object (DAO) pour l'entité {@link Reservation}.
 * Définit les opérations de persistance standard (CRUD) pour les réservations,
 * ainsi que des méthodes de recherche spécifiques, adaptées à la dernière structure
 * de l'entité Reservation.
 */
public interface ReservationDAO {

    /**
     * Sauvegarde une nouvelle réservation ou met à jour une réservation existante.
     *
     * @param reservation L'objet {@link Reservation} à sauvegarder ou à mettre à jour.
     * Ne doit pas être null.
     */
    void saveOrUpdate(Reservation reservation);

    /**
     * Recherche une réservation par son identifiant unique (ID).
     *
     * @param id L'ID de la réservation à rechercher.
     * @return Un {@link Optional} contenant la {@link Reservation} trouvée,
     * ou un {@code Optional} vide si aucune réservation ne correspond à cet ID.
     */
    Optional<Reservation> findById(Long id);

    /**
     * Récupère la liste de toutes les réservations enregistrées.
     *
     * @return Une {@link List} de toutes les {@link Reservation}s.
     * Peut être vide si aucune réservation n'est enregistrée.
     */
    List<Reservation> findAll();

    /**
     * Récupère la liste de toutes les réservations effectuées par un utilisateur spécifique.
     * Les réservations associées (borne) seront chargées paresseusement par défaut.
     *
     * @param utilisateur L'{@link Utilisateur} pour lequel rechercher les réservations.
     * @return Une {@link List} des {@link Reservation}s trouvées pour cet utilisateur.
     * Peut être vide.
     */
    List<Reservation> findByUtilisateur(Utilisateur utilisateur);

    /**
     * Récupère la liste de toutes les réservations associées à une borne spécifique.
     * Les réservations associées (utilisateur) seront chargées paresseusement par défaut.
     *
     * @param borne La {@link BorneRecharge} pour laquelle rechercher les réservations.
     * @return Une {@link List} des {@link Reservation}s trouvées pour cette borne.
     * Peut être vide.
     */
    List<Reservation> findByBorne(BorneRecharge borne);

    /**
     * Récupère la liste des réservations pour une borne donnée qui chevauchent
     * le créneau spécifié. Utile pour vérifier la disponibilité d'une borne.
     * La condition de chevauchement est : r.dateDebut < finInterval ET r.dateFin > debutInterval.
     *
     * @param borne La {@link BorneRecharge} concernée.
     * @param debutInterval La date et heure de début de l'intervalle de recherche.
     * @param finInterval La date et heure de fin de l'intervalle de recherche.
     * @return Une {@link List} des {@link Reservation}s pour cette borne qui chevauchent l'intervalle.
     */
    List<Reservation> findReservationsChevauchantesPourBorne(BorneRecharge borne, LocalDateTime debutInterval, LocalDateTime finInterval);

    /**
     * Supprime une réservation de la base de données en utilisant son ID.
     *
     * @param id L'ID de la réservation à supprimer.
     */
    void deleteById(Long id);

    /**
     * Supprime une réservation donnée de la base de données.
     *
     * @param reservation L'objet {@link Reservation} à supprimer.
     */
    void delete(Reservation reservation);
}
