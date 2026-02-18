package mainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class SearchButton extends JButton {

    public SearchButton() {
        super();
        setText(null);

        setIcon(new MagnifierIcon(32, new Color(235, 235, 235))); // size, color

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(20, 20));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText("Search");
    }

    static class MagnifierIcon implements Icon {
        private final int size;
        private final Color color;

        MagnifierIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override public int getIconWidth()  { return size; }
        @Override public int getIconHeight() { return size; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                boolean pressed = (c instanceof AbstractButton b)
                        && b.getModel().isArmed() && b.getModel().isPressed();

                Color draw = pressed ? color.darker() : color;

                float stroke = Math.max(2f, size / 12f);
                g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(draw);

                int pad = Math.max(3, size / 7);

                int lensD = size - (pad * 2) - Math.round(stroke);
                int lensX = x + pad;
                int lensY = y + pad;

                lensD = (int) (lensD * 0.72);
                Ellipse2D lens = new Ellipse2D.Float(lensX, lensY, lensD, lensD);
                g2.draw(lens);

                float cx = lensX + lensD * 0.72f;
                float cy = lensY + lensD * 0.72f;

                float hx1 = cx + lensD * 0.15f;
                float hy1 = cy + lensD * 0.15f;
                float hx2 = x + size - pad;
                float hy2 = y + size - pad;

                g2.draw(new Line2D.Float(hx1, hy1, hx2, hy2));

            } finally {
                g2.dispose();
            }
        }
    }

}
