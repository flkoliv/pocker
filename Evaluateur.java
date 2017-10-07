
/**
 * Evaluateur du niveau d'une main
 * 
 * @author FLECK Olivier 
 * @version (1.00)
 */
public class Evaluateur {

    private static int suite(Main m) {
        int main[] = m.getCartes();
        int compteur = 1;
        int q = 0;
        main = classer(main);
        int highCard = main[main.length - 1];
        for (int i = main.length - 1; i > 0; i--) {
            if ((main[i] % 13) == (main[i - 1] % 13) + 1) {
                compteur++;
            } else if ((main[i] % 13) == (main[i - 1] % 13)) {
                q++;
            } else {
                compteur = 1;
                highCard = main[i - 1];
                q=0;
            }
            if (compteur == 5) {
                highCard = main[i + 3 + q];
                return 4000000 + (highCard % 13);
            }
        }
        for (int i = 0; i < main.length; i++) {//classe le jeu avec AS comme plus petite carte
            for (int j = i + 1; j < main.length; j++) {
                if ((main[j] % 13) < (main[i] % 13) || main[j] % 13 == 12) {
                    int x = main[j];
                    main[j] = main[i];
                    main[i] = x;
                }
            }
        }
        q = 0;
        compteur = 1;
        highCard = main[main.length - 1];
        for (int i = main.length - 1; i > 0; i--) {
            int x = (main[i] % 13);
            int y = (main[i - 1] % 13);
            if (x == 12) {
                x = -1;
            }
            if (y == 12) {
                y = -1;
            }
            if (x == y + 1) {
                compteur++;
            } else if ((main[i] % 13) == (main[i - 1] % 13)) {
                q++;
            } else {
                compteur = 1;
                highCard = main[i - 1];
            }
            if (compteur == 5) {
                highCard = main[i + 3 + q];
                return 4000000 + (highCard % 13);//quinte avec as comme plus petite carte
            }
        }
        return 0;
    }

    private static String afficheSuite(Main m) {
        String chaine = new String();
        int main[] = m.getCartes();
        int compteur = 1;
        main = classer(main);

        for (int i = main.length - 1; i > 0; i--) {
            if ((main[i] % 13) == (main[i - 1] % 13) + 1) {
                compteur++;
                chaine = chaine + Carte.getNom(main[i]) + ' ';
            } else if ((main[i] % 13) == (main[i - 1] % 13)) {
            } else {
                compteur = 1;
                chaine = "";
            }
            if (compteur == 5) {
                chaine = chaine + Carte.getNom(main[i - 1]);
                return chaine;   //quinte
            }
        }
        for (int i = 0; i < main.length; i++) {//classe le jeu avec AS comme plus petite carte
            for (int j = i + 1; j < main.length; j++) {
                if ((main[j] % 13) < (main[i] % 13) || main[j] % 13 == 12) {
                    int x = main[j];
                    main[j] = main[i];
                    main[i] = x;
                }
            }
        }
        compteur = 1;
        chaine = "";
        for (int i = main.length - 1; i > 0; i--) {
            int x = (main[i] % 13);
            int y = (main[i - 1] % 13);
            if (x == 12) {
                x = -1;
            }
            if (y == 12) {
                y = -1;
            }
            if (x == y + 1) {
                compteur++;
                chaine = chaine + Carte.getNom(main[i]) + ' ';
            } else {
                compteur = 1;
                chaine = "";
            }
            if (compteur == 5) {
                return chaine + ' ' + Carte.getNom(main[i - 1]);//quinte avec as comme plus petite carte
            }
        }
        return "";
    }

    private static String afficheQuinteFlush(Main m) {

        int main[] = m.getCartes();
        int compteur = 1;
        String chaine = new String();
        for (int i = 0; i < main.length; i++) {
            for (int j = i; j < main.length; j++) {
                if (main[i] > main[j]) {
                    int temp = main[i];
                    main[i] = main[j];
                    main[j] = temp;
                }
            }
        }
        for (int i = main.length - 1; i > 0; i--) {
            if (main[i] == main[i - 1] + 1 && ((int) (main[i] / 13) == (int) (main[i - 1] / 13))) {
                compteur++;
                chaine = chaine + Carte.getNom(main[i]) + ' ';
            } else {
                compteur = 1;
                chaine = "";
            }
            if (compteur == 5) {
                chaine = chaine + Carte.getNom(main[i - 1]);
                return chaine;
            }
            if (compteur == 4 && main[i - 1] % 13 == 0) {
                for (int j = i + 3; j < main.length; j++) {
                    if (main[j] % 13 == 12 && (int) main[j] / 13 == (int) main[i] / 13) {
                        chaine = chaine + Carte.getNom(main[i - 1]) + ' ' + Carte.getNom(main[j]);
                        return chaine;
                    }
                }
            }
        }
        return "";
    }

    private static int quinteFlush(Main m) {
        int main[] = m.getCartes();
        int compteur = 1;
        for (int i = 0; i < main.length; i++) {
            for (int j = i; j < main.length; j++) {
                if (main[i] > main[j]) {
                    int temp = main[i];
                    main[i] = main[j];
                    main[j] = temp;
                }
            }
        }
        for (int i = main.length - 1; i > 0; i--) {
            if (main[i] == main[i - 1] + 1 && ((int) (main[i] / 13) == (int) (main[i - 1] / 13))) {
                compteur++;
            } else {
                compteur = 1;
            }
            if (compteur == 5) {
                if (main[i + 3] % 13 == 12) {
                    return 9000000;
                }
                return 8000000 + (main[i + 3] % 13);
            }
            if (compteur == 4 && main[i - 1] % 13 == 0) {
                for (int j = i + 3; j < main.length; j++) {
                    if (main[j] % 13 == 12 && (int) main[j] / 13 == (int) main[i] / 13) {
                        return 8000000 + (main[i + 2] % 13);
                    }
                }
            }
        }
        return 0;
    }

    private static int couleur(Main m) {
        int main[] = m.getCartes();
        int compteur;
        int valeur;
        main = classer(main);
        for (int i = main.length - 1; i >= 0; i--) {
            compteur = 0;
            valeur = 0;
            for (int j = main.length - 1; j >= 0; j--) {
                if ((int) main[i] / 13 == (int) main[j] / 13) {
                    compteur++;
                    valeur = (valeur*13)+(main[j] % 13);
                }
                if (compteur == 5) {
                    return 5000000 + valeur;
                }
            }
        }
        return 0;
    }

    private static String afficheCouleur(Main m) {
        int main[] = m.getCartes();
        int compteur;
        String chaine = new String();
        main = classer(main);
        for (int i = main.length - 1; i >= 0; i--) {
            compteur = 0;
            chaine = "";
            for (int j = main.length - 1; j >= 0; j--) {
                if ((int) main[i] / 13 == (int) main[j] / 13) {
                    compteur++;
                    chaine = chaine + Carte.getNom(main[j]) + ' ';
                }
                if (compteur == 5) {
                    return chaine;
                }
            }
        }
        return "";
    }

    private static int carre(Main m) {
        int main[] = m.getCartes();
        int compteur;
        for (int i = 0; i < main.length; i++) {
            compteur = 0;
            for (int j = 0; j < main.length; j++) {
                if (main[i] % 13 == main[j] % 13) {
                    compteur++;
                }
                if (compteur == 4) {
                    int high = 0;
                    for (int a = 0; a < main.length; a++) {
                        if (main[a] % 13 >= high % 13 && main[a] % 13 != main[i] % 13) {
                            high = main[a];
                        }
                    }
                    return 7000000 + (main[i] % 13) * 13 + high % 13;
                }
            }
        }
        return 0;
    }

    private static String afficheCarre(Main m) {//ok
        int main[] = m.getCartes();
        int compteur;
        String chaine = new String();
        for (int i = 0; i < main.length; i++) {
            compteur = 0;
            chaine = "";
            for (int j = 0; j < main.length; j++) {
                if (main[i] % 13 == main[j] % 13) {
                    compteur++;
                    chaine = chaine + Carte.getNom(main[j]) + ' ';
                }
                if (compteur == 4) {
                    int high = 0;
                    for (int a = 0; a < main.length; a++) {
                        if (main[a] % 13 >= high % 13 && main[a] % 13 != main[i] % 13) {
                            high = main[a];
                        }
                    }
                    chaine = chaine + Carte.getNom(high);
                    return chaine;
                }
            }
        }
        return "";
    }

    private static int brelan(Main m) {
        int main[] = m.getCartes();
        int compteur;
        main = classer(main);
        for (int i = main.length - 1; i > 0; i--) {
            compteur = 0;
            for (int j = 0; j < main.length; j++) {
                if (main[i] % 13 == main[j] % 13) {
                    compteur++;
                }
                if (compteur == 3) {
                    int c1 = -1, c2 = -1;
                    for (int a = 0; a < main.length; a++) {
                        if (main[a] % 13 > c1 % 13 && main[a] % 13 != main[i] % 13) {
                            c1 = main[a];
                        }
                    }
                    for (int a = 0; a < main.length; a++) {
                        if (main[a] % 13 > c2 % 13 && main[a] % 13 != c1 % 13 && main[a] % 13 != main[i] % 13) {
                            c2 = main[a];
                        }
                    }
                    return 3000000 + (((main[i] % 13) * 13 + (c1 % 13)) * 13 + (c2 % 13));
                }
            }
        }
        return 0;
    }

    private static String afficheBrelan(Main m) {//ok
        int main[] = m.getCartes();
        int compteur;
        String chaine = new String();
        main = classer(main);
        for (int i = main.length - 1; i > 0; i--) {
            compteur = 0;
            chaine = "";
            for (int j = 0; j < main.length; j++) {
                if (main[i] % 13 == main[j] % 13) {
                    compteur++;
                    chaine = chaine + Carte.getNom(main[j]) + ' ';
                }
                if (compteur == 3) {
                    int c1 = -1, c2 = -1;
                    for (int a = 0; a < main.length; a++) {
                        if (main[a] % 13 > c1 % 13 && main[a] % 13 != main[i] % 13) {
                            c1 = main[a];
                        }
                    }
                    for (int a = 0; a < main.length; a++) {
                        if (main[a] % 13 > c2 % 13 && main[a] % 13 != c1 % 13 && main[a] % 13 != main[i] % 13) {
                            c2 = main[a];
                        }
                    }
                    chaine = chaine + Carte.getNom(c1) + ' ' + Carte.getNom(c2);
                    return chaine;
                }
            }
        }
        return "";
    }

    private static int full(Main m) {
        int main[] = m.getCartes();
        int compteur;
        int highcard = -1;
        int x=0;
        classer(main);
        for (int i = 0; i < main.length; i++) {
            compteur = 0;
            for (int j = 0; j < main.length; j++) {
                if (main[i] % 13 == main[j] % 13) {
                    compteur++;
                    if (compteur == 3) {
                        highcard = main[j];
                    }
                }
            }
        }
        if (highcard != -1) {
            for (int i = main.length - 1; i >= 0; i--) {
                compteur = 0;
                for (int j = 0; j < main.length; j++) {
                    if (main[i] % 13 == main[j] % 13 && main[i] % 13 != highcard % 13) {
                        compteur++;
                    }
                    if (compteur == 2) {
                        return 6000000 + ((highcard % 13) * 13) + main[i] % 13;
                        
                    }
                }
            }
        }
        return 0;
    }

    private static String afficheFull(Main m) {
        int main[] = m.getCartes();
        main = classer(main);
        String chaine = new String();
        String chaine1 = new String();
        String chaine2 = new String();
        int compteur;
        int highcard = -1;
        for (int i = 0; i < main.length; i++) {
            compteur = 0;
            chaine = "";
            for (int j = 0; j < main.length; j++) {
                if (main[i] % 13 == main[j] % 13) {
                    compteur++;
                    chaine = chaine + Carte.getNom(main[j]) + ' ';
                    if (compteur == 3) {
                        highcard = main[j] % 13;
                        chaine1 = chaine;
                    }
                }
            }
        }
        if (highcard != -1) {
            for (int i = 0; i < main.length; i++) {
                compteur = 0;
                chaine = "";
                for (int j = 0; j < main.length; j++) {
                    if (main[i] % 13 == main[j] % 13 && main[i] % 13 != highcard) {
                        compteur++;
                        chaine = chaine + Carte.getNom(main[j]) + ' ';
                    }
                    if (compteur == 2) {
                        chaine2 = chaine;
                    }
                }
            }
        }
        return chaine1 + chaine2;
    }

    private static int paire(Main m) {
        int main[] = m.getCartes();
        int compteur1, compteur2 = 0, c1 = -1;
        main = classer(main);
        for (int i = main.length - 1; i >= 0; i--) {
            compteur1 = 0;
            for (int j = 0; j < main.length; j++) {
                if (main[i] % 13 == main[j] % 13) {
                    compteur1++;
                }
                if (compteur1 == 2 && compteur2 == 0) {
                    c1 = main[i] % 13;
                    compteur2++;
                }
            }
        }
        int result = c1 % 13;
        int compteur = 0;
        for (int i = main.length - 1; i >= 0; i--) {
            if (main[i] % 13 != c1 && c1 != -1 && compteur < 3) {
                result = (result * 13) + (main[i] % 13);
                compteur++;
            }
        }
        if (c1 != -1) {
            return result + 1000000;
        } else {
            return 0;
        }
    }

    private static String affichePaire(Main m) {
        String chaine = new String();
        int main[] = m.getCartes();
        int compteur;
        main = classer(main);
        for (int i = main.length - 1; i >= 0; i--) {
            compteur = 0;
            chaine = "";
            for (int j = 0; j < main.length; j++) {
                if (main[i] % 13 == main[j] % 13) {
                    compteur++;
                    chaine = chaine + Carte.getNom(main[j]) + ' ';
                }
                if (compteur == 2) {
                    int c1 = -1, c2 = -1, c3 = -1;
                    for (int a = 0; a < main.length; a++) {
                        if (main[a] % 13 > c1 % 13 && main[a] % 13 != main[j] % 13) {
                            c1 = main[a];
                        }
                    }
                    for (int a = 0; a < main.length; a++) {
                        if (main[a] % 13 > c2 % 13 && main[a] % 13 != c1 % 13 && main[a] % 13 != main[j] % 13) {
                            c2 = main[a];
                        }
                    }
                    for (int a = 0; a < main.length; a++) {
                        if (main[a] % 13 > c3 % 13 && main[a] % 13 != c1 % 13 && main[a] % 13 != c2 % 13 && main[a] % 13 != main[j] % 13) {
                            c3 = main[a];
                        }
                    }
                    chaine = chaine + Carte.getNom(c1) + ' ' + Carte.getNom(c2) + ' ' + Carte.getNom(c3);
                    return chaine;
                }
            }
        }

        return "";
    }

    private static int doublePaire(Main m) {
        int main[] = m.getCartes();
        int compteur = 0, c1 = -1, c2 = -1, c3 = -1, y = 0;
        main = classer(main);
        for (int i = main.length - 1; i >= 0; i--) {
            compteur = 0;
            for (int j = 0; j < main.length; j++) {
                if (main[i] % 13 == main[j] % 13) {
                    compteur++;
                }
                if (compteur == 2 && main[i] % 13 != c1) {
                    if (y == 0) {
                        c1 = main[i] % 13;
                    }
                    if (y == 1) {
                        c2 = main[i] % 13;
                    }
                    y++;
                }
            }
        }
        for (int i = 0; i < main.length; i++) {
            if (main[i] % 13 != c1 && main[i] % 13 != c2) {
                c3 = main[i] % 13;
            }
        }
        if (c1 != -1 && c2 != -1) {
            return ((c1 * 13) + c2) * 13 + c3 + 2000000;
        } else {
            return 0;
        }
    }

    private static String afficheDoublePaire(Main m) {
        String chaine = new String();
        String chaine1 = new String();
        String chaine2 = new String();
        int main[] = m.getCartes();
        int compteur = 0, c1 = -1, c2 = -1, c3 = -1, y = 0;
        main = classer(main);
        for (int i = main.length - 1; i >= 0; i--) {
            compteur = 0;
            chaine = "";
            for (int j = 0; j < main.length; j++) {
                if (main[i] % 13 == main[j] % 13) {
                    compteur++;
                    chaine = chaine + Carte.getNom(main[j]) + ' ';
                }
                if (compteur == 2 && main[i] % 13 != c1) {
                    if (y == 0) {
                        chaine1 = chaine;
                        c1 = main[i] % 13;
                    }
                    if (y == 1) {
                        chaine2 = chaine;
                        c2 = main[i] % 13;
                    }
                    y++;
                }
            }
        }
        for (int i = 0; i < main.length; i++) {
            if (main[i] % 13 != c1 && main[i] % 13 != c2) {
                c3 = main[i];
            }
        }
        if (c1 != -1 && c2 != -1) {
            return chaine1 + chaine2 + Carte.getNom(c3);
        } else {
            return "";
        }
    }

    private static int highCard(Main m) {
        int main[] = m.getCartes();
        int result = 0;
        main = classer(main);
        for (int i = main.length - 1; i > main.length - 6 && i >= 0; i--) {
            result = (result * 13) + (main[i] % 13);
        }
        return result;
    }

    private static String afficheHighCard(Main m) {
        String chaine = new String();
        int main[] = m.getCartes();
        int result = 0;
        main = classer(main);
        for (int i = main.length - 1; i > main.length - 6 && i >= 0; i--) {
            chaine = chaine + Carte.getNom(main[i]) + ' ';
        }
        return chaine;
    }

    private static int[] classer(int[] main) {
        for (int i = 0; i < main.length; i++) {
            for (int j = i + 1; j < main.length; j++) {
                if ((main[j] % 13) < (main[i] % 13)) {
                    int x = main[j];
                    main[j] = main[i];
                    main[i] = x;
                }
            }
        }
        return main;
    }

    public static int rang(Main m) {
        int result = 0, temp = 0;
        result = highCard(m);
        temp = paire(m);
        if (temp > result) {
            result = temp;
        }
        temp = doublePaire(m);
        if (temp > result) {
            result = temp;
        }
        temp = brelan(m);
        if (temp > result) {
            result = temp;
        }
        temp = couleur(m);
        if (temp > result) {
            result = temp;
        }
        temp = full(m);
        if (temp > result) {
            result = temp;
        }
        temp = carre(m);
        if (temp > result) {
            result = temp;
        }
        temp = suite(m);
        if (temp > result) {
            result = temp;
        }
        temp = quinteFlush(m);
        if (temp > result) {
            result = temp;
        }
        return result;
    }

    public static int compareMains(Main m1, Main m2) {
        int r1 = rang(m1);
        int r2 = rang(m2);
        if (r1 > r2) {
            return 1;
        }
        if (r1 < r2) {
            return 2;
        }
        return 0;
    }

    public static String nomMain(Main m) {
        int rang = rang(m);
        int type = (int) rang / 1000000;
        int[] x = cartes(rang);
        String chaine = "";
        String au = "au", de = "de", de2 = "de";
        String s="",s2="";
        switch (type) {
            case 0:
                chaine = "Carte Haute " + Carte.getRangNom(x[4]);
                break;
            case 1:
                if (x[3] == 12) {
                    de = "d'";
                }
                if (x[3] > 8 && x[3]!=12) {
                    s=s+'s';
                }
                chaine = "Paire " + de + ' ' + Carte.getRangNom(x[3])+s;
                break;
            case 2:
                if (x[2] == 12) {
                    de = "d'";
                }
                if (x[1] == 12) {
                    de2 = "d'";
                }
                if (x[2] > 8 && x[2]!=12) {
                    s=s+'s';
                }
                if (x[1] > 8 && x[1]!=12) {
                    s2=s2+'s';
                }
                chaine = "Paire " + de + ' ' + Carte.getRangNom(x[2])+ s + " et Paire " + de2 + ' ' + Carte.getRangNom(x[1])+s2;
                break;
            case 3:
                if (x[2] == 12) {
                    de = "d'";
                }
                if (x[2] > 8 && x[2]!=12) {
                    s=s+'s';
                }
                chaine = "Brelan " + de + ' ' + Carte.getRangNom(x[2])+s;
                break;
            case 4:
                if (x[0] == 12) {
                    au = "a l'";
                }
                if (x[0] == 10) {
                    au = "a la";
                }
                chaine = "Suite " + au + ' ' + Carte.getRangNom(x[0]);
                break;
            case 5:
                String q = Carte.getCouleur(afficheCouleur(m).toCharArray()[1]);
                chaine = "Couleur a "+ q;
                break;
            case 6:
                if (x[1] > 8 && x[1]!=12) {
                    s=s+'s';
                }
                if (x[0] > 8 && x[0]!=12) {
                    s2=s2+'s';
                }
                chaine = "Full aux " + Carte.getRangNom(x[1])+ s + " par les " + Carte.getRangNom(x[0])+s2;
                break;
            case 7:
                if (x[1] == 12) {
                    de = "d'";
                }
                if (x[1] > 8 && x[1]!=12) {
                    s=s+'s';
                }
                chaine = "Carre " + de + ' ' + Carte.getRangNom(x[1])+s;
                break;
            case 8:
                if (x[0] == 12) {
                    de = "d'";
                }
                chaine = "Quinte Flush " + au + ' ' + Carte.getRangNom(x[0]);
                break;
            case 9:
                chaine = "Quinte Flush Royale";
                break;
        }
        return chaine;
    }

    public static String meilleurMain(Main m) {
        int rang = rang(m);
        int type = (int) rang / 1000000;
        String chaine = "";
        switch (type) {
            case 0:
                chaine = afficheHighCard(m);
                break;
            case 1:
                chaine = affichePaire(m);
                break;
            case 2:
                chaine = afficheDoublePaire(m);
                break;
            case 3:
                chaine = afficheBrelan(m);
                break;
            case 4:
                chaine = afficheSuite(m);
                break;
            case 5:
                chaine = afficheCouleur(m);
                break;
            case 6:
                chaine = afficheFull(m);
                break;
            case 7:
                chaine = afficheCarre(m);
                break;
            case 8:
                chaine = afficheQuinteFlush(m);
                break;
            case 9:
                chaine = afficheQuinteFlush(m);
                break;
        }
        return chaine;
    }

    private static int[] cartes(int rang) {
        int x = rang % 1000000;
        int[] tab = new int[5];
        int index = 0;
        while (x != 0) {
            tab[index] = x % 13;
            index++;
            x = (int) x / 13;
        }
        return tab;
    }
}
