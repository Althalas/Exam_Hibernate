package com.humanbooster.DAO;


import com.humanbooster.DAO.UtilisateurDAO;
import com.humanbooster.model.Utilisateur;
import com.humanbooster.DAO.GestionnaireSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query; // Pour les requêtes HQL typées

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation de l'interface {@link UtilisateurDAO} utilisant Hibernate
 * pour la persistance des données de l'entité {@link Utilisateur}.
 * Gère les transactions et les sessions Hibernate pour chaque opération.
 */
public class UtilisateurDAOImpl implements UtilisateurDAO {

    /** Référence à la SessionFactory, obtenue via HibernateFactoryManager. */
    private final SessionFactory sessionFactory = GestionnaireSessionFactory.getSessionFactory();

    /**
     * {@inheritDoc}
     * Ouvre une session, commence une transaction, sauvegarde ou met à jour l'entité,
     * puis commite la transaction. Un rollback est effectué en cas d'erreur.
     */
    @Override
    public void saveOrUpdate(Utilisateur utilisateur) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(utilisateur); // Gère à la fois la création et la mise à jour
            transaction.commit();
            System.out.println("Utilisateur sauvegardé/mis à jour : " + utilisateur.getEmail());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                    System.err.println("Transaction annulée pour utilisateur : " + utilisateur.getEmail());
                } catch (Exception rbEx) {
                    System.err.println("Erreur lors du rollback de la transaction pour utilisateur : " + utilisateur.getEmail() + " - " + rbEx.getMessage());
                }
            }
            System.err.println("Erreur lors de la sauvegarde/mise à jour de l'utilisateur : " + e.getMessage());
            e.printStackTrace(); // Pour le débogage

        }
    }

    /**
     * {@inheritDoc}
     * Ouvre une session et récupère l'utilisateur par son ID.
     */
    @Override
    public Optional<Utilisateur> findById(Long id) {
        // Utilisation du try-with-resources pour s'assurer que la session est fermée
        try (Session session = sessionFactory.openSession()) {
            Utilisateur utilisateur = session.get(Utilisateur.class, id);
            return Optional.ofNullable(utilisateur);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par ID " + id + " : " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     * Ouvre une session et exécute une requête HQL pour trouver l'utilisateur par email.
     */
    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<Utilisateur> query = session.createQuery("FROM Utilisateur u WHERE u.email = :emailParam", Utilisateur.class);
            query.setParameter("emailParam", email);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par email " + email + " : " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     * Ouvre une session et exécute une requête HQL pour récupérer tous les utilisateurs.
     */
    @Override
    public List<Utilisateur> findAll() {

        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Utilisateur", Utilisateur.class).list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de tous les utilisateurs : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retourne une liste vide en cas d'erreur
        }
    }

    /**
     * {@inheritDoc}
     * Tente d'abord de trouver l'utilisateur par ID. S'il est trouvé,
     * il est ensuite passé à la méthode {@link #delete(Utilisateur)}.
     */
    @Override
    public void deleteById(Long id) {
        Optional<Utilisateur> utilisateurOpt = findById(id);
        if (utilisateurOpt.isPresent()) {
            delete(utilisateurOpt.get());
        } else {
            System.err.println("Tentative de suppression d'un utilisateur non trouvé avec ID : " + id);
        }
    }

    /**
     * {@inheritDoc}
     * Ouvre une session, commence une transaction, supprime l'entité,
     * puis commite la transaction. Un rollback est effectué en cas d'erreur.
     * L'objet utilisateur doit être dans un état persistant ou détaché avec un ID valide.
     */
    @Override
    public void delete(Utilisateur utilisateur) {
        if (utilisateur == null || utilisateur.getId() == null) {
            System.err.println("Impossible de supprimer un utilisateur null ou sans ID.");
            return;
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Utilisateur utilisateurASupprimer = session.get(Utilisateur.class, utilisateur.getId());
            if (utilisateurASupprimer != null) {
                session.delete(utilisateurASupprimer);
                transaction.commit();
                System.out.println("Utilisateur supprimé : " + utilisateurASupprimer.getEmail());
            } else {
                if(transaction.isActive()) transaction.rollback(); // Annuler la transaction si l'utilisateur n'est pas trouvé
                System.err.println("Utilisateur non trouvé pour suppression (ID : " + utilisateur.getId() + ")");
            }

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                    System.err.println("Transaction annulée lors de la suppression de l'utilisateur : " + utilisateur.getEmail());
                } catch (Exception rbEx) {
                    System.err.println("Erreur lors du rollback de la transaction de suppression pour utilisateur : " + utilisateur.getEmail() + " - " + rbEx.getMessage());
                }
            }
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
