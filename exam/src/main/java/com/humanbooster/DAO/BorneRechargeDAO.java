package com.humanbooster.DAO;



import com.humanbooster.model.BorneRecharge;
import com.humanbooster.model.EtatBorne;
import com.humanbooster.model.LieuRecharge; // Nécessaire pour une recherche par lieu

import java.util.List;
import java.util.Optional;

/**
 * Interface Data Access Object (DAO) pour l'entité {@link BorneRecharge}.
 * Définit les opérations de persistance standard (CRUD) pour les bornes de recharge,
 * ainsi que des méthodes de recherche spécifiques.
 */
public interface BorneRechargeDAO {

    /**
     * Sauvegarde une nouvelle borne de recharge ou met à jour une borne existante.
     *
     * @param borneRecharge L'objet {@link BorneRecharge} à sauvegarder ou à mettre à jour.
     * Ne doit pas être null.
     */
    void saveOrUpdate(BorneRecharge borneRecharge);

    /**
     * Recherche une borne de recharge par son identifiant unique (ID).
     *
     * @param id L'ID de la borne de recharge à rechercher.
     * @return Un {@link Optional} contenant la {@link BorneRecharge} trouvée,
     * ou un {@code Optional} vide si aucune borne ne correspond à cet ID.
     */
    Optional<BorneRecharge> findById(Long id);

    /**
     * Récupère la liste de toutes les bornes de recharge enregistrées.
     *
     * @return Une {@link List} de toutes les {@link BorneRecharge}s.
     * Peut être vide si aucune borne n'est enregistrée.
     */
    List<BorneRecharge> findAll();

    /**
     * Récupère la liste de toutes les bornes de recharge associées à un lieu spécifique.
     *
     * @param lieu L'objet {@link LieuRecharge} pour lequel rechercher les bornes.
     * @return Une {@link List} des {@link BorneRecharge}s trouvées pour ce lieu.
     * Peut être vide.
     */
    List<BorneRecharge> findByLieu(LieuRecharge lieu);

    /**
     * Récupère la liste de toutes les bornes de recharge ayant un état spécifique.
     *
     * @param etat L'{@link EtatBorne} à rechercher.
     * @return Une {@link List} des {@link BorneRecharge}s trouvées avec cet état.
     * Peut être vide.
     */
    List<BorneRecharge> findByEtat(EtatBorne etat);


    /**
     * Récupère la liste des bornes de recharge ayant une puissance supérieure ou égale à la valeur spécifiée.
     *
     * @param puissanceMinKw La puissance minimale en kW.
     * @return Une {@link List} des {@link BorneRecharge}s correspondantes.
     */
    List<BorneRecharge> findByPuissanceMin(double puissanceMinKw);


    /**
     * Supprime une borne de recharge de la base de données en utilisant son ID.
     * La suppression peut entraîner la suppression en cascade des réservations associées
     * si la relation est configurée ainsi (CascadeType.ALL et orphanRemoval=true).
     *
     * @param id L'ID de la borne de recharge à supprimer.
     */
    void deleteById(Long id);

    /**
     * Supprime une borne de recharge donnée de la base de données.
     *
     * @param borneRecharge L'objet {@link BorneRecharge} à supprimer.
     */
    void delete(BorneRecharge borneRecharge);
}
