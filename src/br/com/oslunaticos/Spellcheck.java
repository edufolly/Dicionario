package br.com.oslunaticos;

import dk.dren.hunspell.Hunspell.Dictionary;
import java.awt.Color;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Eduardo Folly
 */
public class Spellcheck {

    private final UnderlineHighlighter high;
    private final Dictionary dictionary;
    private final JTextComponent textComponent;

    public Spellcheck(JTextComponent textComponent, Dictionary dictionary) {
        this.textComponent = textComponent;

        // Dicionário
        this.dictionary = dictionary;

        // Marcador
        high = new UnderlineHighlighter(Color.red);
        textComponent.setHighlighter(high);

        // Document Listener
        SpellcheckDocumentListener sdl = new SpellcheckDocumentListener(this);
        textComponent.getDocument().addDocumentListener(sdl);

        // Mouse Adapter
        SpellcheckMouseAdapter sma = new SpellcheckMouseAdapter(this);
        textComponent.addMouseListener(sma);
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public String getText() {
        return textComponent.getText();
    }

    public UnderlineHighlighter getHighlighter() {
        return high;
    }

    public JTextComponent getTextComponent() {
        return textComponent;
    }
}
