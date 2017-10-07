
public class PokerHoldem {

    public static void main(String[] args) {
        String[] noms = {"Arnaud", "Elodie", "Stephanie", "Lionel", "Oscar", "Sophie"};
        String donneur, gagnant, humain, spare = "Robert";
        int blind, grosseBlind = 20;

        Terminal.ecrireStringln("Bienvenue dans ce jeu de Poker Texas Holdem Limit.");
        do {
            Terminal.ecrireString("Veuillez entrer votre nom :");
            humain = Terminal.lireString();
            humain = verifNom(humain);
        }while (humain.length()==0);
        char [] c = humain.toCharArray();
        c[0]=Character.toUpperCase(c[0]);
        humain = new String(c);
        for (int i = 0; i < noms.length; i++) {
            if (noms[i].equalsIgnoreCase(humain)) {
                noms[i] = spare;
            }
        }
        ListeJoueurs liste = new ListeJoueurs(noms, humain, 500);
        donneur = tirageSortDonneur(liste);
        NewIP p = new NewIP(3, 2, true, 0);
        initialisationIP(p, liste);
        p.afficheFenetre();
        do {
            Croupier charly = new Croupier();
            liste.setPremier(humain);
            blind = grosseBlind;
            charly.melanger();
            charly.payerBlind(liste, donneur, blind);
            charly.donner(liste);
            pause(500);
            Terminal.ecrireStringln("+++++ Croupier donne cartes.");
            reinitialisation(p, liste);
            p.setDonneur(donneur);
            blind = grosseBlind;
            gagnant = miser(p, liste, charly, donneur, blind);
            if (gagnant.equals("")) {
                Terminal.ecrireStringln("+++++ Croupier donne flop.");
                charly.donnerFlop(liste);
                p.fixeFlop(charly.getCartesSurTable());
                pause(500);
                gagnant = miser(p, liste, charly, donneur, blind);
                blind = grosseBlind * 2;
                if (gagnant.equals("")) {
                    charly.donnerTurnRiver(liste);
                    Terminal.ecrireStringln("+++++ Croupier donne Turn. ");
                    p.fixeFlop(charly.getCartesSurTable());
                    pause(500);
                    gagnant = miser(p, liste, charly, donneur, blind);
                    if (gagnant.equals("")) {
                        charly.donnerTurnRiver(liste);
                        Terminal.ecrireStringln("+++++ Croupier donne River. ");
                        p.fixeFlop(charly.getCartesSurTable());
                        pause(500);
                        gagnant = miser(p, liste, charly, donneur, blind);
                    }
                }
            }
            
            if (gagnant.equals("")) {
                afficheCartesNonCouches(p, liste);
                Terminal.ecrireString(charly.payerGagnant(liste));
            } else {
                Terminal.ecrireStringln("+++++ " + gagnant + " gagne " +liste.getJoueur(gagnant).getEsperanceGain()+".");
                charly.payerGagnant(liste);
            }
            donneur = changerDonneur(liste, donneur);
            
            Terminal.ecrireStringln("Appuyez sur entree pour continuer...");
            Terminal.lireString();
            reinitJoueurs(p, liste);
        } while (liste.existe(humain) && liste.getNbJoueurs() > 1);
        p.ferme();
        if (liste.existe(humain)) {
            Terminal.ecrireStringln("Vous avez gagne !!! Felicitations...");
        } else {
            Terminal.ecrireStringln("Vous avez perdu ! Retentez votre chance...");
        }
    }

    private static void reinitJoueurs(NewIP ip, ListeJoueurs l) {
        Player j = l.getPremier();
        do {
            j.reinitialiser();
            if (j.getCave() == 0) {
                Terminal.ecrireStringln("- " + j.getNom() + " quitte la table.");
                l.enleverJoueur(j.getNom());
                ip.enleverJoueur(j.getNom());
            }
            j = j.getSuivant();
        } while (j != null);
    }

    private static void initialisationIP(NewIP ip, ListeJoueurs l) {
        Player j = l.getPremier();
        do {
            if (j.isHuman()) {
                ip.fixeJetonsHumain(j.getCaveIP());
            } else {
                ip.declareJoueur(j.getNom(), j.getCaveIP());
            }
            j = j.getSuivant();
        } while (j != null);
    }

    private static void reinitialisation(NewIP ip, ListeJoueurs l) {
        Player j = l.getPremier();
        ip.reinitialisation();
        do {
            if (j.isHuman()) {
                ip.fixeJetonsHumain(j.getCaveIP());
                ip.fixeJeuHumain(j.getMain().affichage());
            } else {
                ip.joueurFixeNbJetons(j.getNom(), j.getCaveIP());
            }
            ip.setMise(j.getNom(), j.getMise());
            j = j.getSuivant();
        } while (j != null);

    }

    private static String miser(NewIP ip, ListeJoueurs l, Croupier c, String donneur, int blind) {
        l.setPremier(donneur);
        boolean continuerEncheres;
        int compteur = 0;
        setPremierMiseur(l, c);
        Player j = l.getPremier();
        int nbRaise = 0;
        int cartesJetes = 0;
        do {
            continuerEncheres = false;
            do {
                int choix = 0;
                if (!j.horsJeu() && (j.getMise() != c.getMiseMax() || (j.getMise() == c.getMiseMax() && compteur == 0 && l.getNbJoueursEnJeu() > 1))) {
                    if (j.isHuman()) {
                        ip.setActive(j.getNom());
                        choix = choixHumain(c, j, blind, nbRaise);
                    } else if (!j.isHuman()) {
                        ip.setActive(j.getNom());
                        choix = j.choix(c, l, blind, nbRaise);
                        pause(500);
                    }
                }
                switch (choix) {
                    case 1: //check
                        if (j.getMise() != c.getMiseMax()) {
                            j.check(c.getMiseMax());
                            ip.setMise(j.getNom(), j.getMise());
                            ip.joueurFixeNbJetons(j.getNom(), j.getCaveIP());
                        }
                        Terminal.ecrireStringln("- " + j.getNom() + " suis.");
                        break;
                    case 2: //raise
                        j.raise(c.getMiseMax(), blind);
                        c.setMiseMax(j.getMise());
                        ip.setMise(j.getNom(), j.getMise());
                        ip.joueurFixeNbJetons(j.getNom(), j.getCaveIP());
                        Terminal.ecrireStringln("- " + j.getNom() + " monte.");
                        nbRaise++;
                        break;
                    case 3: //fold
                        j.setFold();
                        ip.joueurAbandonne(j.getNom());
                        Terminal.ecrireStringln("- " + j.getNom() + " se couche.");
                        cartesJetes += 2;
                        ip.jetteCartes(cartesJetes);
                        break;
                }
                pause(500);
                j = j.getSuivant();
            } while (j != null);
            j = l.getPremier();
            Player j2 = l.getPremier();
            do {
                if (!j2.horsJeu() && j2.getMise() != c.getMiseMax()) {
                    continuerEncheres = true;
                }
                j2 = j2.getSuivant();
            } while (j2 != null);
            compteur++;
        } while (continuerEncheres);
        Player x = l.getPremier();
        do {
            int m = x.getEsperanceGain();
            Player y = l.getPremier();
            do {
                if (y.getMise() <= x.getMise()) {
                    m = m + y.getMise();
                }
                if (y.getMise() > x.getMise()) {
                    m = m + x.getMise();
                }
                y = y.getSuivant();
            } while (y != null);
            x.setEsperanceGain(m);
            x = x.getSuivant();
        } while (x != null);
        ip.fixeMises(c.ramasserMises(l));
        j = l.getPremier();
        cartesJetes = 0;
        ip.jetteCartes(cartesJetes);
        int count = 0;
        String name = "";
        do {
            if (!j.isFold()) {
                count++;
                name = j.getNom();
            }
            j = j.getSuivant();
        } while (j != null);
        if (count == 1) {
            return name;
        } else {
            return "";
        }
    }

    private static String changerDonneur(ListeJoueurs l, String nom) {
        Player donneur = l.getJoueur(nom);
        do {
            donneur = donneur.getSuivant();
            if (donneur == null) {
                donneur = l.getPremier();
            }
        } while (donneur.getCave() == 0);

        return donneur.getNom();
    }

    private static String tirageSortDonneur(ListeJoueurs l) {
        Player j = l.getPremier();
        int a = (int) (Math.random() * l.getNbJoueurs());
        for (int i = 0; i < a; i++) {
            j = j.getSuivant();
        }
        return j.getNom();
    }

    private static void afficheCartesNonCouches(NewIP ip, ListeJoueurs l) {
        Player j = l.getPremier();
        do {
            if (!j.isFold() && !j.isHuman()) {
                ip.joueurAbat(j.getNom(), j.getMain().affichage());
            }
            j = j.getSuivant();
        } while (j != null);
    }

    private static void setPremierMiseur(ListeJoueurs l, Croupier c) {
        Player j = l.getPremier();
        if (c.getTourMise() == 0) {
            for (int i = 0; i < 3; i++) {
                j = j.getSuivant();
                if (j == null) {
                    j = l.getPremier();
                }
            }
        } else {
            j = j.getSuivant();
        }
        l.setPremier(j.getNom());
    }

    private static int choixHumain(Croupier c, Player j, int blind, int nbRaise) {
        int choix;
        int x = 2;
        boolean continuer = false;
        String i = "";
        if ((j.getCave() + j.getMise()) - c.getMiseMax() >= 0) {
            i = i + "check(1) ";
        } else if ((j.getCave() + j.getMise()) - c.getMiseMax() < 0) {
            i = i + "all-in(1)";
        }
        if ((j.getCave() + j.getMise()) - c.getMiseMax() >= blind && nbRaise < 4) {
            i = i + "raise(2)";
            x++;
        } else if ((j.getCave() + j.getMise()) - c.getMiseMax() < blind && (j.getCave() + j.getMise()) - c.getMiseMax() > 0 && nbRaise < 4) {
            i = i + "all-in(2)";
            x++;
        }
        i = i + " fold(3)";
        Terminal.ecrireString(i+"...Choix :");
        do {
            continuer = false;
            try {
                choix = Terminal.lireInt();
            } catch (Exception e) {
                choix = -1;
            }
            if (x == 3) {
                continuer = (choix < 1 || choix > 3);
            }
            if (x == 2) {
                continuer = (choix != 1 && choix != 3);
            }
            if (continuer) {
                Terminal.ecrireString("Verifiez votre choix :");
            }
        } while (continuer);
        return choix;
    }

    private static void pause(int t) {
        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void afficheJoueurs(NewIP ip, ListeJoueurs l) {
        Player j = l.getPremier();
        do {
            if (!j.isHuman()) {
                ip.joueurAbat(j.getNom(), j.getMain().affichage());
            }
            j = j.getSuivant();
        } while (j != null);
    }
    
    private static String verifNom(String nom){
        char c[]=nom.toCharArray();
        boolean correct = true;
        if (c.length==0)return new String();
        for (int i=0;i<c.length;i++){
            if (!Character.isLetter(c[i]))correct =false;
        }
        if (correct){
            c[0]=Character.toUpperCase(c[0]);
            for (int i=1;i<c.length;i++){
                c[i]=Character.toLowerCase(c[i]);
            }
            return new String(c);
        }
        else return new String();
    }
                
}






