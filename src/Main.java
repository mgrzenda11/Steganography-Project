import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Editor canvas = new Editor("/home/greg/Pictures/05AQNau.jpg"); // insert path to image

        Scanner sc = new Scanner(System.in);
        String message = sc.nextLine();

        int key = 41; // Argeed upon beforehand
        canvas.Inject(key,message);
        System.out.println(canvas.Exrtact(key));

        canvas.display(false);
        canvas.display(true);
        //int lol = canvas.fade('f', 0x25B3AE);
        //System.out.println(Integer.toHexString(lol));
        //System.out.println(canvas.fire(lol));
        //canvas.save();
    }
}
