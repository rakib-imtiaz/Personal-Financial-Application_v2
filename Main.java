import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            // Use Nimbus look and feel (modern built-in L&F)
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    
                    // Customize Nimbus colors and properties
                    UIManager.put("control", new javax.swing.plaf.ColorUIResource(245, 245, 247));
                    UIManager.put("nimbusBase", new javax.swing.plaf.ColorUIResource(0, 122, 255));
                    UIManager.put("nimbusBlueGrey", new javax.swing.plaf.ColorUIResource(235, 235, 237));
                    UIManager.put("nimbusFocus", new javax.swing.plaf.ColorUIResource(0, 122, 255));
                    break;
                }
            }
        } catch (Exception e) {
            try {
                // Fallback to system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                // If all else fails, use default look and feel
            }
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.showLogin();
        });
    }
}
