package com.humanbooster.DAO;

import com.humanbooster.DAO.LieuRechargeDAO;
import com.humanbooster.model.LieuRecharge;
import com.humanbooster.DAO.GestionnaireSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query; // Pour les requêtes HQL typées

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation de l'interface {@link LieuRechargeDAO} utilisant Hibernate
 * pour la persistance des données de l'entité {@link LieuRecharge}.
 * Gère les transactions et les sessions Hibernate pour chaque opération.
 */
public class LieuRechargeDAOImpl implements LieuRechargeDAO {

    /** Référence à la SessionFactory, obtenue via HibernateFactoryManager. */
    private final SessionFactory sessionFactory = GestionnaireSessionFactory.getSessionFactory();

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdate(LieuRecharge lieuRecharge) {
        Transaction transaction = null;
        // Utilisation du try-with-resources pour s'assurer que la session est fermée
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(lieuRecharge); // Gère à la fois la création et la mise à jour
            transaction.commit();
            System.out.println("Lieu de recharge sauvegardé/mis à jour : " + lieuRecharge.getNom());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                    System.err.println("Transaction annulée pour lieu de recharge : " + lieuRecharge.getNom());
                } catch (Exception rbEx) {
                    System.err.println("Erreur lors du rollback de la transaction pour lieu : " + lieuRecharge.getNom() + " - " + rbEx.getMessage());
                }
            }
            System.err.println("Erreur lors de la sauvegarde/mise à jour du lieu de recharge : " + e.getMessage());
            e.printStackTrace(); // Pour le débogage
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<LieuRecharge> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            LieuRecharge lieu = session.get(LieuRecharge.class, id);
            return Optional.ofNullable(lieu);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche du lieu de recharge par ID " + id + " : " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LieuRecharge> findByNom(String nom) {
        try (Session session = sessionFactory.openSession()) {
            Query<LieuRecharge> query = session.createQuery(
                    "SELECT DISTINCT l FROM LieuRecharge l LEFT JOIN FETCH l.bornes WHERE lower(l.nom) LIKE lower(:nomParam)",
                    LieuRecharge.class
            );
            query.setParameter("nomParam", "%" + nom + "%"); // Recherche partielle (contient)
            return query.list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des lieux de recharge par nom '" + nom + "' : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retourne une liste vide en cas d'erreur
        }
    }

    /**
     * {@inheritDoc}
     * Modifié pour utiliser LEFT JOIN FETCH afin de charger la collection 'bornes'
     * et éviter LazyInitializationException lors d'accès ultérieurs en dehors de la session.
     * L'utilisation de DISTINCT est importante pour éviter les doublons de LieuRecharge
     * si un lieu a plusieurs bornes.
     */
    @Override
    public List<LieuRecharge> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT DISTINCT l FROM LieuRecharge l LEFT JOIN FETCH l.bornes", LieuRecharge.class).list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de tous les lieux de recharge : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retourne une liste vide en cas d'erreur
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        Optional<LieuRecharge> lieuOpt = findById(id);
        if (lieuOpt.isPresent()) {
            delete(lieuOpt.get());
        } else {
            System.err.println("Tentative de suppression d'un lieu de recharge non trouvé avec ID : " + id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(LieuRecharge lieuRecharge) {
        if (lieuRecharge == null || lieuRecharge.getId() == null) {
            System.err.println("Impossible de supprimer un lieu de recharge null ou sans ID.");
            return;
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            LieuRecharge lieuASupprimer = session.get(LieuRecharge.class, lieuRecharge.getId());
            if (lieuASupprimer != null) {
                session.delete(lieuASupprimer);
                transaction.commit();
                System.out.println("Lieu de recharge supprimé : " + lieuASupprimer.getNom());
            } else {
                if(transaction.isActive()) transaction.rollback();
                System.err.println("Lieu de recharge non trouvé pour suppression (ID : " + lieuRecharge.getId() + ")");
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                    System.err.println("Transaction annulée lors de la suppression du lieu : " + lieuRecharge.getNom());
                } catch (Exception rbEx) {
                    System.err.println("Erreur lors du rollback de la transaction de suppression pour lieu : " + lieuRecharge.getNom() + " - " + rbEx.getMessage());
                }
            }
            System.err.println("Erreur lors de la suppression du lieu de recharge : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
