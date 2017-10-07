import java.util.Vector;
public class Croupier {

    private JeuCarte jeu = new JeuCarte();
    private int[] cartesSurTable = new int[6];
    private int miseMax;
    private int mises = 0;
    private int tourMise = 0;

    void payerBlind(ListeJoueurs l, String donneur, int blind) {
        int x, y;
        Player j = l.getJoueur(donneur);
        if (j.getSuivant() != null) {
            j = j.getSuivant();
        } else {
            j = l.getPremier();
        }
        j.raise(0, blind / 2);
        x = j.getMise();
        if (j.getSuivant() != null) {
            j = j.getSuivant();
        } else {
            j = l.getPremier();
        }
        j.raise(0, blind);
        y = j.getMise();
        if (y > x) {
            miseMax = y;
        } else {
            miseMax = x;
        }
    }

    void donner(ListeJoueurs liste) {
        Player j = liste.getPremier();
        do {
            j.getMain().ajouterCarte(jeu.donnerCarte());
            j.getMain().ajouterCarte(jeu.donnerCarte());
            j = j.getSuivant();
        } while (j != null);

    }

    void donnerFlop(ListeJoueurs liste) {
        for (int i = 0; i < 3; i++) {
            int c = jeu.donnerCarte();
            cartesSurTable[i + 1] = c;
            cartesSurTable[0]++;
            Player j = liste.getPremier();
            do {
                j.getMain().ajouterCarte(c);
                j = j.getSuivant();
            } while (j != null);
        }
    }

    void donnerTurnRiver(ListeJoueurs liste) {
        int c = jeu.donnerCarte();
        Player j = liste.getPremier();
        cartesSurTable[cartesSurTable[0] + 1] = c;
        cartesSurTable[0]++;
        do {
            j.getMain().ajouterCarte(c);
            j = j.getSuivant();
        } while (j != null);
    }

    int[] getCartesSurTable() {
        int[] tab = new int[cartesSurTable[0]];
        for (int i = 1, j = 0; i <= cartesSurTable[0]; i++, j++) {
            tab[j] = Carte.id(cartesSurTable[i]);
        }
        return tab;
    }

    int getTourMise() {
        return tourMise;
    }

    int getMiseMax() {
        return miseMax;
    }

    void setMiseMax(int mise) {
        miseMax = mise;
    }

    void melanger() {
        jeu.melanger();
    }

    int[] ramasserMises(ListeJoueurs l) {
        Player j = l.getPremier();
        int[] tab = new int[1];
        do {
            mises = mises + j.getMise();
            j.setMise(0);
            j = j.getSuivant();
        } while (j != null);
        miseMax = 0;
        tab[0] = mises;
        tourMise++;
        return tab;
    }

    String payerGagnant(ListeJoueurs l) {
        String chaine ="";
        
        do {
            Vector<Player> v = new Vector<Player>();
            Player j = l.getPremier();
            int nbEgalite = 1;
            int score = 0;
            do {
                int rang = Evaluateur.rang(j.getMain());
                if (!j.isFold() && rang > score) {
                    score = rang;
                    nbEgalite = 1;
                    v.clear();
                    v.add(j);
                } else if (!j.isFold() && rang == score) {
                    nbEgalite++;
                    int x=-1;
                    for (int i=0;i<v.size();i++){
                        Player p =v.elementAt(i);
                        if (p.getEsperanceGain()<j.getEsperanceGain()){
                            x=i+1;
                        }
                    }
                    if (x==-1)v.add(0,j);
                    else v.add(x,j);
                }
                j = j.getSuivant();
            } while (j != null);
            if (nbEgalite > 1) {
                chaine = chaine + "++++++++++ Egalite.\n";
            }
            for (int i=0;i<v.size();i++){
                Player p = v.elementAt(i);
                if (p.getEsperanceGain() < mises) {
                     int somme =(int) p.getEsperanceGain() / nbEgalite;
                     chaine =chaine + "+++++ "+p.getNom() + " gagne "+somme+" avec " + Evaluateur.nomMain(p.getMain()) + ". (" + Evaluateur.meilleurMain(p.getMain()) + ")\n";
                     p.ajouteCave(somme);
                     mises = mises - somme;
                } else {
                     int somme = (int) mises / nbEgalite;
                     chaine =chaine +  "+++++ "+p.getNom() + " gagne "+somme+" avec " + Evaluateur.nomMain(p.getMain()) + ". (" + Evaluateur.meilleurMain(p.getMain()) + ")\n";
                     p.ajouteCave(somme);
                     mises = mises - somme;
                }
                p.setFold();
                nbEgalite--;
                if (nbEgalite == 0)nbEgalite = 1;
            }
       } while (mises != 0);
        return chaine;
    }
}
