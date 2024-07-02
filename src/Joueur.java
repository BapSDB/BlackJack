import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Joueur {

    public enum Type {HUMAIN, IA}

    private final String nom;
    private final Paquet pioche;
    private final List<Carte> main;
    private int positionInitiale;
    private int score;
    private int nbVictoires;

    protected Joueur(String nom) {
        this.nom = nom;
        this.pioche = Paquet.nouveauPaquetNeuf();
        this.main = new ArrayList<>();
    }

    public static Joueur nouveauCroupier() {
        return new IA("le croupier");
    }

    public static Joueur nouveau(Type type, String nom) {
        return switch (type) {
            case HUMAIN -> new Humain(nom);
            case IA -> new IA(nom);
        };
    }

    abstract boolean continuer() throws IOException;

    public void initScore() {
        this.score = 0;
    }

    public void ajouterScore(int score) {
        this.score += score;
    }

    public Carte tirerCarte() {
        final Carte carte = pioche.piocherCarte();
        main.add(carte);
        return carte;
    }

    public List<Carte> tirerCarte(int nbCarte) {
        final List<Carte> cartes = pioche.piocherCarte(nbCarte);
        main.addAll(cartes);
        return cartes;
    }

    public void initPioche() {
        pioche.fusionnerCartes(main);
        pioche.melangerCartes();
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

    public int getScore() {
        return score;
    }

    public int getNbVictoires() {
        return nbVictoires;
    }

    public void setPositionInitiale(int positionInitiale) {
        this.positionInitiale = positionInitiale;
    }

    private static class Humain extends Joueur {
        protected Humain(String nom) {
            super(nom);
        }

        @Override
        boolean continuer() throws IOException {
            final Console console = Console.getInstance();
            System.out.print("Tirer une nouvelle carte ? ");
            final String reponse = console.getBuffer().readLine();
            return console.getRegexReponse().test(reponse);
        }
    }

    private static class IA extends Joueur {
        protected IA(String nom) {
            super(nom);
        }

        @Override
        boolean continuer() throws IOException {
            return true;
        }
    }

}

