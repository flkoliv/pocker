
public class Main {

    private int[] cartes = new int[8];

    public Main() {
    }

    public Main(int[] tab) {
        for (int i = 0; i < tab.length; i++) {
            cartes[i + 1] = tab[i];
        }
        cartes[0] = tab.length;
    }

    void ajouterCarte(int carte) {
        if (cartes[0] < 7) {
            cartes[cartes[0] + 1] = carte;
            cartes[0]++;
        } else {
            throw new TropDeCartes();
        }
    }

    int nbCartes() {
        return cartes[0];
    }

    int[] affichage() {
        int[] tab = new int[2];
        for (int i = 1, j = 0; (i <= cartes[0]) && (j < 2); i++, j++) {
            tab[j] = Carte.id(cartes[i]);
        }
        return tab;

    }

    int[] getCartes() {
        int[] tab = new int[cartes[0]];
        for (int i = 0; i < tab.length; i++) {
            tab[i] = cartes[i + 1];
        }
        return tab;
    }

    boolean contientCarte(int carte) {
        boolean contient = false;
        for (int i = 1; i < cartes.length; i++) {
            if (cartes[i] == carte) {
                contient = true;
            }
        }
        return contient;
    }
}

class TropDeCartes extends RuntimeException {
}