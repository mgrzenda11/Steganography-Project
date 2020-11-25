public class Main {

    public static void main(String[] args) {
        Editor canvas = new Editor("/home/matthew/Pictures/network.jpg"); // insert path to image
        //canvas.Inject(41,"Hello World");
        canvas.Inject2();
        canvas.save();
        //System.out.println(canvas.Exrtact(41));
        //canvas.display(false);
        //canvas.display(true);
        //int lol = canvas.fade('f', 0x25B3AE);
        //System.out.println(Integer.toHexString(lol));
        //System.out.println(canvas.fire(lol));
        //canvas.save();
    }
}
