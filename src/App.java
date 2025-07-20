import javax.swing.SwingUtilities;
import ui.ScopaGui;

public class App {

    public static void main(String[] args) {
        System.out.println("Avvio della GUI...");
        SwingUtilities.invokeLater(ScopaGui::new);
    }

}