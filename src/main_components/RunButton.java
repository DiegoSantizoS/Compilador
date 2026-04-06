package main_components;

import mainFrame.*;
import javax.swing.*;
import java.awt.*;

public class RunButton extends JButton {

    // ====== TUNE THESE ======
    private static final int SIZE = 32;                 // button + icon size
    private static final Color BLUE = new Color(66, 165, 245); // nice UI blue
    private static final Color BLUE_DARK = new Color(30, 136, 229);

    public RunButton() {
        super();
        setText(null);

        // Blue arrow icon
        setIcon(new RightArrowIcon(SIZE, BLUE, BLUE_DARK));

        // Look/feel
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        setMargin(new Insets(0, 0, 0, 0));

        // IMPORTANT: match icon size so it doesn't clip
        Dimension d = new Dimension(SIZE, SIZE);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setToolTipText("Run");
    }

    static class RightArrowIcon implements Icon {
        private final int size;
        private final Color base;
        private final Color stroke;

        RightArrowIcon(int size, Color base, Color stroke) {
            this.size = size;
            this.base = base;
            this.stroke = stroke;
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

                Color fill = pressed ? brighten(base, 0.18f) : base;

                int pad = Math.max(4, size / 6);

                int x1 = x + pad;
                int y1 = y + pad;

                int x2 = x + size - pad;
                int y2 = y + size / 2;

                int x3 = x + pad;
                int y3 = y + size - pad;

                Polygon tri = new Polygon(
                        new int[]{x1, x2, x3},
                        new int[]{y1, y2, y3},
                        3
                );

                g2.setColor(withAlpha(fill, 235));
                g2.fill(tri);

                g2.setColor(pressed ? brighten(stroke, 0.15f) : stroke);
                g2.setStroke(new BasicStroke(Math.max(1.6f, size / 18f),
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(tri);

            } finally {
                g2.dispose();
            }
        }

        private static Color withAlpha(Color c, int a) {
            return new Color(c.getRed(), c.getGreen(), c.getBlue(), Math.max(0, Math.min(255, a)));
        }

        private static Color brighten(Color c, float amount) {
            // amount: 0.0 -> no change, 1.0 -> white
            amount = Math.max(0f, Math.min(1f, amount));
            int r = (int) (c.getRed()   + (255 - c.getRed())   * amount);
            int g = (int) (c.getGreen() + (255 - c.getGreen()) * amount);
            int b = (int) (c.getBlue()  + (255 - c.getBlue())  * amount);
            return new Color(Math.min(255, r), Math.min(255, g), Math.min(255, b));
        }
    }
}
