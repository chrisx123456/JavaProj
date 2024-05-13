package wit.proj;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JFrame {
    final private JTextField sourcePathField;
    final private JTextField destinationPathField;

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
                    JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
            }
        });
        startPanel.add(startBtn);

        add(sourcePanel);
        add(destinationPanel);
        add(startPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    private void Run() throws Exception{
        if(sourcePathField.getText().isEmpty()) throw new Exception("Source path is empty");
        if(destinationPathField.getText().isEmpty()) throw new Exception("Destination path is empty");
        //FileManager.Load?(sourcePathField.getText(), destinationPathField.getText())
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new UI();
            }
        });
    }
}