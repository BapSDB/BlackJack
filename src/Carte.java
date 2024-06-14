import java.io.BufferedReader;
import java.io.IOException;

public record Carte(Valeur valeur, Couleur couleur) {

    public enum Valeur {
        DEUX, TROIS, QUATRE, CINQ, SIX, SEPT, HUIT, NEUF, DIX, VALET, DAME, ROI, AS
    }

    public enum Couleur {
        CARREAU, COEUR, PIQUE, TREFLE
    }

    public int valeurCarte(int score) {
        if (valeur != Valeur.AS)
            return Math.min(valeur.ordinal() + 2, 10);
        if (score <= 10)
            return 11;
        return 1;
    }

    public int valeurCarte(BufferedReader buffer) throws IOException {
        if (valeur != Valeur.AS)
            return Math.min(valeur.ordinal() + 2, 10);
        int valAS = 0;
        do {
            System.out.print("Pour l'AS, choisir 1 ou 11 : ");
            try {
                valAS = Integer.parseUnsignedInt(buffer.readLine());
            } catch (NumberFormatException ignored) {
            }
        } while (valAS != 1 && valAS != 11);
        return valAS;
    }

    @Override
    public String toString() {
        return valeur + " de " + couleur;
    }
}
