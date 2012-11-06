package br.com.oslunaticos;

import java.awt.*;
import javax.swing.text.*;

/**
 *
 * @author Eduardo Folly
 */
public class UnderlineHighlighter extends DefaultHighlighter {

    private Highlighter.HighlightPainter painter;

    public UnderlineHighlighter() {
        this(Color.red);
    }

    public UnderlineHighlighter(Color color) {
        painter = new UnderlineHighlightPainter(color);
    }

    public Object addHighlight(int p0, int p1) throws BadLocationException {
        return super.addHighlight(p0, p1, painter);
    }

    @Override
    public void setDrawsLayeredHighlights(boolean newValue) {
        if (!newValue) {
            throw new IllegalArgumentException(
                    "Só é possível desenhar em camadas de destaque.");
        }
        super.setDrawsLayeredHighlights(newValue);
    }

    public static class UnderlineHighlightPainter extends LayeredHighlighter.LayerPainter {

        protected Color color;

        public UnderlineHighlightPainter(Color color) {
            this.color = color;
        }

        @Override
        public Shape paintLayer(Graphics g, int p0, int p1, Shape viewBounds, JTextComponent editor, View view) {
            g.setColor(color);

            Rectangle alloc;
            if (p0 == view.getStartOffset() && p1 == view.getEndOffset()) {
                if (viewBounds instanceof Rectangle) {
                    alloc = (Rectangle) viewBounds;
                } else {
                    alloc = viewBounds.getBounds();
                }
            } else {
                try {
                    Shape shape = view.modelToView(p0, Position.Bias.Forward, p1, Position.Bias.Backward, viewBounds);
                    if (shape instanceof Rectangle) {
                        alloc = (Rectangle) shape;
                    } else {
                        alloc = shape.getBounds();
                    }
                } catch (BadLocationException e) {
                    return null;
                }
            }

            FontMetrics fm = editor.getFontMetrics(editor.getFont());

            int baseline = alloc.y + alloc.height - fm.getDescent() + 1;

            for (int i = alloc.x; i < alloc.x + alloc.width; i += 2) {
                if (i % 4 == 0) {
                    g.drawLine(i, baseline, i + 1, baseline);
                } else {
                    g.drawLine(i, baseline + 1, i + 1, baseline + 1);
                }
            }
            return alloc;
        }

        @Override
        public void paint(Graphics g, int p0, int p1, Shape bounds, JTextComponent c) {
            // Nao utilizado.
        }
    }
}
