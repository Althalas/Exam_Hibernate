package com.humanbooster.DAO;

import com.humanbooster.DAO.ReservationDAO;
import com.humanbooster.model.BorneRecharge;
import com.humanbooster.model.Reservation;
import com.humanbooster.model.Utilisateur;
import com.humanbooster.DAO.GestionnaireSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation de l'interface {@link ReservationDAO} utilisant Hibernate
 * pour la persistance des données de l'entité {@link Reservation}.
 * Adaptée pour la dernière structure de l'entité Reservation.
 */
public class ReservationDAOImpl implements ReservationDAO {

    private final SessionFactory sessionFactory = GestionnaireSessionFactory.getSessionFactory();

    @Override
    public void saveOrUpdate(Reservation reservation) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(reservation);
            transaction.commit();
            System.out.println("Réservation (ID: " + reservation.getId() + ") sauvegardée/mise à jour.");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erreur lors de la sauvegarde/mise à jour de la réservation ID " + (reservation != null ? reservation.getId() : "null") + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Reservation reservation = session.get(Reservation.class, id);
            return Optional.ofNullable(reservation);
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche de la réservation par ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Reservation> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Reservation", Reservation.class).list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération de toutes les réservations: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Reservation> findByUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null || utilisateur.getId() == null) {
            System.err.println("Utilisateur ou ID utilisateur null pour findByUtilisateur.");
            return new ArrayList<>();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Reservation> query = session.createQuery(
                    "FROM Reservation r WHERE r.utilisateur.id = :utilisateurId", Reservation.class);
            query.setParameter("utilisateurId", utilisateur.getId());
            return query.list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des réservations pour l'utilisateur ID " + utilisateur.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Reservation> findByBorne(BorneRecharge borne) {
        if (borne == null || borne.getId() == null) {
            System.err.println("Borne ou ID borne null pour findByBorne.");
            return new ArrayList<>();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Reservation> query = session.createQuery(
                    "FROM Reservation r WHERE r.borne.id = :borneId", Reservation.class);
            query.setParameter("borneId", borne.getId());
            return query.list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des réservations pour la borne ID " + borne.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Reservation> findReservationsChevauchantesPourBorne(BorneRecharge borne, LocalDateTime debutInterval, LocalDateTime finInterval) {
        if (borne == null || borne.getId() == null || debutInterval == null || finInterval == null || !finInterval.isAfter(debutInterval)) {
            System.err.println("Paramètres invalides pour findReservationsChevauchantesPourBorne.");
            return new ArrayList<>();
        }
        try (Session session = sessionFactory.openSession()) {
            Query<Reservation> query = session.createQuery(
                    "FROM Reservation r WHERE r.borne.id = :borneId AND r.dateDebut < :finInterval AND r.dateFin > :debutInterval",
                    Reservation.class
            );
            query.setParameter("borneId", borne.getId());
            query.setParameter("finInterval", finInterval);
            query.setParameter("debutInterval", debutInterval);
            return query.list();
        } catch (Exception e) {
            System.err.println("Erreur lors de la recherche des réservations chevauchantes pour la borne ID " + borne.getId() + ": " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }




    @Override
    public void deleteById(Long id) {
        Optional<Reservation> reservationOpt = findById(id);
        if (reservationOpt.isPresent()) {
            delete(reservationOpt.get());
        } else {
            System.err.println("Réservation non trouvée pour suppression (ID : " + id + ")");
        }
    }

    @Override
    public void delete(Reservation reservation) {
        if (reservation == null || reservation.getId() == null) {
            System.err.println("Impossible de supprimer une réservation null ou sans ID.");
            return;
        }
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Reservation reservationASupprimer = session.get(Reservation.class, reservation.getId());
            if (reservationASupprimer != null) {
                session.delete(reservationASupprimer);
                transaction.commit();
                System.out.println("Réservation (ID: " + reservationASupprimer.getId() + ") supprimée.");
            } else {
                if(transaction.isActive()) transaction.rollback();
                System.err.println("Réservation non trouvée pour suppression (ID : " + reservation.getId() + ")");
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erreur lors de la suppression de la réservation ID " + reservation.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
