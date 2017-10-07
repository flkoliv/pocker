
public class Player {

    private String nom;
    private int cave;
    private int mise;
    private int esperanceGain;
    private boolean coucher;
    private boolean humain;
    private boolean allIn = false;
    private Main hand = new Main();
    private Player suivant;

    public Player(String name, boolean estHumain, int argent, Player joueurSuivant) {
        humain = estHumain;
        cave = argent;
        nom = name;
        coucher = false;
        mise = 0;
        suivant = joueurSuivant;
    }

    Player getSuivant() {
        return suivant;
    }

    void setSuivant(Player j) {
        suivant = j;
    }

    int choix(int miseActuelle, int blind, int nbRaise) {
        boolean continuer;
        int a;
        do {
            continuer = false;
            a = (int) (Math.random() * 3 + 1);

            if ((cave + mise) - miseActuelle <= 0 && a == 2) {
                continuer = true;
            } else if (miseActuelle == 0 && a == 3) {
                continuer = true;
            } else if (a == 2 && nbRaise == 4) {
                continuer = true;
            }
        } while (continuer);
        return a;
    }

    void raise(int miseActuelle, int blind) {
        if ((cave + mise) - miseActuelle > blind) {
            cave = cave - miseActuelle + mise - blind;
            mise = miseActuelle + blind;
            if (cave == 0) {
                allIn = true;
            }
        } else if ((cave + mise) - miseActuelle <= blind && (cave + mise) - miseActuelle > 0) {
            mise = mise + cave;
            cave = 0;
            allIn = true;
        } else {
            throw new PlusDeJetons();
        }
    }

    void setFold() {
        coucher = true;
    }

    boolean isFold() {
        return coucher;
    }

    void setAllIn() {
        allIn = true;
    }

    boolean isAllIn() {
        return allIn;
    }

    boolean isHuman() {
        return humain;
    }

    int getEsperanceGain() {
        return esperanceGain;
    }

    void setEsperanceGain(int m) {
        esperanceGain = m;
    }

    void check(int somme) {
        if (cave == 0) {
            throw new PlusDeJetons();
        } else if ((cave - somme + mise) >= 0) {
            cave = cave - somme + mise;
            mise = somme;
            if (cave == 0) {
                allIn = true;
            }
        } else if ((cave - somme + mise) < 0) {
            mise = mise + cave;
            cave = 0;
            allIn = true;
        }
    }

    int[] getCaveIP() {
        int[] c = new int[1];
        c[0] = cave;
        return c;
    }

    int getCave() {
        return cave;
    }

    int getMise() {
        return mise;
    }

    void setMise(int x) {
        mise = x;
    }

    void ajouteCave(int somme) {
        cave = cave + somme;
    }

    String getNom() {
        return nom;
    }

    Main getMain() {
        return hand;
    }

    boolean horsJeu() {
        if (allIn || coucher) {
            return true;
        } else {
            return false;
        }
    }

    void reinitialiser() {
        mise = 0;
        esperanceGain = 0;
        coucher = false;
        allIn = false;
        hand = new Main();
    }

    private int bluff(int choix) {
        int a = (int) (Math.random() * 100);
        int result = choix;
        if (a >= 90) {
            do {
                result = (int) (Math.random() * 3 + 1);
            } while (result == choix);
        }
        return result;
    }

    private int choixPreFlop() {
        int result = 3;
        int rang = Evaluateur.rang(hand);
        if (rang % 1000000 >= 163 || rang >= 1000009 || rang == 153 || rang == 152) {
            result = 2;
        } else if (rang % 1000000 >= 125 || rang >= 100000) {
            result = 1;
        }
        return result;
    }

    private int choixFlop(int nbRaise) {
        Main m = new Main();
        int[] tabMains = new int[10];
        int[] main = getMain().getCartes();
        int rang1 = Evaluateur.rang(hand);
        for (int j = 2; j < main.length; j++) {
            m.ajouterCarte(main[j]);
        }
        int rang2 = Evaluateur.rang(m);
        for (int j = 0; j < 52; j++) {
            for (int k = j + 1; k < 52; k++) {
                Main mi = new Main();
                for (int q = 0; q < main.length; q++) {
                    mi.ajouterCarte(main[q]);
                }
                if (!mi.contientCarte(j)) {
                    mi.ajouterCarte(j);
                    if (!mi.contientCarte(k)) {
                        mi.ajouterCarte(k);
                        tabMains[(int) (Evaluateur.rang(mi) / 1000000)]++;
                    }
                }
            }
        }
        boolean suivre = false;
        for (int i = 4; i < tabMains.length; i++) {
            if (tabMains[i] > 0) {
                suivre = true;
            }
        }
        if (rang1 > rang2 && rang1 > 2000000) {
            return 2;
        }
        if (rang1 > rang2 && rang1 > 1000000 && nbRaise < 2) {
            return 2;
        } else if (rang1 > rang2 && rang1 > 1000000) {
            return 1;
        } else if (suivre) {
            return 1;
        }
        return 3;
    }

    private int choixTurn(int nbRaise) {
        Main m = new Main();
        int[] tabMains = new int[10];
        int[] main = getMain().getCartes();
        int rang1 = Evaluateur.rang(hand);
        for (int j = 2; j < main.length; j++) {
            m.ajouterCarte(main[j]);
        }
        int rang2 = Evaluateur.rang(m);
        for (int j = 0; j < 52; j++) {
            Main mi = new Main();
            for (int q = 0; q < main.length; q++) {
                mi.ajouterCarte(main[q]);
            }
            if (!mi.contientCarte(j)) {
                mi.ajouterCarte(j);
                tabMains[(int) (Evaluateur.rang(mi) / 1000000)]++;
            }
        }
        boolean suivre = false;
        for (int i = 4; i < tabMains.length; i++) {
            if (tabMains[i] > 0) {
                suivre = true;
            }
        }

        if (rang1 > rang2 && rang1 > 2000000) {
            return 2;
        }
        if (rang1 > rang2 && rang1 > 1000000 && nbRaise < 2) {
            return 2;
        } else if (rang1 > rang2 && rang1 > 1000000) {
            return 1;
        } else if (suivre && nbRaise < 2) {
            return 1;
        }
        return 3;
    }

    private int choixRiver(int nbRaise) {
        Main m = new Main();
        int[] main = getMain().getCartes();
        int rang1 = Evaluateur.rang(hand);
        for (int j = 2; j < main.length; j++) {
            m.ajouterCarte(main[j]);
        }
        int rang2 = Evaluateur.rang(m);
        if (rang1 > rang2 && rang1 > 2000000) {
            return 2;
        }
        if (rang1 > rang2 && rang1 > 1000000 && nbRaise < 2) {
            return 2;
        } else if (rang1 > rang2 && rang1 > 1000000) {
            return 1;
        }
        return 3;
    }

    int choix(Croupier c, ListeJoueurs l, int blind, int nbRaise) {
        int choix = 0;
        int nb = l.getNbJoueurs();
        switch (c.getTourMise()) {
            case 0:
                choix = choixPreFlop();
                break;
            case 1:
                choix = choixFlop(nbRaise);
                break;
            case 2:
                choix = choixTurn(nbRaise);
                break;
            case 3:
                choix = choixRiver(nbRaise);
                break;
        }
        choix = bluff(choix);
        if ((cave + mise) - c.getMiseMax() <= 0 && choix == 2) {
            choix = 1;
        }
        if (nbRaise >= 4 && choix == 2) {
            choix = 1;
        }
        if (c.getMiseMax() == 0 && choix == 3) {
            choix = 1;
        }
        if (c.getMiseMax() == mise && choix == 3) {
            choix = 1;
        }
        if (l.getNbJoueursEnJeu() == 1 && choix == 2) {
            choix = 1;
        }
        return choix;
    }
}

class PlusDeJetons extends RuntimeException {
}