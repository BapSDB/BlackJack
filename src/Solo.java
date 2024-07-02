import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Solo {
    private final Joueur joueur;
    private final List<Integer> classement;
    private final int nbPartiesGagnantes;
    private int nbTours;
    private int nbParties;
    private boolean perdu;
    private boolean continuer;

    private Solo(Builder builder) {
        this.joueur = builder.joueur;
        this.classement = IntStream.generate(() -> 0).boxed().limit(22).collect(Collectors.toCollection(ArrayList::new));
        this.nbPartiesGagnantes = builder.nbPartiesGagnantes;
    }

    public static class Builder {

        private final Joueur joueur;
        private int nbPartiesGagnantes;

        private Builder() throws IOException {
            final BufferedReader buffer = Console.getInstance().getBuffer();
            String nom;
            do {
                System.out.print("Entrez votre nom : ");
                nom = buffer.readLine();
            } while (nom.isEmpty());
            joueur = Joueur.nouveau(Joueur.Type.HUMAIN, nom);
            nbPartiesGagnantes = 0;
            while (nbPartiesGagnantes == 0) {
                System.out.print("Nombre de Parties Gagnantes ? ");
                try {
                    nbPartiesGagnantes = Integer.parseUnsignedInt(buffer.readLine());
                } catch (NumberFormatException ignored) {
                }
            }
        }

        public Solo getSolo() {
            return new Solo(this);
        }

    }

    private static Solo solo;

    public static Solo getInstance() {
        if (solo == null) {
            try {
                solo = new Builder().getSolo();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return solo;
    }

    private void premierTour() {
        joueur.initScore();
        joueur.initPioche();
        solo.nbTours = 0;
        perdu = false;
        continuer = true;

        // On pioche les deux premières cartes du joueur
        final List<Carte> cartes = joueur.tirerCarte(2);
        final String nom = joueur.getNom();
        final int score = joueur.getScore();
        System.out.println();
        System.out.println(nom + " pioche ses deux premières cartes...");
        for (Carte carte : cartes) {
            System.out.println(nom + " a pioché : " + carte);
            joueur.ajouterScore(carte.valeurCarte(score));
        }
        final int nouveauScore = joueur.getScore();
        System.out.println(nom + " a un score initial de " + nouveauScore + " points.");
        if (nouveauScore == 21) {
            System.out.println(nom + " termine la partie avec un score parfait de 21 points !");
            continuer = false;
        }
    }

    private void tourJoueur() throws IOException {
        final String nom = joueur.getNom();
        final int score = joueur.getScore();

        // On demande au joueur s'il souhaite tirer une carte
        // En cas de 21 ou de dépassement de 21, La partie se termine
        // Et on affiche le message correspondant au résultat
        continuer = joueur.continuer();
        if (continuer) {
            Carte carte = joueur.tirerCarte();
            System.out.println();
            System.out.println(nom + " a pioché : " + carte);
            joueur.ajouterScore(carte.valeurCarte(score));
            final int nouveauScore = joueur.getScore();
            System.out.println(nom + " a un score de " + nouveauScore + " points.\n");
            if (nouveauScore > 21) {
                System.out.println(nom + " a perdu !");
                perdu = true;
                continuer = false;
            } else if (nouveauScore == 21) {
                System.out.println(nom + " termine la partie avec un score parfait de 21 points !");
                continuer = false;
            }
        }
        if (!continuer) System.out.println(nom + " abandonne la partie avec " + joueur.getScore() + " points.");
    }

    private void tourComplet() throws IOException {
        solo.nbTours++;
        System.out.println("\n*******************************\n");
        System.out.println("TOUR " + solo.nbTours + " :");
        tourJoueur();
    }

    private boolean partieFinie() {
        return !continuer;
    }

    private void partieComplete() throws IOException {
        while (!partieFinie()) tourComplet();
        System.out.println("\n*******************************\n");
        System.out.println("FIN DE LA PARTIE\n");

        if (!perdu)
            classement.set(joueur.getScore(), classement.get(joueur.getScore()) + 1);
        else
            classement.set(0, classement.get(0) + 1);

        joueur.incrNbVictoires();
        System.out.print("(Appuyer sur Entrée pour continuer...)");
        Console.getInstance().getBuffer().readLine();
        System.out.println();

        // On affiche les meilleurs scores du joueur
        final List<Map.Entry<Integer, Integer>> score = IntStream.range(0, classement.size())
                .mapToObj(i -> Map.entry(i, classement.get(i)))
                .filter(entry -> entry.getValue() > 0)
                .toList();

        final int nbParties = solo.nbParties;
        System.out.println("Le score de " + joueur.getNom() + " après " +
                nbParties + " partie" + (nbParties > 1 ? "s" : "") +
                " jouée" + (nbParties > 1 ? "s" : " :")
        );

        score.forEach(entry ->
            System.out.println("Score " + entry.getKey() + " -> " + entry.getValue() + " fois")
        );

        System.out.println();

    }

    public void jouer() throws IOException {
        final Console console = Console.getInstance();
        String reponse = "oui";
        while (console.getRegexReponse().test(reponse)) {
            solo.nbParties++;
            System.out.println("\n*******************************\n");
            System.out.println("PARTIE " + solo.nbParties + " :");
            premierTour();
            partieComplete();
            if (joueur.getNbVictoires() >= solo.nbPartiesGagnantes)
                break;
            System.out.println();
            System.out.print("Nouvelle partie ? ");
            reponse = console.getBuffer().readLine();
        }

        System.out.println("\n*******************************\n");
        System.out.println("FIN DU JEU\n");

        // On affiche le ou les noms des joueurs qui ont obtenu le meilleur score après x parties jouées
        final List<Map.Entry<Integer, Integer>> score = IntStream.range(0, classement.size())
                .mapToObj(i -> Map.entry(i, classement.get(i)))
                .filter(entry -> entry.getValue() > 0)
                .toList();

        final int nbParties = solo.nbParties;
        System.out.println("Le score de " + joueur.getNom() + " après " +
                nbParties + " partie" + (nbParties > 1 ? "s" : "") +
                " jouée" + (nbParties > 1 ? "s" : " :")
        );

        score.forEach(entry ->
                System.out.println("Score " + entry.getKey() + " -> " + entry.getValue() + " fois")
        );


    }
}
