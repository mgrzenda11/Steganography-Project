import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class Editor {
    private final int height, width;
    private BufferedImage picture, graphia;

    public Editor(String path) {
        try {
            picture = ImageIO.read(new File(path));
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        height = picture.getHeight();
        width = picture.getWidth();

        ColorModel model = picture.getColorModel();
        WritableRaster raster = picture.copyData(null);
        graphia = new BufferedImage(model, raster, model.isAlphaPremultiplied(), null);
    }

    public void Inject() {
        int rgb, r, g, b;


        // rgb = graphia.getRGB(int x, int y);
        // r = rgb >> 16; g = (rgb-r) >> 8; b = rgb - r - g;
        // graphia.setRGB(int x, int y, 0xRRGGBB);
    }

    public int fade(char c, int lv) {
        int r = lv >> 16;
        int g = (lv>>8) - (r<<8);
        int b = lv - (r<<16) - (g<<8);
        int off, val = c - 32;
        if((off = (r+g+b % 191)) == val) return lv;

        int p = off/3;
        switch(off%3) {
            case 2:
                r++;
                g++;
            case 1:
                r++;
            case 0:
                r = (r+p)%191;
                g = (g+p)%191;
                b = (b+p)%191;
                break;
        }
        return (r<<16 | g<<8 | b);
    }

    public int fade2(char c, int lv) {
        int r = lv >> 16;
        int g = (lv>>8) & 0xff;
        int b = lv & 0xff;
        //System.out.println(r + " " + g + " " + b);

        int val1 = c >> 4;
        int val2 = c - (val1<<4);
        //System.out.println(val1 + " " + val2);
        r = (r & 0xf0) | val1;
        b = (b & 0xf0) | val2;

        return (r<<16 | g<<8 | b);
    }

    public void display(boolean which) {
        BufferedImage pic = which ? graphia : picture;

        // Scaled dimensions of image
        int high = 600;
        int wide = (width * 600) / height;

        Image myPic2 = pic.getScaledInstance(wide, high, Image.SCALE_DEFAULT);

        JLabel picLabel = new JLabel(new ImageIcon(myPic2));
        JPanel jPanel = new JPanel();
        JFrame f = new JFrame();

        jPanel.add(picLabel);
        f.setSize(new Dimension(wide + 20,high + 40));
        f.add(jPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
