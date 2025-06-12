package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

class ImagenPanel extends JPanel {
    private BufferedImage imagen;

    public ImagenPanel(String ruta) {
        try {
            imagen = ImageIO.read(new File(ruta));
        } catch (Exception e) {
            imagen = null;
        }
        setPreferredSize(new Dimension(235, 60));
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagen != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.drawImage(imagen.getScaledInstance(220, 60, Image.SCALE_SMOOTH), 0, 0, this);
            g2.dispose();
        }
    }
}
