package br.com.oslunaticos;

import dk.dren.hunspell.Hunspell;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Eduardo Folly
 */
public class SpellcheckDocumentListener implements DocumentListener {

    private int ativa = 2;
    private int cont = 0;
    private Thread t;
    private Spellcheck spellcheck;

    public SpellcheckDocumentListener(Spellcheck spellcheck) {
        this.spellcheck = spellcheck;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        ativa(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        ativa(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        ativa(e);
    }

    private void ativa(DocumentEvent e) {
        cont = 0;
        if (t == null) {
            t = new Thread(new Ativador());
            t.start();
        } else {
            if (t.getState().equals(Thread.State.TERMINATED)) {
                t = new Thread(new Ativador());
                t.start();
            }
        }
    }

    private class Ativador implements Runnable {

        @Override
        public void run() {
            while (cont < ativa) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
                cont++;
            }
            UnderlineHighlighter high = spellcheck.getHighlighter();
            high.removeAllHighlights();

            String texto = "";

            try {
                texto = spellcheck.getText();
            } catch (Exception ex) {
            }

            if (texto.trim().isEmpty()) {
                return;
            }

            Hunspell.Dictionary d = spellcheck.getDictionary();

            texto = Palavra.clearWord(texto);

            SpellcheckSplit ss = new SpellcheckSplit(texto);
            while (ss.hasMoreTokens()) {
                Palavra p = ss.nextToken();

                String word = p.getPalavra();

                if (d.misspelled(word)) {
                    try {
                        high.addHighlight(p.getIni(), p.getFim());
                    } catch (BadLocationException ex) {
                    }
                }
            }
        }
    }
}
