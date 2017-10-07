
public class JeuCarte {

    public static final int NB_CARTES = 52;
    private int[] jeu;
    private int pos = 0;

    public JeuCarte() {
        jeu = new int[NB_CARTES];
    }

    void melanger() {
        boolean carteExiste;
        int carte;
        for (int i = 0; i < NB_CARTES; i++) {
            do {
                carteExiste = false;
                carte = (int) (Math.random() * NB_CARTES);
                for (int j = 0; j < i; j++) {
                    if (carte == jeu[j]) {
                        carteExiste = true;
                    }
                }

            } while (carteExiste);
            jeu[i] = carte;
        }
    }

    int donnerCarte() {
        pos++;
        if (pos > NB_CARTES) {
            throw new PlusDeCartes();
        }
        return jeu[pos - 1];
    }
}

class PlusDeCartes extends RuntimeException {
}