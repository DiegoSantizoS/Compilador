package mainFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClosableTabComponent extends JPanel {

    private final JTabbedPane tabbedPane;
    private final JLabel titleLabel = new JLabel();
    
    public ClosableTabComponent(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        
        titleLabel.setText(getCurrentTitle());
        
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel titleLabel = new JLabel() {
            @Override
            public String getText() {
                int index = tabbedPane.indexOfTabComponent(ClosableTabComponent.this);
                return (index != -1) ? tabbedPane.getTitleAt(index) : "";
            }
        };

        CloseXButton closeButton = new CloseXButton(() -> {
            int index = tabbedPane.indexOfTabComponent(ClosableTabComponent.this);
            if (index != -1) tabbedPane.remove(index);
        });

        add(titleLabel);
        add(closeButton);
    }

    static class CloseXButton extends JButton {
        private boolean hover = false;

        private final Color normal = new Color(140, 140, 140);
        private final Color hovered = new Color(255, 60, 60); 
        private final Color pressed = new Color(255, 120, 120);

        CloseXButton(Runnable onClick) {
            setPreferredSize(new Dimension(15, 15));
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(false); 
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setToolTipText("Close");

            addActionListener(e -> onClick.run());

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                ButtonModel m = getModel();
                Color c = normal;
                if (m.isPressed()) c = pressed;
                else if (hover) c = hovered;

                int w = getWidth();
                int h = getHeight();

                int pad = 5; 
                int offsetX = 4; 
                int x1 = pad + offsetX, y1 = pad;
                int x2 = w - pad + offsetX, y2 = h - pad;

                g2.setColor(c);
                g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                g2.drawLine(x1, y1, x2, y2);
                g2.drawLine(x2, y1, x1, y2);

            } finally {
                g2.dispose();
            }
        }
    }
    
    private String getCurrentTitle() {
        int i = tabbedPane.indexOfTabComponent(this);
        return (i != -1) ? tabbedPane.getTitleAt(i) : "";
    }

    public void setTitle(String t) {
        titleLabel.setText(t);
        revalidate();
        repaint();
    }
}
