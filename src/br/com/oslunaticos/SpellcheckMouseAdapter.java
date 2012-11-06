package br.com.oslunaticos;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Eduardo Folly
 */
public class SpellcheckMouseAdapter extends MouseAdapter {

    private Spellcheck spellcheck;

    public SpellcheckMouseAdapter(Spellcheck spellcheck) {
        this.spellcheck = spellcheck;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        positionCaret(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        positionCaret(e);
    }

    private void positionCaret(MouseEvent e) {
        if (e.isPopupTrigger()) {

            JPopupMenu popup = new JPopupMenu();
            final JTextComponent text = spellcheck.getTextComponent();

            Point p = new Point(e.getX(), e.getY());
            int i = text.getUI().viewToModel(text, p);
            text.getCaret().setDot(i);

            try {
                int count = i - 1;
                String c;

                try {
                    do {
                        count--;
                        c = text.getText(count, 1);
                    } while (!c.trim().isEmpty());
                } catch (Exception ex) {
                    count = -1;
                }

                final int ini = count + 1;

                count = i;
                try {
                    do {
                        count++;
                        c = text.getText(count, 1);
                    } while (!c.trim().isEmpty());
                } catch (Exception ex) {
                    return;
                }

                int fim = count;

                final String wordIni = text.getText(ini, fim - ini);
                final String word = Palavra.clearWord(wordIni).trim();

                if (spellcheck.getDictionary().misspelled(word)) {
                    popup.removeAll();

                    JMenuItem menuItem = new JMenuItem("Sugestões:");
                    menuItem.setEnabled(false);
                    popup.add(menuItem);

                    Iterator<String> itr =
                            spellcheck.getDictionary().suggest(word).iterator();

                    while (itr.hasNext()) {
                        String sugest = itr.next();
                        menuItem = new JMenuItem(sugest);
                        menuItem.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                                char pri = word.charAt(0);
                                int count = 0;
                                while (pri != wordIni.charAt(count)) {
                                    count++;
                                }

                                JMenuItem menuItem = (JMenuItem) e.getSource();

                                substitui(text, ini + count, word, menuItem.getText());
                            }
                        });
                        popup.add(menuItem);
                    }

                    if (popup.getComponentCount() < 2) {
                        popup.removeAll();
                        menuItem = new JMenuItem("Sem sugestões.");
                        menuItem.setEnabled(false);
                        popup.add(menuItem);
                    }

                    popup.show(e.getComponent(), e.getX(), e.getY());
                }

            } catch (Exception ex) {
            }
        }
    }

    private void substitui(JTextComponent text, int ini, String de, String para) {
        try {
            text.getDocument().remove(ini, de.length());
            text.getDocument().insertString(ini, para, null);
        } catch (Exception e) {
        }
    }
}
