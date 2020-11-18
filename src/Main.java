public class Main {

    public static void main(String[] args) {
        Editor canvas = new Editor("/home/greg/Pictures/05AQNau.jpg"); // insert path to image
        canvas.display(true);
        canvas.display(false);
        System.out.println(Integer.toHexString(canvas.fade2('f', 0x25B3AE)));
    }
}
