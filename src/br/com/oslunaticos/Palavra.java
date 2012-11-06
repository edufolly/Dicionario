package br.com.oslunaticos;

/**
 *
 * @author Eduardo Folly
 */
public class Palavra {

    int ini;
    int fim;
    String palavra;

    public Palavra(int ini, int fim, String palavra) {
        this.ini = ini;
        this.fim = fim;
        this.palavra = palavra;
    }

    public int getFim() {
        return fim;
    }

    public void setFim(int fim) {
        this.fim = fim;
    }

    public int getIni() {
        return ini;
    }

    public void setIni(int ini) {
        this.ini = ini;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    @Override
    public String toString() {
        return "Palavra{" + "ini=" + ini + ", fim=" + fim + ", palavra=" + palavra + '}';
    }

    public static String clearWord(String text) {
        String troca = "!@#$%&*()_+=,.;:/?[]{}<>\\–";
        for (int i = 0; i < troca.length(); i++) {
            char c = troca.charAt(i);
            text = text.replace(c, ' ');
        }

        text = text.replaceAll(" - ", "   ");

        return text;
    }
}
