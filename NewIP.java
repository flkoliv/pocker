
import java.awt.*;
import javax.swing.*;

public class NewIP extends InterfacePoker {

    private NewJoueur[] joueur;
    private int nbj = 0;
    private NewHumain humain;
    private Mises mises;
    private Flop flop;
    private SpecialPanel jpane;
    private int xGrille,  yGrille,  largJ,  hauteurJ,  larg,  haut;
    private boolean isTexas;

    NewIP(int x, int y, boolean isTHE, int nbCol) {
        super(x, y, isTHE, nbCol);
        this.setTitle("Table de Poker");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jpane = new SpecialPanel();
        this.setContentPane(jpane);
        // initialisation de variables
        xGrille = x;
        yGrille = y;
        isTexas = isTHE;
        joueur = new NewJoueur[x * y];
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
        humain = new NewHumain((larg - (Cartes.nbCartes * 76) - Jetons.largeur()) / 2, yGrille * hauteurJ + 140);
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
        joueur[nbj] = new NewJoueur(nom, jetons,
                10 + (nbj % xGrille) * (largJ + 20),
                10 + (nbj / xGrille) * hauteurJ);
        nbj++;
        repaint();
    }

    public void enleverJoueur(String nom) {
        boolean existe = false;
        for (int i = 0; i < joueur.length; i++) {
            if (joueur[i].nom.equals(nom)) {
                existe = true;
            }
        }
        if (existe) {
            NewJoueur[] joueur2 = new NewJoueur[nbj - 1];
            for (int i = 0, j = 0; i < joueur.length; i++) {
                if (!joueur[i].nom.equals(nom)) {
                    joueur2[j] = joueur[i];
                    j++;
                }
            }
            joueur = joueur2;
            nbj--;
            repaint();
        }
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
        try {
            int idx = indexFromName(n);
            joueur[idx].abandonne();
        } catch (PasTrouve e) {
            int[] vide = {};
            fixeJeuHumain(vide);
        }
        repaint();
    }

    public void joueurModifieNbJetons(String n, int[] delta) {
        int idx = indexFromName(n);
        joueur[idx].modifieNbJetons(delta);
        repaint();
    }

    public void joueurFixeNbJetons(String n, int[] nbj) {
        try {
            int idx = indexFromName(n);
            joueur[idx].fixeNbJetons(nbj);
        } catch (PasTrouve e) {
            int[] vide = {};
            fixeJetonsHumain(nbj);
        }
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
        for (int i = 0; i < joueur.length; i++) {
            if (joueur[i] != null) {
                joueur[i].mise = 0;
            }
        }
        humain.mise = 0;

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

    public void setActive(String n) {
        for (int i = 0; i < joueur.length; i++) {
            if (joueur[i] != null) {
                joueur[i].desactiver();
            }
        }
        humain.desactiver();
        try {
            int idx = indexFromName(n);
            joueur[idx].activer();
        } catch (PasTrouve e) {
            humain.activer();
        }
        repaint();
    }

    public void setMise(String n, int x) {

        try {
            int idx = indexFromName(n);
            joueur[idx].fixeMise(x);
        } catch (PasTrouve e) {
            humain.fixeMise(x);
        }
        repaint();
    }

    public void setDonneur(String n) {
        for (int i = 0; i < joueur.length; i++) {
            if (joueur[i] != null) {
                joueur[i].donneur = false;
            }
        }
        humain.donneur = false;
        try {
            int idx = indexFromName(n);
            joueur[idx].donneur = true;
        } catch (PasTrouve e) {
            humain.donneur = true;
        }
        repaint();
    }

    public void reinitialisation() {
        for (int i = 0; i < joueur.length; i++) {
            joueur[i].reprend();
        }
        int[] flop = {};
        fixeFlop(flop);
        jetteCartes(0);
        int[] tab = {0};
        fixeMises(tab);
    }
}

class NewJoueur extends Joueur {

    boolean active = false;
    boolean donneur = false;
    int mise = 0;

    public NewJoueur(String n, int[] jts, int dx, int dy) {
        super(n, jts, dx, dy);
    }

    void affiche(JPanel jf, Graphics g) {
        if (cache) {
            Jeux.jeu[nbCartes - 1].paintIcon(jf, g, posX, posY);
        } else if (enjeu) {
            Cartes.afficheCompact(jf, g, cartes, posX, posY);
        }
        if (active) {
            g.setColor(Color.RED);//ajout
        }
        if (Jetons.enChiffres()) {
            String chaine = "";
            if (donneur) {
                chaine = "(D)";
            }
            if (jetons.donneChiffre() > 0) {
                chaine = chaine + nom + " " + jetons.donneChiffre();
            }
            if (jetons.donneChiffre() == 0) {
                chaine = chaine + nom + " All-in";
            }
            g.drawString(chaine, posX + 20, posY + 155);
            if (mise > 0) {
                g.drawString(String.valueOf(mise), posX + 75, posY + 175);
            }
        } else {
            g.drawString(nom, posX + 200, posY + 15);
            jetons.affiche(jf, g);
        }
        g.setColor(Color.BLACK);//ajout
    }

    void activer() {//ajout
        active = true;
    }

    void desactiver() {//ajout
        active = false;

    }

    void fixeMise(int i) {
        mise = i;
    }

    void reprend() {
        cache = true;
        enjeu = true;
    }
}

class NewHumain extends Humain {

    protected int[] cartes = new int[0];
    protected Jetons jetons;
    private int posX,  posY;
    int mise = 0;
    boolean active = false;
    boolean donneur = false;

    NewHumain(int x, int y) {
        super(x, y);
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
        if (active) {
            g.setColor(Color.RED);
        }
        Cartes.affiche(jf, g, cartes, posX, posY);
        String chaine = "";
        if (donneur) {
            chaine = chaine + "(D)";
        }
        if (mise > 0) {
            chaine = chaine + String.valueOf(mise);
        }
        if (jetons.donneChiffre() == 0) {
            g.drawString("All-in", posX + 155, posY + 80);
        }
        g.drawString(chaine, posX + 170, posY + 50);
        jetons.affiche(jf, g);
        g.setColor(Color.BLACK);
    }

    void fixeMise(int i) {
        mise = i;
    }

    void activer() {//ajout
        active = true;
    }

    void desactiver() {//ajout
        active = false;

    }
}












    
