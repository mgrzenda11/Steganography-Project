public class Main {

    public static void main(String[] args) {
        Editor canvas = new Editor("/home/matt/Pictures/terminal_plant.jpg"); 
        
        canvas.Inject(41, "Pickle");
        System.out.println(canvas.Extract(41));
        canvas.display(true);
        canvas.display(false);
        canvas.save();
    }
}
