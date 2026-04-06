package main_components.piccolo;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.piccolo2d.PCanvas;
import org.piccolo2d.PNode;
import org.piccolo2d.event.PBasicInputEventHandler;
import org.piccolo2d.event.PInputEvent;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.nodes.PText;

public class PiccoloTreePanel extends JPanel {

    private final JLabel titulo;
    private final PCanvas canvas;

    private static final double H_GAP = 18;
    private static final double V_GAP = 50;
    private static final double PADDING_X = 8;
    private static final double PADDING_Y = 5;

    public PiccoloTreePanel(String textoTitulo) {
        setLayout(new BorderLayout());

        titulo = new JLabel(textoTitulo, SwingConstants.CENTER);
        titulo.setPreferredSize(new java.awt.Dimension(0, 35));
        canvas = new PCanvas();

        add(titulo, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);

        configurarInteraccion();
    }

    public void setTree(TreeNodeModel root) {
        canvas.getLayer().removeAllChildren();

        if (root == null) {
            revalidate();
            repaint();
            return;
        }

        Map<TreeNodeModel, PNode> visuales = new HashMap<>();
        double[] nextLeafX = {20};

        layoutTree(root, 0, nextLeafX, visuales);
        drawConnections(root, visuales);
        drawNodes(visuales);

        canvas.getCamera().animateViewToCenterBounds(
                canvas.getLayer().getFullBounds(),
                true,
                100
        );

        revalidate();
        repaint();
    }

    private void configurarInteraccion() {
        canvas.addInputEventListener(new PBasicInputEventHandler() {
            @Override
            public void mouseWheelRotated(PInputEvent event) {
                double scale = event.getWheelRotation() < 0 ? 1.1 : 0.9;
                canvas.getCamera().scaleViewAboutPoint(
                        scale,
                        event.getPosition().getX(),
                        event.getPosition().getY()
                );
            }
        });
    }

    private double layoutTree(
            TreeNodeModel node,
            int depth,
            double[] nextLeafX,
            Map<TreeNodeModel, PNode> visuales
    ) {
        PText text = new PText(node.getLabel());
        text.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        double width = text.getWidth() + (PADDING_X * 2);
        double height = text.getHeight() + (PADDING_Y * 2);

        PPath box = PPath.createRectangle(0, 0, width, height);
        text.setOffset(PADDING_X, PADDING_Y);
        box.addChild(text);

        double x;
        double y = 20 + (depth * V_GAP);

        if (node.getChildren().isEmpty()) {
            x = nextLeafX[0];
            nextLeafX[0] += width + H_GAP;
        } else {
            double firstX = -1;
            double lastX = -1;

            for (TreeNodeModel child : node.getChildren()) {
                double childCenterX = layoutTree(child, depth + 1, nextLeafX, visuales);
                if (firstX < 0) {
                    firstX = childCenterX;
                }
                lastX = childCenterX;
            }

            x = ((firstX + lastX) / 2.0) - (width / 2.0);
        }

        box.setOffset(x, y);
        visuales.put(node, box);

        return x + (width / 2.0);
    }

    private void drawConnections(TreeNodeModel node, Map<TreeNodeModel, PNode> visuales) {
        PNode parentNode = visuales.get(node);

        double x1 = parentNode.getFullBoundsReference().getCenterX();
        double y1 = parentNode.getFullBoundsReference().getMaxY();

        for (TreeNodeModel child : node.getChildren()) {
            PNode childNode = visuales.get(child);

            double x2 = childNode.getFullBoundsReference().getCenterX();
            double y2 = childNode.getFullBoundsReference().getMinY();

            PPath line = PPath.createLine((float) x1, (float) y1, (float) x2, (float) y2);
            line.setStroke(new BasicStroke(1.2f));
            canvas.getLayer().addChild(line);

            drawConnections(child, visuales);
        }
    }

    private void drawNodes(Map<TreeNodeModel, PNode> visuales) {
        for (PNode node : visuales.values()) {
            canvas.getLayer().addChild(node);
        }
    }
}