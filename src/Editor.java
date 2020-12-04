import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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

    public void createMiniImage() {
        //literally a script to create a 5x5 image of a smile face
        BufferedImage img = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        //blue = 0x25e6e2
        //yellow = 0xfff700
        //brown = 0x634200
        //dark green = 0x006308
        //green = 0x1df52f
        img.setRGB(0, 0, 0x25e6e2);
        img.setRGB(1, 0, 0x25e6e2);
        img.setRGB(2, 0, 0xfff700);
        img.setRGB(3, 0, 0x25e6e2);
        img.setRGB(4, 0, 0x25e6e2);

        img.setRGB(0, 1, 0x25e6e2);
        img.setRGB(1, 1, 0xfff700);
        img.setRGB(2, 1, 0x634200);
        img.setRGB(3, 1, 0xfff700);
        img.setRGB(4, 1, 0x25e6e2);

        img.setRGB(0, 2, 0x25e6e2);
        img.setRGB(1, 2, 0x25e6e2);
        img.setRGB(2, 2, 0xfff700);
        img.setRGB(3, 2, 0x25e6e2);
        img.setRGB(4, 2, 0x25e6e2);

        img.setRGB(0, 3, 0x006308);
        img.setRGB(1, 3, 0x006308);
        img.setRGB(2, 3, 0x1df52f);
        img.setRGB(3, 3, 0x1df52f);
        img.setRGB(4, 3, 0x006308);

        img.setRGB(0, 4, 0x006308);
        img.setRGB(1, 4, 0x006308);
        img.setRGB(2, 4, 0x1df52f);
        img.setRGB(3, 4, 0x006308);
        img.setRGB(4, 4, 0x006308);

        miniDisplay("MiniImage", img);

        //saving image
        File output = new File(System.getProperty("user.home") + "/Pictures/MiniImage.png");
        try {
            ImageIO.write(img, "png", output);
        } catch(IOException ioe) {
            System.out.println(ioe.getMessage());
        }

    }

    public void Inject(int prime, String message) {
        int bound = height * width, x = 1, k, i, j;
        ArrayList<Integer> col = new ArrayList<>();

        for(char c: (message+'\0').toCharArray()) {
            //System.out.println(c);
            do {
                k = (int) (Math.pow(prime * x++, 2)) % bound;
            } while(col.contains(k));
            col.add(k);
            i = k % width;
            j = k / width;
            System.out.print("(" + i + "," + j + ")\t");
            System.out.print(Integer.toHexString(graphia.getRGB(i,j)) + "\t");
            graphia.setRGB(i, j,fade(c, graphia.getRGB(i,j)));
            System.out.println(Integer.toHexString(graphia.getRGB(i,j)) + "\t");
        }

        for(int l = 0; l < width; l++) {
            for (int m = 0; m < height; m++) {
                System.out.print(Integer.toHexString(graphia.getRGB(m,l)) + " ");
            }
            System.out.println("\n");
        }
    }

    public void Inject2(){
        for(int row = 0; row<width; row++) {
            for(int col = 0; col<height; col++) {
                graphia.setRGB(row, col,fade('a', graphia.getRGB(row,col)));
            }
        }
    }

    public String Exrtact(int key) {
        int bound = height * width, x = 1, k, i, j;
        ArrayList<Integer> col = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        char c;

        while(x < bound) {
            do {
                k = (int) (Math.pow(key * x++, 2)) % bound;
            } while(col.contains(k));
            col.add(k);
            i = k % width;
            j = k / width;
            c = fire(graphia.getRGB(i, j));

            if(c != '\0') message.append(c);
            else break;
        }

        return message.toString();
    }

    /*private void printRGBValues() {
        for(int row = 0; row<width; row++) {
            for(int col = 0; col<height)
        }
    } */

    private int fade(char c, int lv) {
        int r = lv >> 16, g = (lv>>8) & 0xff, b = lv & 0xff;
        int t = ThreadLocalRandom.current().nextInt(0,16);

        g = (g & 0xf0) | t;
        r = (r & 0xf0) | (((c >> 4) + t) % 16);
        b = (b & 0xf0) | (((c & 0x0f) + t) % 16);

        return (r<<16 | g<<8 | b);
    }

    private char fire(int lv) {
        int r = (lv>>16) & 0x0f, g = 16 - ((lv>>8) & 0x0f), b = lv & 0x0f;
        return (char) (((r+g)%16) << 4 | ((b+g)%16));
    }

    public void display(boolean which) {
        BufferedImage pic = which ? graphia : picture;
        String name = which ? "graphia" : "picture";

        // Scaled dimensions of image
        int high = 600;
        int wide = (width * 600) / height;

        Image myPic2 = pic.getScaledInstance(wide, high, Image.SCALE_DEFAULT);
        JLabel picLabel = new JLabel(new ImageIcon(myPic2));
        JPanel jPanel = new JPanel();
        JFrame f = new JFrame();

        jPanel.add(picLabel);
        f.setTitle(name);
        f.setSize(new Dimension(wide + 20,high + 40));
        f.add(jPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void miniDisplay(String title, BufferedImage img) {
        //blow up the image
        int height = 500;
        int width = 500;

        Image miniPic = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        JLabel picLabel = new JLabel(new ImageIcon(miniPic));
        JPanel jPanel = new JPanel();
        JFrame f = new JFrame();

        jPanel.add(picLabel);
        f.setTitle(title);
        f.setSize(new Dimension(width + 20,height + 40));
        f.add(jPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void save() {
        // System.out.println(System.getProperty("user.home"));
        File output = new File(System.getProperty("user.home") + "/Pictures/graphia.png");
        try {
            ImageIO.write(toBI(graphia.getScaledInstance(500, 500, Image.SCALE_DEFAULT)), "png", output);
        } catch(IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public BufferedImage toBI(Image img) {
        if (img instanceof BufferedImage)
            return (BufferedImage) img;

        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        return bimage;
    }
}
