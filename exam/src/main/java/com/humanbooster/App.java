package com.humanbooster;

import com.humanbooster.DAO.*;
import com.humanbooster.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Classe principale pour démontrer les fonctionnalités de base du système
 * Electricity Business avec Hibernate et le pattern DAO.
 * Effectue des opérations CRUD minimales sur les entités.
 * Mise à jour pour correspondre à la nouvelle structure de l'entité Reservation.
 */
public class App { // Renommez en App si c'est le nom de votre classe principale
    public static void main(String[] args) {
        System.out.println("Démarrage de l'application Electricity Business (TP Hibernate)...");

        UtilisateurDAO utilisateurDao = new UtilisateurDAOImpl();
        LieuRechargeDAO lieuRechargeDao = new LieuRechargeDAOImpl();
        BorneRechargeDAO borneRechargeDao = new BorneRechargeDAOImpl();
        ReservationDAO reservationDao = new ReservationDAOImpl();

        try {
            System.out.println("\n--- GESTION DES UTILISATEURS ---");
            // Utilisation du constructeur mis à jour pour Utilisateur
            Utilisateur user1 = new Utilisateur("jean.dupont@example.com", "password123", RoleUtilisateur.UTILISATEUR);
            Utilisateur user2 = new Utilisateur("alice.martin@example.com", "securepass", RoleUtilisateur.UTILISATEUR);
            // Assurez-vous que l'entité Utilisateur a bien été mise à jour avec les champs de l'image image_6be2c9.png

            utilisateurDao.saveOrUpdate(user1);
            utilisateurDao.saveOrUpdate(user2);
            if (user1.getId() != null && user2.getId() != null) {
                System.out.println("Utilisateur 1 (ID: " + user1.getId() + ") et Utilisateur 2 (ID: " + user2.getId() + ") créés/mis à jour.");
            } else {
                System.err.println("Problème lors de la création des utilisateurs initiaux, les IDs sont null. Vérifiez les erreurs de contrainte (email unique) et la configuration de la base de données.");
            }

            if (user1.getId() != null) {
                Optional<Utilisateur> foundUserOpt = utilisateurDao.findById(user1.getId());
                foundUserOpt.ifPresent(u -> {
                    System.out.println("Utilisateur trouvé par ID " + user1.getId() + ": " + u.getEmail());
                    u.setEmail("jean.dupont.updated@example.com"); // Changement d'email pour le test
                    utilisateurDao.saveOrUpdate(u);
                    System.out.println("Email de l'utilisateur ID " + u.getId() + " mis à jour.");
                });
            }

            System.out.println("\nListe de tous les utilisateurs :");
            List<Utilisateur> tousLesUtilisateurs = utilisateurDao.findAll();
            tousLesUtilisateurs.forEach(u -> System.out.println(" - " + u));


            System.out.println("\n--- GESTION DES LIEUX DE RECHARGE ---");
            LieuRecharge lieu1 = new LieuRecharge("Parking Principal", "1 Avenue de la République, Ville Lumière");
            LieuRecharge lieu2 = new LieuRecharge("Zone Commerciale Nord", "15 Route Nationale, Ville Étoile");

            lieuRechargeDao.saveOrUpdate(lieu1);
            lieuRechargeDao.saveOrUpdate(lieu2);
            if (lieu1.getId() != null && lieu2.getId() != null) {
                System.out.println("Lieu 1 (ID: " + lieu1.getId() + ") et Lieu 2 (ID: " + lieu2.getId() + ") créés/mis à jour.");
            } else {
                System.err.println("Problème lors de la création des lieux initiaux.");
            }

            System.out.println("\nListe de tous les lieux de recharge :");
            lieuRechargeDao.findAll().forEach(l -> System.out.println(" - " + l));


            System.out.println("\n--- GESTION DES BORNES DE RECHARGE ---");
            if (lieu1.getId() != null && lieu2.getId() != null) {
                // Utilisation du constructeur corrigé pour BorneRecharge
                BorneRecharge borne1 = new BorneRecharge(7.0, EtatBorne.DISPONIBLE, lieu1); // 7.0 pour tarifHoraire
                BorneRecharge borne2 = new BorneRecharge(22.0, EtatBorne.OCCUPEE, lieu1);
                BorneRecharge borne3 = new BorneRecharge(3.0, EtatBorne.HORS_SERVICE, lieu2);

                borneRechargeDao.saveOrUpdate(borne1);
                borneRechargeDao.saveOrUpdate(borne2);
                borneRechargeDao.saveOrUpdate(borne3);
                System.out.println("Borne 1 (ID: " + borne1.getId() + "), Borne 2 (ID: " + borne2.getId() + "), Borne 3 (ID: " + borne3.getId() + ") créées.");

                borne2.setEtatBorne(EtatBorne.DISPONIBLE); // Utiliser setEtat
                borneRechargeDao.saveOrUpdate(borne2);
                System.out.println("État de la Borne 2 mis à jour à DISPONIBLE.");

                System.out.println("\nBornes du lieu '" + lieu1.getNom() + "':");
                LieuRecharge lieu1AvecBornes = lieuRechargeDao.findById(lieu1.getId()).orElse(null);
                if (lieu1AvecBornes != null) {
                    List<BorneRecharge> bornesLieu1 = borneRechargeDao.findByLieu(lieu1AvecBornes);
                    bornesLieu1.forEach(b -> System.out.println(" - " + b));
                }

                System.out.println("\nBornes actuellement DISPONIBLES:");
                borneRechargeDao.findByEtat(EtatBorne.DISPONIBLE).forEach(b -> System.out.println(" - " + b));


                System.out.println("\n--- GESTION DES RESERVATIONS ---");
                Optional<Utilisateur> utilisateurPourResaOpt = utilisateurDao.findById(user2.getId());
                Optional<BorneRecharge> bornePourResaOpt = borneRechargeDao.findById(borne1.getId());

                if (utilisateurPourResaOpt.isPresent() && bornePourResaOpt.isPresent() && user2.getId() != null && borne1.getId() != null) {
                    Utilisateur utilisateurPourResa = utilisateurPourResaOpt.get();
                    BorneRecharge borneAReserver = bornePourResaOpt.get();

                    if (borneAReserver.getEtatBorne() == EtatBorne.DISPONIBLE) { // Utiliser getEtat
                        LocalDateTime debutCrenau = LocalDateTime.now().plusHours(2).withMinute(0).withSecond(0).withNano(0);
                        LocalDateTime finCrenau = debutCrenau.plusHours(1); // Réservation d'une heure

                        List<Reservation> chevauchements = reservationDao.findReservationsChevauchantesPourBorne(borneAReserver, debutCrenau, finCrenau);
                        if (chevauchements.isEmpty()) {
                            // Utilisation du constructeur mis à jour pour Reservation (sans dateReservation)
                            Reservation reservation1 = new Reservation(debutCrenau, finCrenau, utilisateurPourResa, borneAReserver);
                            // Le statut est EN_ATTENTE par défaut dans le constructeur de Reservation
                            reservationDao.saveOrUpdate(reservation1);
                            System.out.println("Réservation créée (ID: " + reservation1.getId() + ", Statut: " + reservation1.getStatut() + ") pour " + utilisateurPourResa.getEmail() + " sur la borne ID " + borneAReserver.getId());

                            // On pourrait vouloir changer le statut pour la démo
                            reservation1.setStatut(StatutReservation.ACCEPTEE);
                            reservationDao.saveOrUpdate(reservation1);
                            System.out.println("Statut de la réservation ID " + reservation1.getId() + " mis à jour à " + reservation1.getStatut());


                            System.out.println("\nRéservations de " + utilisateurPourResa.getEmail() + ":");
                            reservationDao.findByUtilisateur(utilisateurPourResa).forEach(r -> System.out.println(" - " + r));
                        } else {
                            System.out.println("La borne ID " + borneAReserver.getId() + " est déjà réservée sur ce créneau.");
                        }
                    } else {
                        System.out.println("La borne ID " + borneAReserver.getId() + " n'est pas disponible (État: " + borneAReserver.getEtatBorne() + ").");
                    }
                } else {
                    System.out.println("Utilisateur ou Borne non trouvé (ou ID null) pour créer une réservation.");
                }

                System.out.println("\n--- SUPPRESSION D'ELEMENTS (Exemple) ---");
                // ... (la logique de suppression peut rester similaire, mais attention aux ID si les créations ont échoué)

            } else {
                System.err.println("Erreur: Un des lieux n'a pas été persisté correctement, certaines opérations sur les bornes et réservations sont sautées.");
            }

            // ... (fin de la section suppression et du try-catch)

        } catch (Exception e) {
            System.err.println("Une erreur majeure est survenue dans l'application :");
            e.printStackTrace();
        } finally {
            GestionnaireSessionFactory.shutdown();
        }

        System.out.println("\nFin de l'application Electricity Business (TP Hibernate).");
    }
}
