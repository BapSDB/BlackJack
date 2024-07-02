import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Joueur {

    public enum Type {HUMAIN, IA}

    protected final String nom;
    protected final Paquet pioche;
    protected final List<Carte> main;
    protected int positionInitiale;
    protected int score;
    protected int nbVictoires;
    protected int jetons;
    protected int mise;
    protected final int miseParDefaut;

    protected Joueur(String nom, int jetons) {
        this.nom = nom;
        this.pioche = Paquet.nouveauPaquetNeuf();
        this.main = new ArrayList<>();
        this.jetons = jetons;
        this.miseParDefaut = jetons / 10;
    }

    public static Joueur nouveauCroupier() {
        return new IA("le croupier", 0);
    }

    public static Joueur nouveauCroupier(int jetons) {
        return new IA("le croupier", jetons);
    }

    public static Joueur nouveau(Type type, String nom) {
        return switch (type) {
            case HUMAIN -> new Humain(nom, 0);
            case IA -> new IA(nom, 0);
        };
    }
    public static Joueur nouveau(Type type, String nom, int jetons) {
        return switch (type) {
            case HUMAIN -> new Humain(nom, jetons);
            case IA -> new IA(nom, jetons);
        };
    }

    abstract boolean continuer() throws IOException;
    abstract void miser() throws IOException;

    public void initScore() {
        this.score = 0;
    }

    public void ajouterScore(int score) {
        this.score += score;
    }

    public void ajouterJetons(int jetons) {
        this.jetons += jetons;
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

    public int getJetons() {
        return jetons;
    }

    public int getMise()
    {
        return mise;
    }

    public void setPositionInitiale(int positionInitiale) {
        this.positionInitiale = positionInitiale;
    }

    private static class Humain extends Joueur {
        protected Humain(String nom, int jetons) {
            super(nom, jetons);
        }

        @Override
        boolean continuer() throws IOException {
            final Console console = Console.getInstance();
            System.out.print("Tirer une nouvelle carte ? ");
            final String reponse = console.getBuffer().readLine();
            return console.getRegexReponse().test(reponse);
        }

        @Override
        void miser() throws IOException
        {
            final Console console = Console.getInstance();
            mise = 0;
            while (mise == 0)
            {
                System.out.print("Votre mise ? ");
                final String reponse = console.getBuffer().readLine();
                if (reponse.isEmpty())
                {
                    mise = Math.min(jetons, miseParDefaut);
                    System.out.println("Vous avez choisi la mise par défaut.");
                }
                else
                    try
                    {
                        mise = Integer.parseUnsignedInt(reponse);

                    } catch (NumberFormatException ignored) {}
                if (mise > jetons) {
                    System.out.println("Votre mise est supérieure à la quantité de jetons qu'il vous reste.");
                    mise = 0;
                }
            }
            jetons -= mise;
        }
    }

    private static class IA extends Joueur {
        protected IA(String nom, int jetons) {
            super(nom, jetons);
        }

        @Override
        boolean continuer() throws IOException {
            return true;
        }

        @Override
        void miser() throws IOException
        {
        }
    }

}

