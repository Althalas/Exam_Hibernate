# **README \- Electricity Business (TP Java Hibernate \- Évaluation DAO ORM)**

Ce projet est une implémentation du système "Electricity Business" utilisant Java, Hibernate pour la couche ORM (Object-Relational Mapping) et le pattern DAO (Data Access Object) pour la persistance des données. Il est basé sur le cahier des charges "TP Java Hibernate \- Évaluation DAO ORM".

## **Structure du Projet (Maven)**

Le projet est structuré comme un projet Maven standard :

* **pom.xml**: Fichier de configuration Maven qui gère les dépendances du projet (Hibernate, connecteur de base de données, etc.) et la configuration du build.  
* **src/main/java/**: Contient le code source Java de l'application.  
  * com/example/entity/ (ou com/humanbooster/model/ selon votre structure) : Classes d'entités JPA (Utilisateur, LieuRecharge, BorneRecharge, Reservation, et les énumérations EtatBorne, RoleUtilisateur, StatutReservation).  
  * com/example/dao/ (ou com/humanbooster/DAO/ ou com/humanbooster/dao/): Interfaces DAO définissant les contrats pour les opérations de persistance.  
  * com/example/dao/impl/ (ou com/humanbooster/DAO/impl/ ou com.humanbooster/dao/impl/): Implémentations concrètes des interfaces DAO utilisant Hibernate.  
  * com/example/util/ (ou com/humanbooster/util/): Classes utilitaires, notamment HibernateFactoryManager.java pour la gestion de la SessionFactory.  
  * com/example/Main.java (ou com.humanbooster/App.java): Classe principale contenant la méthode main pour lancer des démonstrations ou l'application.  
* **src/main/resources/**: Contient les fichiers de configuration.  
  * hibernate.cfg.xml: Fichier de configuration principal d'Hibernate (connexion base de données, dialecte, mapping des entités, etc.).  
  * (Optionnel) log4j2.xml ou logback.xml si une configuration de logging plus avancée est utilisée.

## **Partie Core Terminée**

La partie "core" du projet, telle que définie par le cahier des charges du TP Hibernate, est considérée comme suit :

* **Modélisation et Mapping ORM :** Les entités (Utilisateur, LieuRecharge, BorneRecharge, Reservation) sont définies et mappées avec les annotations JPA conformément au modèle de données fourni.  
* **Couche DAO :**  
  * Les interfaces DAO pour chaque entité sont définies.  
  * Les implémentations DAO pour chaque entité, utilisant Hibernate pour les opérations CRUD (Create, Read, Update, Delete) de base, sont réalisées.  
* **Configuration Hibernate :** La configuration pour la connexion à la base de données (MySQL, comme indiqué dans vos logs récents) et la gestion de la SessionFactory sont en place.  
* **Démonstration :** Une classe principale (Main.java ou App.java) permet de tester les opérations CRUD de base.

*(Note : L'intégration du menu console de l'exercice précédent n'est pas encore finalisée dans cette version du README, car nous nous concentrons sur les exigences du TP Hibernate.)*

## **Bonus Implémentés (Facultatif)**

Conformément au cahier des charges du "TP Java Hibernate \- Évaluation DAO ORM", les fonctionnalités bonus suivantes **n'ont pas encore été implémentées** dans la version actuelle du code :

* Pas de filtrage avancé des réservations (par date, par utilisateur trié par date, etc.) au-delà des recherches de base par DAO.  
* Pas de gestion spécifique des tarifs ou de leur historique.  
* Pas de fonctionnalités avancées d'interface utilisateur au-delà des tests CRUD dans la classe Main.

*(Cette section est à mettre à jour si des bonus du TP Hibernate sont implémentés.)*

## **Compilation et Exécution (avec Maven)**

1. **Prérequis :**  
   * JDK 11 ou supérieur.  
   * Apache Maven installé et configuré dans votre PATH.  
   * Un serveur de base de données MySQL accessible (par exemple, via Docker, comme configuré dans votre hibernate.cfg.xml). Assurez-vous que le service MySQL est démarré.  
2. **Configuration de la Base de Données :**  
   * Vérifiez et mettez à jour les informations de connexion (URL, utilisateur, mot de passe) dans le fichier src/main/resources/hibernate.cfg.xml pour correspondre à votre environnement MySQL.  
   * Assurez-vous que la base de données spécifiée dans l'URL (par exemple, testdb) existe ou que l'option createDatabaseIfNotExist=true est présente dans l'URL JDBC et que l'utilisateur a les droits nécessaires.  
3. **Compilation :**  
   * Ouvrez un terminal ou une invite de commande à la racine de votre projet Maven (là où se trouve le fichier pom.xml).  
   * Exécutez la commande Maven pour nettoyer et compiler le projet, et créer le package (JAR) :  
     mvn clean package

4. **Exécution :**  
   * Après une compilation réussie, vous pouvez exécuter l'application (la classe Main ou App) de plusieurs manières :  
     * **Via Maven (si le plugin exec-maven-plugin est configuré dans le pom.xml) :**  
       mvn exec:java

     * Directement avec la commande java (après mvn package) :  
       Le JAR sera créé dans le dossier target/. Par exemple, si votre artifactId est electricity-business-tp-hibernate et la version 1.0-SNAPSHOT, le JAR sera target/electricity-business-tp-hibernate-1.0-SNAPSHOT.jar.  
       java \-jar target/electricity-business-tp-hibernate-1.0-SNAPSHOT.jar

       *(Assurez-vous que votre pom.xml configure correctement la classe principale dans le manifeste du JAR si vous utilisez cette méthode, ou spécifiez-la avec \-cp et le nom complet de la classe).*  
5. **Vérification :**  
   * Observez la console pour les logs Hibernate (y compris les requêtes SQL si show\_sql est activé) et les messages de votre classe Main/App.  
   * Vérifiez l'état de votre base de données MySQL pour confirmer que les tables ont été créées et que les données ont été insérées/modifiées/supprimées comme prévu.

## **Livrables Attendus (selon demande initiale adaptée au TP)**

* **Code source complet (src/)**: Incluant les entités, DAO, utilitaires, et la classe principale.  
* **Fichier pom.xml**: Pour la gestion du projet Maven.  
* **Fichier hibernate.cfg.xml**: Pour la configuration d'Hibernate.  
* **Ce fichier README.md**: Documentant le projet.  
* **(Optionnel) Dossier exports/**: Non applicable pour le TP Hibernate, sauf si une fonctionnalité de génération de fichier est explicitement ajoutée.