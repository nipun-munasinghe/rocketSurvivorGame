import javax.swing.*;

public class App {

    public static void main(String[] args) throws Exception {
        int boardWidth = 1200;
        int boardHeight = 736;

        JFrame frame = new JFrame("Rocket Survivor");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RocketSurvivor rocketSurvivor = new RocketSurvivor();
        frame.add(rocketSurvivor);
        frame.pack();
        rocketSurvivor.requestFocus();
        frame.setVisible(true);
    }
}