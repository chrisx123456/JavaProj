package wit.proj;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
/**
 * Klasa odpowiadająca za UI
 * @author Maciej Lacek, Bartosz Kowalczyk
 * @since 20.05.2024
 * @version 1.0
 */
public class UI extends JFrame {
    /**JTextField przechowyjący ścieżkę do foldery ze zdjęciami*/
    final private JTextField sourcePathField;
    /**JTextField przechowyjący ścieżkę do foldery w którym będzie tworzyć podfoldery i zapisywac zdjęcia*/
    final private JTextField destinationPathField;

    /**
     * Konstuktor klasy, klasa dziediczy po JFrame więc wszystkie kontrolki są dodawane własnie w konstruktorze.
     */
    public UI(){
        setLayout(new GridLayout(3, 1));

        JPanel sourcePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sourcePathField = new JTextField(20);
        JButton sourceBtn = new JButton("...");
        sourceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    sourcePathField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        sourcePanel.add(new JLabel("Source:"));
        sourcePanel.add(sourcePathField);
        sourcePanel.add(sourceBtn);

        JPanel destinationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        destinationPathField = new JTextField(20);
        JButton destinationBtn = new JButton("...");
        destinationBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    destinationPathField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        destinationPanel.add(new JLabel("Destination:"));
        destinationPanel.add(destinationPathField);
        destinationPanel.add(destinationBtn);

        JPanel startPanel = new JPanel();
        JButton startBtn = new JButton("Start");
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Run();
                } catch (Exception ex) {
                    DisplayMessage(ex.getMessage(), JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        startPanel.add(startBtn);

        this.add(sourcePanel);
        this.add(destinationPanel);
        this.add(startPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Ustawia wysokość/szerokość okna tak aby dostosował się do PrefferedSize kontrolek.
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    /**
     * Metoda uruchamiająca działanie programu
     * @throws Exception Jakikolwiek wyjątek wygenerowany w trakcie działania programu, wyjątek obslużony jest w logice przycisku w konstruktorze
     */
    private void Run() throws Exception{
        if(sourcePathField.getText().isEmpty()) throw new Exception("Source path is empty");
        if(destinationPathField.getText().isEmpty()) throw new Exception("Destination path is empty");
        File src = new File(sourcePathField.getText());
        File dst = new File(destinationPathField.getText());
        FileManager.RunMultiThread(src, dst);
    }

    /**
     * Metoda wyświetlająca popup
     * @param msg Wiadomośc do wyświetlenia
     * @param type Typ okienka z wiadomością np Message,Error
     */
    public static void DisplayMessage(String msg, int type){
        JOptionPane.showMessageDialog(null, msg, "", type);
    }

    /**
     * main, uruchamia całe UI
     * @param args args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UI();
            }

        });
    }
}