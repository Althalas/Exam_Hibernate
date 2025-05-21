# **README \- Electricity Business (TP Java Hibernate \- Évaluation DAO ORM)**

Ce projet est une implémentation du système "Electricity Business" utilisant Java, Hibernate pour la couche ORM (Object-Relational Mapping) et le pattern DAO (Data Access Object) pour la persistance des données. Il est basé sur le cahier des charges "TP Java Hibernate \- Évaluation DAO ORM".

## **Structure du Projet (Maven)**

Le projet est structuré comme un projet Maven standard :

* **pom.xml**: Fichier de configuration Maven qui gère les dépendances du projet (Hibernate, connecteur de base de données, etc.) et la configuration du build.  
* **src/main/java/**: Contient le code source Java de l'application.  
  * com/humanbooster/model : Classes d'entités JPA (Utilisateur, LieuRecharge, BorneRecharge, Reservation, et les énumérations EtatBorne, RoleUtilisateur, StatutReservation).  
  * com/humanbooster/DAO : Interfaces DAO définissant les contrats pour les opérations de persistance et implémentations concrètes des interfaces DAO utilisant Hibernate.
  * com/humanbooster/DAO/GestionnaireSessionFactory : Classes utilitaires, notamment GestionnaireSessionFactory.java pour la gestion de la SessionFactory.  
  * com/humanbooster/App.java : Classe principale contenant la méthode main pour lancer des démonstrations ou l'application.  
* **src/main/resources/**: Contient les fichiers de configuration.  
  * hibernate.cfg.xml: Fichier de configuration principal d'Hibernate (connexion base de données, dialecte, mapping des entités, etc.).
 
## **Explication des Choix Techniques**

* **Langage et Plateforme :** Java (JDK 11+) a été choisi pour sa robustesse, son écosystème mature et sa portabilité.  
* **Gestion de Projet et Dépendances :** Apache Maven est utilisé pour structurer le projet, gérer les dépendances (Hibernate, pilote JDBC MySQL, etc.) et faciliter le processus de build.  
* **ORM (Object-Relational Mapping) :** Hibernate (version 5.6.x) a été sélectionné comme framework ORM. Il simplifie l'interaction avec la base de données en mappant les objets Java à des tables relationnelles, réduisant ainsi la quantité de code SQL "boilerplate" à écrire. Il offre également des fonctionnalités avancées comme la gestion du cache et le chargement paresseux (lazy loading).  
* **API de Persistance :** JPA (Java Persistence API) est utilisé via les annotations (@Entity, @Table, @Id, @Column, @ManyToOne, @OneToMany, etc.) pour définir le mapping des entités. Hibernate est une implémentation de JPA.  
* **Base de Données :** MySQL (version 8+) est utilisée comme système de gestion de base de données relationnelle, conformément aux indications des logs d'exécution. La connexion est configurée dans hibernate.cfg.xml. Pour le développement et les tests, une stratégie hibernate.hbm2ddl.auto=create-drop est employée pour recréer le schéma à chaque lancement, garantissant un environnement propre.  
* **Pattern DAO (Data Access Object) :** La couche d'accès aux données est structurée selon le pattern DAO. Pour chaque entité, une interface DAO définit les opérations de persistance, et une classe d'implémentation concrète fournit la logique utilisant Hibernate. Cela permet de découpler la logique métier de la technologie de persistance.  
* **Gestion de la SessionFactory :** Une classe utilitaire (GestionnaireSessionFactory) est responsable de la création et de la fourniture de l'unique instance de SessionFactory, qui est un objet coûteux à créer. Les DAO obtiennent la SessionFactory de ce gestionnaire et ouvrent/ferment les Session Hibernate pour chaque transaction.  
* **Gestion des Transactions :** Les opérations de modification de données (sauvegarde, mise à jour, suppression) sont encapsulées dans des transactions Hibernate pour garantir l'atomicité et la cohérence des données. Un rollback est effectué en cas d'erreur.  
* **Chargement des Collections :** Les collections liées (relations @OneToMany, @ManyToMany) sont configurées avec FetchType.LAZY par défaut pour optimiser les performances en ne chargeant les données associées que lorsque c'est explicitement nécessaire. Pour éviter les LazyInitializationException lors d'accès en dehors d'une session active (par exemple, dans les méthodes toString() ou dans la couche de présentation/service), des stratégies de chargement explicite comme JOIN FETCH dans les requêtes HQL sont utilisées dans les DAO lorsque pertinent (par exemple, dans les méthodes findAll qui sont susceptibles d'être utilisées pour un affichage complet).

## **Résultat Attendu des Méthodes DAO Testées (dans App.java)**

La classe App.java a pour objectif de démontrer le bon fonctionnement des opérations CRUD (Create, Read, Update, Delete) de base pour chaque entité via leurs DAO respectifs.

Pour chaque entité (Utilisateur, LieuRecharge, BorneRecharge, Reservation), on s'attend aux résultats suivants lors de l'exécution de App.java :

1. **Création (Save/SaveOrUpdate) :**  
   * **Attendu :** De nouvelles instances d'entités sont créées et persistées en base de données. Après l'appel à saveOrUpdate(), les objets Java devraient se voir assigner un ID auto-généré par la base de données (si @GeneratedValue(strategy \= GenerationType.IDENTITY) est utilisé). Les logs Hibernate devraient afficher les requêtes INSERT correspondantes. Un message de confirmation de sauvegarde/mise à jour est affiché sur la console.  
   * **Vérification :** Consultation de la base de données MySQL pour confirmer la présence des nouvelles lignes dans les tables respectives.  
2. **Lecture (FindById, FindAll, autres FindBy...) :**  
   * **findById(Long id) Attendu :** Retourne un Optional contenant l'entité si un enregistrement avec l'ID spécifié existe, sinon un Optional vide. Les logs Hibernate devraient afficher une requête SELECT ... WHERE id=?.  
   * **findAll() Attendu :** Retourne une List de toutes les entités présentes dans la table correspondante. Si JOIN FETCH est utilisé (comme pour LieuRechargeDaoImpl.findAll()), les collections associées (ex: bornes pour LieuRecharge) devraient être initialisées et accessibles sans LazyInitializationException. Les logs Hibernate devraient afficher une requête SELECT ....  
   * **findByEmail(String email) (pour UtilisateurDao) Attendu :** Retourne un Optional de l'utilisateur correspondant à l'email (unique).  
   * **findByNom(String nom) (pour LieuRechargeDao) Attendu :** Retourne une List des lieux dont le nom correspond (potentiellement avec JOIN FETCH pour les bornes).  
   * **findByLieu(LieuRecharge lieu), findByEtat(EtatBorne etat) (pour BorneRechargeDao) Attendu :** Retournent des listes filtrées de bornes.  
   * **findByUtilisateur(Utilisateur u), findByBorne(BorneRecharge b), findReservationsChevauchantesPourBorne(...) (pour ReservationDao) Attendu :** Retournent des listes filtrées de réservations.  
   * **Vérification :** Affichage des entités récupérées sur la console. Les données affichées doivent correspondre à ce qui a été persisté.  
3. **Mise à jour (SaveOrUpdate avec un objet existant modifié) :**  
   * **Attendu :** Les modifications apportées à un objet entité (préalablement chargé ou un nouvel objet avec un ID existant) sont persistées en base de données après l'appel à saveOrUpdate(). Les logs Hibernate devraient afficher les requêtes UPDATE correspondantes.  
   * **Vérification :** Rechargement de l'entité depuis la base de données (via findById) et vérification que les champs ont bien les nouvelles valeurs. Consultation directe de la base de données.  
4. **Suppression (Delete, DeleteById) :**  
   * **Attendu :** L'enregistrement correspondant à l'entité ou à l'ID spécifié est supprimé de la base de données. Si des relations sont configurées avec CascadeType.ALL et orphanRemoval=true (par exemple, LieuRecharge vers BorneRecharge, ou Utilisateur vers Reservation), les entités dépendantes devraient également être supprimées. Les logs Hibernate devraient afficher les requêtes DELETE correspondantes.  
   * **Vérification :** Tentative de rechargement de l'entité supprimée (devrait retourner Optional.empty()). Consultation directe de la base de données pour confirmer la disparition de la ligne. Vérification que les entités en cascade ont aussi été supprimées.

En résumé, l'exécution de App.java doit montrer que chaque méthode DAO interagit correctement avec la base de données MySQL via Hibernate, en créant, lisant, mettant à jour et supprimant des données de manière cohérente et sans erreurs (notamment les LazyInitializationException grâce aux JOIN FETCH et les ConstraintViolationException grâce à une gestion propre du schéma avec create-drop et des données de test uniques).

## **Partie Core Terminée**

La partie "core" du projet, telle que définie par le cahier des charges du TP Hibernate, est considérée comme suit :

* **Modélisation et Mapping ORM :** Les entités (Utilisateur, LieuRecharge, BorneRecharge, Reservation) sont définies et mappées avec les annotations JPA conformément au modèle de données fourni.  
* **Couche DAO :**  
  * Les interfaces DAO pour chaque entité sont définies.  
  * Les implémentations DAO pour chaque entité, utilisant Hibernate pour les opérations CRUD (Create, Read, Update, Delete) de base, sont réalisées.  
* **Configuration Hibernate :** La configuration pour la connexion à la base de données (MySQL, comme indiqué dans vos logs récents) et la gestion de la SessionFactory sont en place.  
* **Démonstration :** Une classe principale App.java permet de tester les opérations CRUD de base.

## **Prérequis**

   * JDK 11 ou supérieur.  
   * Apache Maven installé et configuré dans le PATH.
   * Un serveur de base de données MySQL accessible (via Docker, configuré avec hibernate.cfg.xml). Vérifier que le service MySQL est démarré.
