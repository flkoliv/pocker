
public class Carte {

    private int index;

    public Carte(int i) {
        index = i;
    }

    int getIndex() {
        return index;
    }

    int getCouleurIndex() {
        return (int) index / 13;
    }

    static int getCouleurIndex(int x) {
        return (int) x / 13;
    }

    static char getCouleur(int x) {
        char c = ' ';
        switch (getCouleurIndex(x)) {
            case 0:
                c = 'h';
                break;
            case 1:
                c = 'd';
                break;
            case 2:
                c = 'c';
                break;
            case 3:
                c = 's';
                break;
        }
        return c;
    }
    
    static String getCouleur(char c){
        if (c=='h')return "Coeur";
        else if (c=='s')return "Pique";
        else if (c=='d')return "Carreau";
        else if (c=='c')return "Trefle";
        else return "";
    }
    
    
    
    int getRangIndex() {
        return index % 13;
    }

    static int getRangIndex(int x) {
        return x % 13;
    }

    static char getRang(int x) {
        char c = ' ';
        switch (getRangIndex(x)) {
            case 0:
                c = '2';
                break;
            case 1:
                c = '3';
                break;
            case 2:
                c = '4';
                break;
            case 3:
                c = '5';
                break;
            case 4:
                c = '6';
                break;
            case 5:
                c = '7';
                break;
            case 6:
                c = '8';
                break;
            case 7:
                c = '9';
                break;
            case 8:
                c = 'T';
                break;
            case 9:
                c = 'J';
                break;
            case 10:
                c = 'D';
                break;
            case 11:
                c = 'R';
                break;
            case 12:
                c = 'A';
                break;
        }
        return c;
    }

    static String getRangNom(int x) {
        String c = "";
        switch (getRangIndex(x)) {
            case 0:
                c = "2";
                break;
            case 1:
                c = "3";
                break;
            case 2:
                c = "4";
                break;
            case 3:
                c = "5";
                break;
            case 4:
                c = "6";
                break;
            case 5:
                c = "7";
                break;
            case 6:
                c = "8";
                break;
            case 7:
                c = "9";
                break;
            case 8:
                c = "10";
                break;
            case 9:
                c = "Valet";
                break;
            case 10:
                c = "Dame";
                break;
            case 11:
                c = "Roi";
                break;
            case 12:
                c = "As";
                break;
        }
        return c;
    }

    static String getNom(int x) {
        String s = new String();
        s = s + getRang(x) + getCouleur(x);
        return s;
    }

    int id() {
        int a = 0, b = 0;
        switch (getCouleurIndex()) {
            case 0:
                a = 100;
                break;
            case 1:
                a = 200;
                break;
            case 2:
                a = 300;
                break;
            case 3:
                a = 0;
                break;
        }
        b = getRangIndex() + 1;
        if (b == 13) {
            b = 0;
        }
        a = a + b;
        return a;
    }

    static int id(int x) {
        int a = 0, b = 0;
        switch (getCouleurIndex(x)) {
            case 0:
                a = 100;
                break;
            case 1:
                a = 200;
                break;
            case 2:
                a = 300;
                break;
            case 3:
                a = 0;
                break;
        }
        b = getRangIndex(x) + 1;
        if (b == 13) {
            b = 0;
        }
        a = a + b;
        return a;
    }
}
