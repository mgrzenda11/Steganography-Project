import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Editor {
    //height and width of the image
    private final int height, width;

    //global variables for the original image (picture) and the image after steganography has been applied (graphia)
    private BufferedImage picture, graphia;


    //----------------------------------------------------------------------------------------------------
    //! Constructor for the Editor Class
    //----------------------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------------------
    //! Injects a message into an image by adjusting the RGB values of certain pixels
    //! Explanation: This algorithm encrypts each character in the message into a pixel via the pixel's RGB values
    //! To determine which pixel is encrypted, a prime is multiplied by an increasing index, the product is squared, 
    //! and then modded by the number of pixels in the image.
    //!
    //! param: prime - the prime number used in encryption
    //! param: message - the message to be encrypted
    //! Returns: none
    //----------------------------------------------------------------------------------------------------
    public void Inject(int prime, String message) {
        //total length of the array of bytes
        int bound = height * width;
        //integer which the prime is multiplied by
        int primeIndex = 1;
        //value of the pixel location before being translated into x and y coordinates
        int primeValue; 
        //x coordinate of pixel
        int xcoord; 
        //y coordinate of pixel
        int ycoord;
        //arraylist to keep track of which coordinates have been encoded aleady
        ArrayList<Integer> col = new ArrayList<>();

        for(char c: (message+'\0').toCharArray()) {
            do {
                primeValue = (int) (Math.pow(prime *primeIndex++, 2)) % bound;
            } while(col.contains(k));
            col.add(k);

            xcoord = primeValue % width;
            ycoord = primeValue / width;

            graphia.setRGB(xcoord, ycoord, encryptPixel(c, graphia.getRGB(xcoord,ycoord)));
        }

        for(int l = 0; l < width; l++) {
            for (int m = 0; m < height; m++) {
                System.out.print(Integer.toHexString(graphia.getRGB(m,l)) + " ");
            }
            System.out.println("\n");
        }
    }

    //----------------------------------------------------------------------------------------------------
    //! Takes an image with a message encrypted into it and extracts the message
    //! Explanation: decrypts the message by using the same prime number and mathematical sequence as the encryption 
    //! algorithm to determine which pixels are encrypted.
    //! 
    //! param: key - the prime used to encode the message
    //! Returns: the encrypted message
    //----------------------------------------------------------------------------------------------------
    public String Extract(int key) {
        int bound = height * width;
        int primeIndex = 1;
        int primeValue;
        int xcoord;
        int ycoord;
        ArrayList<Integer> col = new ArrayList<>();
        StringBuilder message = new StringBuilder();
        char c;

        while(x < bound) {
            do {
                primeValue = (int) (Math.pow(key * primeIndex++, 2)) % bound;
            } while(col.contains(k));
            col.add(k);
            xcoord = primeValue % width;
            ycoord = primeValue / width;
            c = decryptPixel(graphia.getRGB(xcoord, ycoord));

            if(c != '\0') message.append(c);
            else break;
        }

        return message.toString();
    }

    //----------------------------------------------------------------------------------------------------
    //! Encypts a single pixel by applying bitwise OR with a random number to the R, G, and B values. 
    //! The character to be encoded is an 8 bit char. The first four bits of the char are encoded into the 
    //! R value. The second four bits are encoded into the B value. 
    //!
    //! param: c - the character to encode into the RGB value
    //! param: lv - the whole RGB value
    //! Returns: the new RGB value in the form of an int
    //----------------------------------------------------------------------------------------------------
    private int encryptPixel(char c, int lv) {
        //split integer into RGB values
        int r = lv >> 16, g = (lv>>8) & 0xff, b = lv & 0xff;
        int t = ThreadLocalRandom.current().nextInt(0,16);

        //encrypt char into RGB value
        g = (g & 0xf0) | t;
        r = (r & 0xf0) | (((c >> 4) + t) % 16);
        b = (b & 0xf0) | (((c & 0x0f) + t) % 16);

        return (r<<16 | g<<8 | b);
    }

    //----------------------------------------------------------------------------------------------------
    //! Decrypts a single pixel by extracting the char stored in the RGB value.
    //! param: lv - the RGB int value ecnrypted with a character of the message
    //! Returns: the character stored in the RGB value
    //----------------------------------------------------------------------------------------------------
    private char decryptPixel(int lv) {
        int r = (lv>>16) & 0x0f, g = 16 - ((lv>>8) & 0x0f), b = lv & 0x0f;
        return (char) (((r+g)%16) << 4 | ((b+g)%16));
    }

    //----------------------------------------------------------------------------------------------------
    //! Displays an image
    //! param: title - the title of the dialog box
    //! param: img - the Image to be displayed
    //! Returns: a BufferedImage
    //----------------------------------------------------------------------------------------------------
    public void display(boolean whichImage) {
        //determine which image is being displayed
        BufferedImage pic = whichImage ? graphia : picture;
        String name = whichImage ? "graphia" : "picture";

        // Scaled dimensions of image
        int high = 600;
        int wide = (width * 600) / height;

        //set up the GUI
        Image myPic2 = pic.getScaledInstance(wide, high, Image.SCALE_DEFAULT);
        JLabel picLabel = new JLabel(new ImageIcon(myPic2));
        JPanel jPanel = new JPanel();
        JFrame f = new JFrame();

        //add image to GUI and display it
        jPanel.add(picLabel);
        f.setTitle(name);
        f.setSize(new Dimension(wide + 20,high + 40));
        f.add(jPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    //----------------------------------------------------------------------------------------------------
    //! Displays an image less than 25 pixels (used for very small images)
    //! param: title - the title of the dialog box
    //! param: img - the Image to be displayed
    //! Returns: none
    //----------------------------------------------------------------------------------------------------
    public void miniDisplay(String title, BufferedImage img) {
        //blow up the image
        int height = 500;
        int width = 500;

        //set up the GUI
        Image miniPic = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        JLabel picLabel = new JLabel(new ImageIcon(miniPic));
        JPanel jPanel = new JPanel();
        JFrame f = new JFrame();

        //add image to the GUI and display
        jPanel.add(picLabel);
        f.setTitle(title);
        f.setSize(new Dimension(width + 20,height + 40));
        f.add(jPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    //----------------------------------------------------------------------------------------------------
    //! Method to save an image (regardless of whether or not steganography has been applied) to disk
    //! Returns: nothing
    //----------------------------------------------------------------------------------------------------
    public void save() {
        File output = new File(System.getProperty("user.home") + "/Pictures/graphia.png");
        try {
            ImageIO.write(toBI(graphia.getScaledInstance(500, 500, Image.SCALE_DEFAULT)), "png", output);
        } catch(IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    //----------------------------------------------------------------------------------------------------
    //! Convenience method to convert an Image to a BufferedImage
    //! param: img - The Image to be converted to a BufferedImage
    //! Returns: a BufferedImage
    //----------------------------------------------------------------------------------------------------
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
