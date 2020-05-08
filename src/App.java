import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class App extends JFrame implements ActionListener {
    private JLabel labelQuestion;
    private JLabel labelApp;
    private JLabel labelFile;
    private JTextField fieldApp;
    private JTextField fieldTestFile;
    private JTextField fieldProdFile;
    private JButton testFileButtonOpen;
    private JButton prodFileButtonOpen;
    private JButton buttonEnter;
    private JTextArea log;
    private JFileChooser testFileChooser;
    private JFileChooser prodFileChooser;
    FileWriter csvOutputFile;

    public App() {
        super("Test Smell Detector");
        initComponents();

        setSize(600, 300);
        setLocationRelativeTo(null);
        Image icon = new javax.swing.ImageIcon("images/logo.png").getImage();
        setIconImage(icon);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        labelQuestion = new JLabel("Please select the Test File and relevant Production File you wish to analyse.");
        labelApp = new JLabel("Application Name:");
        labelFile = new JLabel("Test File:");
        fieldApp = new JTextField(20);
        fieldTestFile = new JTextField(20);
        fieldProdFile = new JTextField(20);
        fieldApp.setEditable(true);
        fieldTestFile.setEditable(false);
        fieldProdFile.setEditable(false);
        testFileButtonOpen = new JButton("Open Test File");
        prodFileButtonOpen = new JButton("Open Prod File");
        buttonEnter = new JButton("Enter");
        testFileChooser = new JFileChooser();
        prodFileChooser = new JFileChooser();
        testFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        prodFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        setLayout(new FlowLayout());

        add(labelApp);
        add(fieldApp);
        add(labelFile);
        add(fieldTestFile);
        add(fieldProdFile);
        add(testFileButtonOpen);
        add(prodFileButtonOpen);
        add(buttonEnter);

        testFileButtonOpen.addActionListener(this);
        prodFileButtonOpen.addActionListener(this);
        buttonEnter.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        try {
            csvOutputFile = new FileWriter("file.csv");
            csvOutputFile.append(fieldApp.getText());
            csvOutputFile.append(",");

            if (event.getSource().equals(testFileButtonOpen)){
                int returnVal = testFileChooser.showOpenDialog(App.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File testFile = testFileChooser.getSelectedFile();
                    fieldTestFile.setText(testFile.getAbsolutePath());
               }
            }

            if (event.getSource().equals(prodFileButtonOpen)){
                int returnVal = prodFileChooser.showOpenDialog(App.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File prodFile = prodFileChooser.getSelectedFile();
                    fieldProdFile.setText(prodFile.getAbsolutePath());
                }
            }

            csvOutputFile.append(fieldTestFile.getText());
            csvOutputFile.append(",");
            csvOutputFile.append(fieldProdFile.getText());
            csvOutputFile.flush();
            csvOutputFile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (event.getSource().equals(buttonEnter)) {
            if (fieldTestFile.getText().isBlank() || fieldProdFile.getText().isEmpty()){
                System.out.println("Please choose a file");
            } else {
                dispose();
                String command = "java -jar test-smell-tool.jar file.csv";
                Runtime run  = Runtime.getRuntime();
                try {
                    Process proc = run.exec(command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

        public static void main(String[]args){
            new App().setVisible(true);
        }

}