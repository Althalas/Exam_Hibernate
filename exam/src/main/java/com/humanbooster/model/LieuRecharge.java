package com.humanbooster.model;

import org.hibernate.Hibernate; // Import pour Hibernate.isInitialized

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entité représentant un lieu de recharge pour véhicules électriques.
 * Mappée à la table "lieux_recharge".
 * Champs : id (Long), nom (String), adresse (String).
 * Relation : Un LieuRecharge peut avoir plusieurs BornesRecharge.
 */
@Entity
@Table(name = "lieux_recharge")
public class LieuRecharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrément géré par la BDD
    @Column(name = "id")
    private Long id;

    @Column(name = "nom", nullable = false, length = 200)
    private String nom;

    @Column(name = "adresse", nullable = false, length = 255)
    private String adresse;

    @OneToMany(mappedBy = "lieuRecharge", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<BorneRecharge> bornes = new HashSet<>();

    /**
     * Constructeur par défaut requis par JPA/Hibernate.
     */
    public LieuRecharge() {
    }

    /**
     * Constructeur pour créer un nouveau lieu de recharge.
     * @param nom Le nom du lieu.
     * @param adresse L'adresse complète du lieu.
     */
    public LieuRecharge(String nom, String adresse) {
        this.nom = nom;
        this.adresse = adresse;
    }

    // Getters et Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Set<BorneRecharge> getBornes() {
        return bornes;
    }

    public void setBornes(Set<BorneRecharge> bornes) {
        this.bornes = bornes;
    }

    public void addBorne(BorneRecharge borne) {
        this.bornes.add(borne);
        borne.setLieuRecharge(this);
    }

    public void removeBorne(BorneRecharge borne) {
        this.bornes.remove(borne);
        borne.setLieuRecharge(null);
    }

    /**
     * Retourne une représentation textuelle de l'objet LieuRecharge.
     * Affiche le nombre de bornes uniquement si la collection a été initialisée
     * pour éviter LazyInitializationException.
     * @return Une chaîne de caractères décrivant le lieu.
     */
    @Override
    public String toString() {
        String nbBornesStr;
        if (Hibernate.isInitialized(bornes) && bornes != null) {
            nbBornesStr = String.valueOf(bornes.size());
        } else {
            nbBornesStr = "(non initialisées ou null)";
        }
        return "LieuRecharge [ID=" + id + ", Nom='" + nom + "', Adresse='" + adresse + "', Nombre de bornes=" + nbBornesStr + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LieuRecharge that = (LieuRecharge) o;
        if (id != null) {
            return Objects.equals(id, that.id);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : System.identityHashCode(this);
    }
}
