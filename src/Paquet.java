import java.util.*;

public class Paquet implements Iterable<Carte> {

    public static final int NB_MAX_CARTES = 52;

    @Override
    public Iterator<Carte> iterator() {
        return cartes.iterator();
    }

    private final List<Carte> cartes;

    private Paquet() {
        this.cartes = new ArrayList<>(NB_MAX_CARTES);
    }

    public Paquet(Paquet paquet) {
        this.cartes = new ArrayList<>(paquet.cartes);
    }

    public static Paquet nouveauPaquetVide(Random random) {
        return new Paquet();
    }

    public static Paquet nouveauPaquetNeuf() {
        Paquet paquet = new Paquet();
        for (Carte.Valeur valeur : Carte.Valeur.values())
            for (Carte.Couleur couleur : Carte.Couleur.values())
                paquet.cartes.add(new Carte(valeur, couleur));
        return paquet;
    }

    public void melangerCartes() {
        Collections.shuffle(cartes, Utility.getInstance().getRandom());
    }

    public void fusionnerCartes(List<Carte> cartes) {
        final Iterator<Carte> it = cartes.iterator();
        while (it.hasNext()) {
            this.cartes.add(Utility.getInstance().getRandom().nextInt(this.cartes.size()), it.next());
            it.remove();
        }
    }

    public int size()
    {
        return cartes.size();
    }

    public Carte getCarte(int i) {
        return cartes.get(i);
    }

    public List<Carte> piocheCarte() {
        return piocheCarte(1);
    }

    public List<Carte> piocheCarte(int x) {
        if (x < 1)
            throw new IllegalArgumentException(
                    "On doit piocher un nombre de carte strictement positif (x == " + x + ")"
            );
        List<Carte> cartesPiochees = new ArrayList<>(cartes.subList(0, x));
        cartes.removeAll(cartesPiochees);
        return cartesPiochees;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{\n");

        for (Carte carte : cartes) {
            stringBuilder.append(carte);
            stringBuilder.append(",\n");
        }

        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
