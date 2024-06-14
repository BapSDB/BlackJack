import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Joueur {

    private final String nom;
    private final Paquet pioche;
    private final List<Carte> main;
    private int positionInitiale;
    private int score;
    private int nbVictoires;

    private Joueur(String nom, Random random) {
        this.nom = nom;
        this.pioche = Paquet.nouveauPaquetNeuf(random);
        this.main = new ArrayList<>();
    }

    public static Joueur nouveau(String nom, Random random) {
        return new Joueur(nom, random);
    }

    public void initScore() {
        this.score = 0;
    }

    public void ajouterScore(int score) {
        this.score += score;
    }

    public void incrNbVictoires() {
        ++nbVictoires;
    }

    public int getPositionInitiale() {
        return positionInitiale;
    }

    public String getNom() {
        return nom;
    }

    public Paquet getPioche() {
        return pioche;
    }

    public List<Carte> getMain() {
        return main;
    }

    public int getScore() {
        return score;
    }

    public int getNbVictoires() {
        return nbVictoires;
    }

    public void setPositionInitiale(int positionInitiale) {
        this.positionInitiale = positionInitiale;
    }

}
