package mainFrame;
import javax.swing.*;
import java.awt.*;

public class RunButton extends JButton {

    public RunButton() {
        super();
        setText(null);

        setIcon(new RightArrowIcon(32, new Color(0, 200, 0)));

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);

        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(20, 20)); // you can change this
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setToolTipText("Run");
    }

    static class RightArrowIcon implements Icon {
        private final int size;
        private final Color color;

        RightArrowIcon(int size, Color color) {
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

                int pad = Math.max(3, size / 7);

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

                boolean pressed = (c instanceof AbstractButton b)
                        && b.getModel().isArmed() && b.getModel().isPressed();

                Color fill = pressed ? color.brighter(): color;

                g2.setColor(new Color(fill.getRed(), fill.getGreen(), fill.getBlue(), 230));
                g2.fill(tri);

                g2.setColor(fill.darker());
                g2.setStroke(new BasicStroke(Math.max(1.4f, size / 18f),
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.draw(tri);

            } finally {
                g2.dispose();
            }
        }
    }

}
