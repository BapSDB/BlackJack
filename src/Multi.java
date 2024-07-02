import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Multi {

    private final List<Joueur> joueurs;
    private final List<Joueur> joueursEnLice;
    private final List<List<Joueur>> classement;
    private final int nbJoueurs;
    private final int nbPartiesGagnantes;
    private int nbTours;
    private int nbParties;
    private int joueurCourant;

    private Multi(Builder builder) {
        this.joueurs = builder.joueurs;
        this.joueursEnLice = new ArrayList<>(builder.nbJoueurs);
        this.classement = Stream.<List<Joueur>>generate(ArrayList::new).limit(22).toList();
        this.nbPartiesGagnantes = builder.nbPartiesGagnantes;
        this.nbJoueurs = builder.nbJoueurs;
    }

    public static class Builder {

        private final List<Joueur> joueurs;
        private int nbJoueurs;
        private int nbPartiesGagnantes;

        private Builder() throws IOException {
            final BufferedReader buffer = Console.getInstance().getBuffer();
            nbJoueurs = 0;
            nbPartiesGagnantes = 0;
            while (nbJoueurs == 0) {
                System.out.print("Nombre de joueurs ? ");
                try {
                    nbJoueurs = Integer.parseUnsignedInt(buffer.readLine());
                } catch (NumberFormatException ignored) {
                }
            }
            while (nbPartiesGagnantes == 0) {
                System.out.print("Nombre de Parties Gagnantes ? ");
                try {
                    nbPartiesGagnantes = Integer.parseUnsignedInt(buffer.readLine());
                } catch (NumberFormatException ignored) {
                }
            }
            joueurs = new ArrayList<>(nbJoueurs);
            final List<String> noms = new ArrayList<>(nbJoueurs);
            for (int i = 1; i <= nbJoueurs; i++) {
                String nom;
                System.out.println();
                int num = -1;
                do {
                    System.out.print("Entrez le nom du joueur " + i + " : ");
                    nom = buffer.readLine();
                    if (nom.isEmpty()) continue;
                    if ((num = noms.indexOf(nom)) != -1)
                        System.out.println("le nom « " + nom + " » a déjà été saisi par le joueur " + (num + 1));
                } while (num != -1);
                noms.add(nom);
                joueurs.add(Joueur.nouveau(Joueur.Type.HUMAIN, nom));
            }
        }

        public Multi getMulti() {
            return new Multi(this);
        }


    }

    private static Multi multi;

    public static Multi getInstance() {
        if (multi == null) {
            try {
                multi = new Builder().getMulti();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return multi;
    }

    private void premierTour() {
        final List<Joueur> joueurs = multi.joueursEnLice;
        final List<List<Joueur>> classement = multi.classement;
        joueurs.addAll(multi.joueurs);
        joueurs.forEach(Joueur::initPioche);
        joueurs.forEach(Joueur::initScore);
        classement.forEach(List::clear);
        multi.nbTours = 0;

        // Permutation aléatoire de l'ordre de passage des joueurs
        Collections.shuffle(joueurs, Utility.getInstance().getRandom());
        IntStream.range(0, multi.nbJoueurs)
                .forEachOrdered(i -> joueurs.get(i).setPositionInitiale(i));
        System.out.println("Le tirage au sort a donné l'ordre suivant : " +
                joueurs.stream()
                        .map(Joueur::getNom)
                        .collect(Collectors.joining(", "))
        );

        // On pioche les deux premières cartes pour chaque joueur
        Iterator<Joueur> it = joueurs.iterator();
        while (it.hasNext()) {
            final Joueur joueur = it.next();
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
                it.remove();
            }
            classement.get(nouveauScore).add(joueur);
        }
    }

    private List<Joueur> joueursEnTete() {
        final List<List<Joueur>> classement = multi.classement;
        ListIterator<List<Joueur>> it = classement.listIterator(classement.size());
        while (it.hasPrevious()) {
            final List<Joueur> joueurs = it.previous();
            if (!joueurs.isEmpty())
                return joueurs;
        }
        throw new NoSuchElementException();
    }

    private Joueur departager(List<Joueur> finalistes) throws IOException {
        System.out.println();
        System.out.println(finalistes.size() + " joueurs sont à égalité.");
        System.out.println("Celui qui tire la carte de plus haute valeur remporte la partie !");

        // On fait piocher une carte à chacun des joueurs à égalité
        // La carte de plus haute valeur désigne le vainqueur
        // À chaque tour, on conserve les joueurs qui ont la même carte de plus haute valeur
        // Puis on recommence jusqu'au vainqueur incontesté
        boolean fin = false;
        Map.Entry<Joueur, Carte> gagnant = null;
        finalistes.sort(Comparator.comparing(Joueur::getPositionInitiale));
        while (!fin) {
            gagnant = null;
            int joueurCourant = 0;
            final int nombreTours = finalistes.size();
            for (int i = 0; i < nombreTours; i++) {
                final Joueur finaliste = finalistes.get(joueurCourant);
                final String nom = finaliste.getNom();
                final Carte carte = finaliste.tirerCarte();
                System.out.print("Attention, " + nom + " ! *Roulement de tambour* (Appuyer sur Entrée pour continuer...)");
                Console.getInstance().getBuffer().readLine();
                System.out.println(nom + " a pioché : " + carte);
                if (gagnant == null || carte.valeur().compareTo(gagnant.getValue().valeur()) > 0) {
                    gagnant = Map.entry(finaliste, carte);
                    joueurCourant++;
                    fin = true;
                } else if (carte.valeur().compareTo(gagnant.getValue().valeur()) < 0) {
                    finalistes.remove(joueurCourant);
                } else {
                    joueurCourant++;
                    fin = false;
                }
            }
        }
        final String nom = gagnant.getKey().getNom();
        System.out.println();
        System.out.println(nom + " a tiré la carte de plus haute valeur : " + gagnant.getValue());
        System.out.println(nom + " est le gagnant au départage !");
        return gagnant.getKey();
    }

    private void tourJoueur() throws IOException {
        final List<Joueur> joueurs = multi.joueursEnLice;
        final Joueur joueur = joueurs.get(multi.joueurCourant);
        final String nom = joueur.getNom();
        final int score = joueur.getScore();
        final List<List<Joueur>> classement = multi.classement;

        // Si le joueur courant est le plus proche de 21 avant tirage, on l'affiche
        System.out.println();
        if (joueursEnTete().contains(joueur))
            System.out.println(nom + " est le plus proche de 21 avec un score de " + score + " points.");
        else
            System.out.println(nom + " a un score de " + score + " points.");

        // On supprime le score d'avant tirage du classement
        classement.get(score).remove(joueur);

        // On demande au joueur courant s'il souhaite tirer une carte
        // En cas de 21 ou de dépassement de 21, le joueur se retire de la liste des joueurs en lice
        // Et on affiche le message correspondant au résultat
        boolean perdu = false, continuer = joueur.continuer();
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
        if (!perdu)
            classement.get(joueur.getScore()).add(joueur);
        if (!continuer) {
            joueurs.remove(multi.joueurCourant);
            System.out.println(nom + " abandonne la partie avec " + joueur.getScore() + " points.");
        } else
            multi.joueurCourant++;
    }

    private void tourComplet() throws IOException {
        multi.nbTours++;
        System.out.println("\n*******************************\n");
        System.out.println("TOUR " + multi.nbTours + " :");

        // On affiche l'ordre de passage des joueurs toujours en lice
        // Et leur score
        final List<Joueur> joueurs = multi.joueursEnLice;
        int n = joueurs.size();
        System.out.println(n + " joueur" + (n > 1 ? "(s)" : "") + " : " +
                joueurs.stream()
                        .map(joueur -> joueur.getNom() + "(" + joueur.getScore() + ")")
                        .collect(Collectors.joining(", ")) +
                (n > 1 ? " sont" : " est") +
                " toujours en lice pour améliorer" +
                (n > 1 ? " leur" : " son") + " score !"
        );

        // On affiche les noms des joueurs dont le score est le plus proche de 21
        // Et leur score
        final List<Joueur> meilleursJoueurs = joueursEnTete();
        final Joueur premier = meilleursJoueurs.get(0);
        n = meilleursJoueurs.size();
        System.out.println((n > 1 ? "Les" : "Le") +
                (n > 1 ? " joueurs " : " joueur ") +
                (n < 2 ? premier.getNom() :
                        meilleursJoueurs.stream()
                                .map(Joueur::getNom)
                                .limit(n - 1)
                                .collect(Collectors.joining(", "))) +
                (n > 1 ? " et " + meilleursJoueurs.get(n - 1).getNom() : "") +
                (n > 1 ? " sont" : " est") +
                " en tête avec " + premier.getScore() + " points."
        );

        // On fait jouer un tour à chaque joueur toujours en lice
        multi.joueurCourant = 0;
        final int nombreTours = multi.joueursEnLice.size();
        for (int i = 0; i < nombreTours; i++) tourJoueur();
    }

    private boolean partieFinie() {
        return multi.joueursEnLice.isEmpty();
    }

    private void partieComplete() throws IOException {
        while (!partieFinie()) tourComplet();
        System.out.println("\n*******************************\n");
        System.out.println("FIN DE LA PARTIE\n");

        // On affiche les joueurs dans l'ordre croissant des scores qui n'ont pas dépassé 21
        // Et qui sont donc toujours en lice pour la victoire finale
        System.out.println("Les joueurs toujours en lice pour la victoire finale sont :");
        for (List<Joueur> joueurs : multi.classement)
            if (!joueurs.isEmpty())
                joueurs.stream()
                        .map(joueur -> joueur.getNom() + " avec " + joueur.getScore() + " points.")
                        .forEach(System.out::println);

        // On identifie les joueurs qui ont obtenu le meilleur score
        final List<Joueur> finalistes = joueursEnTete();
        Joueur premier = finalistes.get(0);

        // S'il y en a plus d'un, on affiche le nom du vainqueur après départage
        // Sinon, on affiche le nom du vainqueur incontesté
        if (finalistes.size() > 1)
            premier = departager(finalistes);
        else
            System.out.println(premier.getNom() + " est le gagnant incontesté !");

        // On incrémente le nombre de victoires du vainqueur
        premier.incrNbVictoires();
        System.out.print("(Appuyer sur Entrée pour continuer...)");
        Console.getInstance().getBuffer().readLine();
        System.out.println();

        // On affiche la liste des joueurs et le nombre de leurs victoires
        // Du plus petit nombre au plus grand
        System.out.println("Les scores de victoires sont : ");
        multi.joueurs.stream()
                .sorted(Comparator.comparing(Joueur::getNbVictoires))
                .map(joueur -> joueur.getNom() + " avec " + joueur.getNbVictoires() + " victoire(s)")
                .forEach(System.out::println);
        System.out.println();

        // On affiche le ou les noms des joueurs qui ont obtenu le meilleur score après x parties jouées
        final Map<Integer, List<String>> classement = multi.joueurs.stream()
                .collect(Collectors.groupingBy(
                        Joueur::getNbVictoires,
                        Collectors.mapping(Joueur::getNom, Collectors.toList())
                ));
        final Map.Entry<Integer, List<String>> meilleursJoueurs = classement.entrySet().stream().max(Map.Entry.comparingByKey()).orElseThrow();
        final int meilleurScore = meilleursJoueurs.getKey();
        final List<String> noms = meilleursJoueurs.getValue();
        final int n = noms.size();
        final int nbParties = multi.nbParties;
        System.out.println((n > 1 ? "Les" : "Le") +
                (n > 1 ? " joueurs " : " joueur ") +
                (n < 2 ? noms.get(0) : String.join(", ", noms.subList(0, n - 1))) +
                (n > 1 ? " et " + noms.get(n - 1) : "") +
                (n > 1 ? " sont" : " est") + " en tête avec " + meilleurScore + " victoire" +
                (meilleurScore > 1 ? "s" : "") + (n > 1 ? " chacun" : "") + " après " +
                nbParties + " partie" + (nbParties > 1 ? "s" : "") + " jouée" + (nbParties > 1 ? "s." : ".")
        );
    }

    public void jouer() throws IOException {
        final Console console = Console.getInstance();
        Optional<Joueur> meilleurJoueur = Optional.empty();
        String reponse = "oui";
        while (console.getRegexReponse().test(reponse)) {
            multi.nbParties++;
            System.out.println("\n*******************************\n");
            System.out.println("PARTIE " + multi.nbParties + " :");
            premierTour();
            partieComplete();
            meilleurJoueur = multi.joueurs.stream()
                    .dropWhile(joueur -> joueur.getNbVictoires() < multi.nbPartiesGagnantes)
                    .findFirst();
            if (meilleurJoueur.isPresent())
                break;
            System.out.println();
            System.out.print("Nouvelle partie ? ");
            reponse = console.getBuffer().readLine();
        }

        System.out.println("\n*******************************\n");
        System.out.println("FIN DU JEU\n");
        if (meilleurJoueur.isPresent()) {
            final Joueur joueur = meilleurJoueur.get();
            System.out.println("BRAVO, " + joueur.getNom() + " !!! tu remportes le BlackJack !!!");
            System.out.println();
        }

        // On affiche le ou les noms des joueurs qui ont obtenu le meilleur score après x parties jouées
        final Map<Integer, List<String>> classement = multi.joueurs.stream()
                .collect(Collectors.groupingBy(
                        Joueur::getNbVictoires,
                        Collectors.mapping(Joueur::getNom, Collectors.toList())
                ));
        final Map.Entry<Integer, List<String>> meilleursJoueurs = classement.entrySet().stream().max(Map.Entry.comparingByKey()).orElseThrow();
        final int meilleurScore = meilleursJoueurs.getKey();
        final List<String> noms = meilleursJoueurs.getValue();
        final int n = noms.size();
        final int nbParties = multi.nbParties;
        System.out.println((n > 1 ? "Les" : "Le") +
                (n > 1 ? " joueurs " : " joueur ") +
                (n < 2 ? noms.get(0) : String.join(", ", noms.subList(0, n - 1))) +
                (n > 1 ? " et " + noms.get(n - 1) : "") +
                (n > 1 ? " sont" : " est") + " en tête avec " + meilleurScore + " victoire" +
                (meilleurScore > 1 ? "s" : "") + (n > 1 ? " chacun" : "") + " après " +
                nbParties + " partie" + (nbParties > 1 ? "s" : "") + " jouée" + (nbParties > 1 ? "s." : ".")
        );

    }
}
