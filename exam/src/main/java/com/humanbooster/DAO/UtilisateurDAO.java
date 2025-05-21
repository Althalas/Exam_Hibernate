package com.humanbooster.DAO;
 // Package pour les interfaces DAO

import com.humanbooster.model.Utilisateur;
import java.util.List;
import java.util.Optional;

/**
 * Interface Data Access Object (DAO) pour l'entité {@link Utilisateur}.
 * Définit les opérations de persistance standard (CRUD) pour les utilisateurs,
 * ainsi que des méthodes de recherche spécifiques.
 */
public interface UtilisateurDAO {

    /**
     * Sauvegarde un nouvel utilisateur ou met à jour un utilisateur existant dans la base de données.
     * Si l'utilisateur a un ID null, il est considéré comme nouveau et sera persisté.
     * S'il a un ID non null, une tentative de mise à jour (merge) sera effectuée.
     *
     * @param utilisateur L'objet {@link Utilisateur} à sauvegarder ou à mettre à jour.
     * Ne doit pas être null.
     */
    void saveOrUpdate(Utilisateur utilisateur);

    /**
     * Recherche un utilisateur par son identifiant unique (ID).
     *
     * @param id L'ID de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'{@link Utilisateur} trouvé,
     * ou un {@code Optional} vide si aucun utilisateur ne correspond à cet ID.
     */
    Optional<Utilisateur> findById(Long id);

    /**
     * Recherche un utilisateur par son adresse e-mail.
     * L'adresse e-mail est supposée être unique.
     *
     * @param email L'adresse e-mail de l'utilisateur à rechercher.
     * @return Un {@link Optional} contenant l'{@link Utilisateur} trouvé,
     * ou un {@code Optional} vide si aucun utilisateur ne correspond à cet email.
     */
    Optional<Utilisateur> findByEmail(String email);

    /**
     * Récupère la liste de tous les utilisateurs enregistrés dans la base de données.
     *
     * @return Une {@link List} de tous les {@link Utilisateur}s.
     * Peut être vide si aucun utilisateur n'est enregistré.
     */
    List<Utilisateur> findAll();

    /**
     * Supprime un utilisateur de la base de données en utilisant son ID.
     * Si aucun utilisateur avec cet ID n'est trouvé, l'opération n'a aucun effet.
     *
     * @param id L'ID de l'utilisateur à supprimer.
     */
    void deleteById(Long id);

    /**
     * Supprime un utilisateur donné de la base de données.
     * L'utilisateur doit être une entité gérée ou avoir un ID correspondant à une entité existante.
     *
     * @param utilisateur L'objet {@link Utilisateur} à supprimer.
     */
    void delete(Utilisateur utilisateur);
}
