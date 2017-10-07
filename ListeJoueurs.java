
public class ListeJoueurs {

    private Player premier;

    public ListeJoueurs(){}
    
    public ListeJoueurs(String[] ai, String humain, int cave) {
        for (int i = 0; i < ai.length; i++) {
            ajouterJoueur(ai[i], false, cave);
        }
        ajouterJoueur(humain, true, cave);
    }

    public void ajouterJoueur(String nom, boolean estHumain, int argent) {
        Player ancienPremier = premier;
        premier = new Player(nom, estHumain, argent, ancienPremier);
    }

    public Player getPremier() {
        return premier;
    }

    public boolean estVide() {
        return (premier == null);
    }

    public Player getDernier() {
        Player j = premier;
        while (j.getSuivant() != null) {
            j = j.getSuivant();
        }
        return j;
    }

    public void setPremier(String nom) {
        Player j, y, z;
        j = premier;
        if (!j.getNom().equals(nom)) {
            while (j.getSuivant() != null) {
                if (j.getSuivant().getNom().equals(nom)) {
                    break;
                }
                j = j.getSuivant();
            }
            if (j.getSuivant() == null) {
                throw new PasDansListe();
            } else {
                y = j.getSuivant();
                z = getDernier();
                z.setSuivant(getPremier());
                premier = y;
                j.setSuivant(null);
            }
        }
    }

    public Player getJoueur(String n) {
        if (!estVide()) {
            Player j = premier;
            do {
                if (j.getNom().equals(n)) {
                    return j;
                }
                j = j.getSuivant();
            } while (j != null);
        }
        return null;

    }

    public int getNbJoueurs() {
        Player j = premier;
        int i = 0;
        do {
            i++;
            j = j.getSuivant();

        } while (j != null);
        return i;
    }

    public int getNbJoueursHJ() {
        Player j = premier;
        int i = 0;
        do {
            if (j.isFold() || j.isAllIn()) {
                i++;
            }
            j = j.getSuivant();

        } while (j != null);
        return i;
    }

    public int getNbJoueursEnJeu() {
        return getNbJoueurs() - getNbJoueursHJ();
    }

    public void enleverJoueur(String nom) {
        Player j = premier, j1;
        if (j.getNom().equals(nom)) {
            premier = j.getSuivant();
        } else {
            do {
                if (j.getSuivant() != null) {
                    if (j.getSuivant().getNom().equals(nom)) {
                        j.setSuivant(j.getSuivant().getSuivant());
                    }
                }
                j = j.getSuivant();
            } while (j != null);
        }
    }

    public boolean existe(String nom) {
        Player j = premier;
        do {
            if (j.getNom().equals(nom)) {
                return true;
            }
            j = j.getSuivant();
        } while (j != null);
        return false;
    }
       
}

class PasDansListe extends RuntimeException {
}