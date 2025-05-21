package com.humanbooster.DAO;

import com.humanbooster.DAO.BorneRechargeDAO;
import com.humanbooster.model.BorneRecharge;
import com.humanbooster.model.EtatBorne;
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
 * Implémentation de l'interface {@link BorneRechargeDAO} utilisant Hibernate
 * pour la persistance des données de l'entité {@link BorneRecharge}.
 * Gère les transactions et les sessions Hibernate pour chaque opération.
 */
public class BorneRechargeDAOImpl implements BorneRechargeDAO {

    /** Référence à la SessionFactory, obtenue via HibernateFactoryManager. */
    private final SessionFactory sessionFactory = GestionnaireSessionFactory.getSessionFactory();

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOrUpdate(BorneRecharge borneRecharge) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(borneRecharge);
            transaction.commit();
            System.out.println("Borne de recharge sauvegardée/mise à jour : ID " + borneRecharge.getId());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                    System.err.println("Transaction annulée pour borne de recharge ID : " + (borneRecharge != null ? borneRecharge.getId() : "null"));
                } catch (Exception rbEx) {
                    System.err.println("Erreur lors du rollback de la transaction pour borne : " + (borneRecharge != null ? borneRecharge.getId() : "null") + " - " + rbEx.getMessage());
                }
            }
            System.err.println("Erreur lors de la sauvegarde/mise à jour de la borne de recharge : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BorneRecharge> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            BorneRecharge borne = session.get(BorneRecharge.class, id);
            return Optional.ofNullable(borne);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche de la borne de recharge par ID " + id + " : " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BorneRecharge> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM BorneRecharge", BorneRecharge.class).list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de toutes les bornes de recharge : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BorneRecharge> findByLieu(LieuRecharge lieu) {
        if (lieu == null || lieu.getId() == null) {
            System.err.println("Lieu de recharge ou son ID ne peut être null pour la recherche de bornes.");
            return new ArrayList<>();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<BorneRecharge> query = session.createQuery("FROM BorneRecharge b WHERE b.lieuRecharge.id = :lieuId", BorneRecharge.class);
            query.setParameter("lieuId", lieu.getId());
            return query.list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des bornes pour le lieu ID " + lieu.getId() + " : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BorneRecharge> findByEtat(EtatBorne etat) {
        if (etat == null) {
            System.err.println("L'état de la borne ne peut être null pour la recherche.");
            return new ArrayList<>();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<BorneRecharge> query = session.createQuery("FROM BorneRecharge b WHERE b.etatBorne = :etatParam", BorneRecharge.class);
            query.setParameter("etatParam", etat);
            return query.list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des bornes par état '" + etat + "' : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BorneRecharge> findByPuissanceMin(double puissanceMinKw) {
        try (Session session = sessionFactory.openSession()) {
            Query<BorneRecharge> query = session.createQuery("FROM BorneRecharge b WHERE b.puissanceKw >= :puissanceMin", BorneRecharge.class);
            query.setParameter("puissanceMin", puissanceMinKw);
            return query.list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des bornes par puissance minimale (" + puissanceMinKw + "kW) : " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        Optional<BorneRecharge> borneOpt = findById(id);
        if (borneOpt.isPresent()) {
            delete(borneOpt.get());
        } else {
            System.err.println("Tentative de suppression d'une borne de recharge non trouvée avec ID : " + id);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(BorneRecharge borneRecharge) {
        if (borneRecharge == null || borneRecharge.getId() == null) {
            System.err.println("Impossible de supprimer une borne de recharge null ou sans ID.");
            return;
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            BorneRecharge borneASupprimer = session.get(BorneRecharge.class, borneRecharge.getId());
            if (borneASupprimer != null) {
                session.delete(borneASupprimer);
                transaction.commit();
                System.out.println("Borne de recharge supprimée : ID " + borneASupprimer.getId());
            } else {
                if(transaction.isActive()) transaction.rollback();
                System.err.println("Borne de recharge non trouvée pour suppression (ID : " + borneRecharge.getId() + ")");
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                try {
                    transaction.rollback();
                    System.err.println("Transaction annulée lors de la suppression de la borne ID : " + borneRecharge.getId());
                } catch (Exception rbEx) {
                    System.err.println("Erreur lors du rollback de la transaction de suppression pour borne ID : " + borneRecharge.getId() + " - " + rbEx.getMessage());
                }
            }
            System.err.println("Erreur lors de la suppression de la borne de recharge : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
