
import java.awt.*;
import javax.swing.*;

class Cartes {

    protected static ImageIcon[][] cartes = new ImageIcon[4][13];
    protected static ImageIcon aPlat = new ImageIcon("cards/back-90.gif");
    protected static int nbCartes;

    protected static void init() {
        String suits = "shdc";
        String faces = "a23456789tjqk";
        int cardPosition = 0;
        cartes = new ImageIcon[4][13];
        for (int suit = 0; suit < suits.length(); suit++) {
            for (int face = 0; face < 13; face++) {
                cartes[suit][face] = new ImageIcon("cards/" + faces.charAt(face) + suits.charAt(suit) + ".gif");
            }
        }
    }

    protected static ImageIcon donneCarte(int n) {
        return cartes[n / 100][n % 100];
    }

    protected static void affiche(JPanel jp, Graphics g, int[] lcs, int dx,
            int dy) {
        for (int i = 0; i < lcs.length; i++) {
            donneCarte(lcs[i]).paintIcon(jp, g, dx + (i * 76), dy);
        }
    }

    protected static void afficheCompact(JPanel jp, Graphics g, int[] lcs,
            int dx, int dy) {
        if (nbCartes == 2) {
            affiche(jp, g, lcs, dx, dy);
        } else {
            for (int i = 0; i < lcs.length; i++) {
                donneCarte(lcs[i]).paintIcon(jp, g, dx + (i * 30), dy + (i * 5));
            }
        }
    }
}

class Flop {

    protected int[] cartes = new int[0];
    private int posX,  posY;

    Flop(int x, int y) {
        posX = x;
        posY = y;
    }

    protected void fixeJeu(int[] j) {
        cartes = j;
    }

    protected void addCarte(int ni) {
        int[] newc = new int[cartes.length + 1];
        for (int i = 0; i < cartes.length; i++) {
            newc[i] = cartes[i];
        }
        newc[cartes.length] = ni;
        cartes = newc;
    }

    protected void affiche(JPanel jp, Graphics g) {
        Cartes.affiche(jp, g, cartes, posX, posY);
    }
}

class Mises {

    protected Jetons jetons;
    protected Jetons nouvelle;
    protected boolean empty,  jet;
    protected int nbCartes,  posX,  posY;

    Mises(int x, int y) {
        empty = true;
        jet = false;
        posX = x;
        posY = y;
        if (Jetons.enChiffres()) {
            jetons = new Jetons(posX, posY + 40);
            nouvelle = new Jetons(posX + 100, posY + 40);
        } else {
            jetons = new Jetons(posX, posY);
            nouvelle = new Jetons(posX + Jetons.largeur() + 10, posY);
        }
    }

    protected void fixeJetons(int[] j) {
        jetons.fixe(j);
    }

    protected void jetteCartes(int n) {
        empty = false;
        jet = true;
        nbCartes = n;
    }

    protected void nouvelleMise(int[] ls) {
        empty = false;
        jet = false;
        nouvelle.fixe(ls);
    }

    protected void affiche(JPanel jf, Graphics g) {
        jetons.affiche(jf, g);
        int dx;
        if (Jetons.enChiffres()) {
            dx = posX + 70;
        } else {
            dx = posX + jetons.largeur() + 10;
        }
        if (!empty) {
            if (jet) {
                for (int i = 0; i < nbCartes; i++) {
                    Cartes.aPlat.paintIcon(jf, g, dx + (3 * i), posY - (3 * i));
                }
            } else {
                nouvelle.affiche(jf, g);
            }
        }
    }
}

class Humain {

    protected int[] cartes = new int[0];
    protected Jetons jetons;
    private int posX,  posY;

    Humain(int x, int y) {
        posX = x;
        posY = y;
        if (Jetons.enChiffres()) {
            jetons = new Jetons(posX + (Cartes.nbCartes * 76) + 10, posY + 20);
        } else {
            jetons = new Jetons(posX + (Cartes.nbCartes * 76) + 10, posY);
        }
    }

    protected void fixeJeu(int[] j) {
        cartes = j;
    }

    protected void fixeJetons(int[] j) {
        jetons.fixe(j);
    }

    protected void modifieJetons(int[] j) {
        jetons.modifie(j);
    }

    protected void affiche(JPanel jf, Graphics g) {
        Cartes.affiche(jf, g, cartes, posX, posY);
        jetons.affiche(jf, g);
    }
}

class Jetons {

    protected static int[] couleurs = {0, 1, 2, 3, 4};
    protected static int nbCol,  larg;
    protected static ImageIcon[][] piles = new ImageIcon[5][25];
    private int[] lesjetons;
    private int posX,  posY;

    Jetons(int x, int y) {
        posX = x;
        posY = y;
        if (nbCol == 0) {
            lesjetons = new int[1];
        } else {
            lesjetons = new int[nbCol];
        }
    }

    Jetons(int x, int y, int[] nj) {
        posX = x;
        posY = y;
        fixe(nj);
    }

    protected static int largeur() {
        return larg;
    }

    protected static boolean enChiffres() {
        return nbCol == 0;
    }

    protected static void init(int ni) {
        nbCol = ni;
        larg = 70 * ni;
        char[] col = {'r', 'j', 'b', 'w', 'g'};
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 25; j++) {
                piles[i][j] = new ImageIcon("icons/pile_" + col[i] + "_" +
                        (j + 1) + ".gif");
            }
        }
    }

    protected static void afficheUneCouleur(JPanel jf, Graphics g, int dx,
            int dy, int j, int idx) {
        if (j > 150) {
            j = 150;
        } else if (j == 0) {
            return;
        }
        if (j <= 25) {
            piles[idx][j - 1].paintIcon(jf, g, dx, dy);
            return;
        } else {
            piles[idx][24].paintIcon(jf, g, dx, dy);
            if (j <= 50) {
                piles[idx][j - 26].paintIcon(jf, g, dx + 33, dy);
                return;
            } else {
                piles[idx][24].paintIcon(jf, g, dx + 33, dy);
                afficheUneCouleur(jf, g, dx + 4, dy + 8, j - 50, idx);
            }
        }
    }

    protected void affiche(JPanel jf, Graphics g) {
        if (enChiffres()) {
            g.drawString("" + lesjetons[0], posX, posY);
        } else {
            for (int i = nbCol - 1; i >= 0; i--) {
                afficheUneCouleur(jf, g, posX + 70 * i, posY, lesjetons[i],
                        couleurs[i]);
            }
        }
    }

    protected void fixe(int[] js) {
        lesjetons = new int[js.length];
        for (int i = 0; i < js.length; i++) {
            lesjetons[i] = js[i];
        }
    }

    protected void modifie(int[] js) {
        for (int i = 0; i < Jetons.nbCol; i++) {
            lesjetons[i] = lesjetons[i] + js[i];
        }
        if (nbCol == 0) {
            lesjetons[0] = lesjetons[0] + js[0];
        }
    }

    protected int donneChiffre() {
        return lesjetons[0];
    }
}

class Jeux {

    static ImageIcon[] jeu = new ImageIcon[5];

    static void init() {
        for (int i = 0; i < 5; i++) {
            jeu[i] = new ImageIcon("icons/fjeu" + (i + 1) + ".gif");
        }
    }
}

class Joueur {

    Jetons jetons;
    String nom;
    int posX, posY;
    int[] cartes;
    boolean cache, enjeu;
    int nbCartes;

    Joueur(String n, int[] jts, int dx, int dy) {
        nom = n;
        posX = dx;
        posY = dy;
        if (Jetons.enChiffres()) {
            jetons = new Jetons(posX + 10, posY + 145, jts);
        } else {
            jetons = new Jetons(posX + 200, posY + 25, jts);
        }
        cache = true;
        enjeu = true;
        nbCartes = Cartes.nbCartes;
    }

    void affiche(JPanel jf, Graphics g) {
        if (cache) {
            Jeux.jeu[nbCartes - 1].paintIcon(jf, g, posX, posY);
        } else if (enjeu) {
            Cartes.afficheCompact(jf, g, cartes, posX, posY);
        }

        if (Jetons.enChiffres()) {
            g.drawString(nom + " " + jetons.donneChiffre(), posX + 20, posY + 155);

        } else {
            g.drawString(nom, posX + 200, posY + 15);
            jetons.affiche(jf, g);
        }

    }

    void modifieNbJetons(int[] delta) {
        jetons.modifie(delta);
    }

    void fixeNbJetons(int[] nbj) {
        jetons.fixe(nbj);
    }

    void fixeNbCartes(int n) {
        nbCartes = n;
    }

    void abat(int[] cs) {
        cartes = cs;
        cache = false;
    }

    void abandonne() {
        cache = false;
        enjeu = false;
    }
}

// ====================================================================
public class InterfacePoker extends JFrame {

    private Joueur[] joueur;
    private int nbj = 0;
    private Humain humain;
    private Mises mises;
    private Flop flop;
    private SpecialPanel jpane;
    private int xGrille,  yGrille,  largJ,  hauteurJ,  larg,  haut;
    private boolean isTexas;

    InterfacePoker(int x, int y, boolean isTHE, int nbCol) {
        this.setTitle("Table de Poker");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jpane = new SpecialPanel();
        this.setContentPane(jpane);
        // initialisation de variables
        xGrille = x;
        yGrille = y;
        isTexas = isTHE;
        joueur = new Joueur[x * y];
        // calcul taille de la fenêtre
        int largeurFlop, largeurMise;
        if (isTHE) {
            largeurFlop = 380;
            Cartes.nbCartes = 2;
        } else {
            largeurFlop = 0;
            Cartes.nbCartes = 5;
        }
        if (nbCol != 0) {
            largeurMise = (70 * nbCol * 2) + 30;
            larg = 20 + x * (200 + 70 * nbCol + 20);
            haut = y * (150) + 270;
            hauteurJ = 150;
            largJ = 200 + 70 * nbCol;
            if (larg < 380 + (70 * nbCol) + 40) {
                larg = 380 + (70 * nbCol) + 40;
            }
        } else {
            largeurMise = 150;
            hauteurJ = 190;
            largJ = 200;
            larg = 20 + x * 220;
            haut = y * 190 + 270;
            if (larg < 420) {
                larg = 420;
            }
        }
        if (larg < largeurFlop + largeurMise + 20) {
            larg = largeurFlop + largeurMise + 20;
        }
        jpane.setPreferredSize(new Dimension(larg, haut));
        jpane.setBackground(Color.green);
        // initialisation de classes
        Jeux.init();
        Jetons.init(nbCol);
        mises = new Mises(largeurFlop + 20, hauteurJ * yGrille + 20);
        humain = new Humain((larg - (Cartes.nbCartes * 76) - Jetons.largeur()) / 2, yGrille * hauteurJ + 140);
        flop = new Flop(10, yGrille * hauteurJ + 20);
        Cartes.init();
    }

    class SpecialPanel extends JPanel {

        Font font;

        public void paintComponent(Graphics g) {
            super.paintComponent(g);   // Required
            if (font == null) {
                font = new Font(g.getFont().getName(),
                        g.getFont().getStyle(),
                        g.getFont().getSize() * 2);
            }
            g.setFont(font);
            for (int i = 0; i < nbj; i++) {
                joueur[i].affiche(this, g);
            }
            if (isTexas) {
                flop.affiche(this, g);
            }
            mises.affiche(this, g);
            humain.affiche(this, g);
        }
    }

    public void afficheFenetre() {
        this.pack();
        this.setVisible(true);
    }

    public void declareJoueur(String nom, int[] jetons) {
        joueur[nbj] = new Joueur(nom, jetons,
                10 + (nbj % xGrille) * (largJ + 20),
                10 + (nbj / xGrille) * hauteurJ);
        nbj++;
        repaint();
    }

    private int indexFromName(String n) {
        for (int i = 0; i < nbj; i++) {
            if (joueur[i].nom.equals(n)) {
                return i;
            }
        }
        throw new PasTrouve();
    }

    public void joueurAbat(String n, int[] cartes) {
        int idx = indexFromName(n);
        joueur[idx].abat(cartes);
        repaint();
    }

    public void joueurAbandonne(String n) {
        int idx = indexFromName(n);
        joueur[idx].abandonne();
        repaint();
    }

    public void joueurModifieNbJetons(String n, int[] delta) {
        int idx = indexFromName(n);
        joueur[idx].modifieNbJetons(delta);
        repaint();
    }

    public void joueurFixeNbJetons(String n, int[] nbj) {
        int idx = indexFromName(n);
        joueur[idx].fixeNbJetons(nbj);
        repaint();
    }

    public void joueurFixeNbCartes(String n, int nb) {
        int idx = indexFromName(n);
        joueur[idx].fixeNbCartes(nb);
        repaint();
    }

    public void fixeJeuHumain(int[] lj) {
        humain.fixeJeu(lj);
        repaint();
    }

    public void fixeJetonsHumain(int[] lj) {
        humain.fixeJetons(lj);
        repaint();
    }

    public void modifieJetonsHumain(int[] dj) {
        humain.modifieJetons(dj);
        repaint();
    }

    public void fixeMises(int[] lms) {
        mises.fixeJetons(lms);
        repaint();
    }

    public void fixeAjoutMise(int[] lms) {
        mises.nouvelleMise(lms);
        repaint();
    }

    public void jetteCartes(int i) {
        mises.jetteCartes(i);
        repaint();
    }

    public void fixeFlop(int[] nc) {
        flop.fixeJeu(nc);
        repaint();
    }

    public void ajouteCarteFlop(int nc) {
        flop.addCarte(nc);
        repaint();
    }

    public void ferme() {
        this.dispose();
    }
}

class PasTrouve extends RuntimeException {
}



 