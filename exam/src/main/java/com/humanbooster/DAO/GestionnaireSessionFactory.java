package com.humanbooster.DAO;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Gère la création et l'accès à l'unique instance de SessionFactory d'Hibernate.
 * La SessionFactory est initialisée une seule fois au chargement de la classe.
 */
public class GestionnaireSessionFactory {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Construit la SessionFactory en lisant la configuration depuis hibernate.cfg.xml.
     * Cette méthode est appelée statiquement une seule fois.
     *
     * @return L'instance de SessionFactory configurée.
     * @throws ExceptionInInitializerError si la création de la SessionFactory échoue.
     */
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("La création initiale de la SessionFactory Hibernate a échoué : " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retourne l'instance unique et globalement accessible de la SessionFactory.
     * Les DAO utiliseront cette méthode pour obtenir la SessionFactory, puis appelleront
     * sessionFactory.openSession() pour démarrer une nouvelle session Hibernate.
     *
     * @return La SessionFactory configurée.
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Ferme la SessionFactory et libère toutes les ressources associées (pools de connexion, caches, etc.).
     * Cette méthode doit impérativement être appelée à la fin de l'application
     * pour assurer un arrêt propre et éviter les fuites de ressources.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            getSessionFactory().close();
            System.out.println("SessionFactory Hibernate a été fermée proprement.");
        }
    }

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private GestionnaireSessionFactory() {
    }
}
