package com.humanbooster.DAO;


import com.humanbooster.model.LieuRecharge;
import java.util.List;
import java.util.Optional;

/**
 * Interface Data Access Object (DAO) pour l'entité {@link LieuRecharge}.
 * Définit les opérations de persistance standard (CRUD) pour les lieux de recharge.
 */
public interface LieuRechargeDAO {

    /**
     * Sauvegarde un nouveau lieu de recharge ou met à jour un lieu existant.
     *
     * @param lieuRecharge L'objet {@link LieuRecharge} à sauvegarder ou à mettre à jour.
     * Ne doit pas être null.
     */
    void saveOrUpdate(LieuRecharge lieuRecharge);

    /**
     * Recherche un lieu de recharge par son identifiant unique (ID).
     *
     * @param id L'ID du lieu de recharge à rechercher.
     * @return Un {@link Optional} contenant le {@link LieuRecharge} trouvé,
     * ou un {@code Optional} vide si aucun lieu ne correspond à cet ID.
     */
    Optional<LieuRecharge> findById(Long id);

    /**
     * Recherche un lieu de recharge par son nom.
     * Cette méthode suppose que le nom du lieu peut ne pas être unique,
     * donc elle retourne une liste.
     *
     * @param nom Le nom du lieu de recharge à rechercher.
     * @return Une {@link List} des {@link LieuRecharge}s trouvés correspondant à ce nom.
     * Peut être vide.
     */
    List<LieuRecharge> findByNom(String nom);

    /**
     * Récupère la liste de tous les lieux de recharge enregistrés.
     *
     * @return Une {@link List} de tous les {@link LieuRecharge}s.
     * Peut être vide si aucun lieu n'est enregistré.
     */
    List<LieuRecharge> findAll();

    /**
     * Supprime un lieu de recharge de la base de données en utilisant son ID.
     * La suppression peut entraîner la suppression en cascade des bornes associées
     * si la relation est configurée ainsi (CascadeType.ALL et orphanRemoval=true).
     *
     * @param id L'ID du lieu de recharge à supprimer.
     */
    void deleteById(Long id);

    /**
     * Supprime un lieu de recharge donné de la base de données.
     *
     * @param lieuRecharge L'objet {@link LieuRecharge} à supprimer.
     */
    void delete(LieuRecharge lieuRecharge);
}
