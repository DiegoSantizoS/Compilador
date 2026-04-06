package main_components;

import javax.swing.*;
import java.awt.*;

public class WindowControlButton extends JButton {

    public enum ButtonType {
        MINIMIZE,
        RESTORE,
        CLOSE
    }

    private static final int SIZE = 50;

    private static final Color FG_NORMAL = new Color(230, 230, 230);
    private static final Color FG_HOVER  = Color.WHITE;

    private static final Color BG_HOVER_NORMAL = new Color(255, 255, 255, 22);
    private static final Color BG_PRESS_NORMAL = new Color(255, 255, 255, 38);

    private static final Color BG_HOVER_CLOSE = new Color(232, 17, 35);
    private static final Color BG_PRESS_CLOSE = new Color(200, 10, 25);

    private final ButtonType type;
    private boolean hover = false;
    private boolean pressed = false;

    public WindowControlButton(ButtonType type) {
        this.type = type;

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
        setMargin(new Insets(0, 0, 0, 0));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        Dimension d = new Dimension(SIZE, SIZE);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        switch (type) {
            case MINIMIZE -> {
                setText("—");
                setToolTipText("Minimize");
                setFont(new Font("Dialog", Font.PLAIN, 16));
            }
            case RESTORE -> {
                setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
                updateRestoreIcon(false);
            }
            case CLOSE -> {
                setText("✕");
                setToolTipText("Close");
                setFont(new Font("Dialog", Font.PLAIN, 16));
            }
        }

        getModel().addChangeListener(e -> {
            ButtonModel model = getModel();
            hover = model.isRollover();
            pressed = model.isArmed() && model.isPressed();
            repaint();
        });

        setForeground(FG_NORMAL);
    }
    
    public void updateRestoreIcon(boolean maximized) {
        if (maximized) {
            setText("❐");
            setToolTipText("Restore");
        } else {
            setText("□");
            setToolTipText("Maximize");
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color bg = null;

            if (type == ButtonType.CLOSE) {
                if (pressed) bg = BG_PRESS_CLOSE;
                else if (hover) bg = BG_HOVER_CLOSE;
            } else {
                if (pressed) bg = BG_PRESS_NORMAL;
                else if (hover) bg = BG_HOVER_NORMAL;
            }

            if (bg != null) {
                g2.setColor(bg);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }

            setForeground((type == ButtonType.CLOSE && (hover || pressed)) ? Color.WHITE
                                                                           : (hover ? FG_HOVER : FG_NORMAL));

            super.paintComponent(g2);

        } finally {
            g2.dispose();
        }
    }
}