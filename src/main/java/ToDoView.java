import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToDoView extends JFrame {

    private JPanel mainPanel;
    private JTextField textFieldTask;
    private JButton buttonAdd;
    private JTextField textFieldWhose;
    private JTextArea textAreaLista;
    private JButton teljesítveButton;
    private JSpinner spinnerId;

    private DB db;

    public ToDoView(){
        db = new DB();
        db.connect("localhost", 3306, "root", "", "todo");

        setSize(550,600);
        setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldTask.getText() == "") {
                    JOptionPane.showMessageDialog(null, "Ki kell tölteni a feladat mezőt");
                    return;
                }
                db.addTask(new Task(textFieldTask.getText(), textFieldWhose.getText()));
                JOptionPane.showMessageDialog(null, "Hozzáadtál egy feladatot");
                updateTask();
            }
        });

        teljesítveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!db.getIdValidate((Integer) spinnerId.getValue())) {
                    JOptionPane.showMessageDialog(null, "Nincs ilyen feladat");
                    return;
                }
                db.removeTask((Integer) spinnerId.getValue());
                JOptionPane.showMessageDialog(null, "Sikeresen teljesítetted a feladatot :)");
                updateTask();
            }
        });

        updateTask();
    }

    private void updateTask() {
        textAreaLista.setText("");
        for (Task task : db.listTask()){
            if (textFieldWhose.getText() == ""){
                textAreaLista.append(db.getId(task.task, task.whose) + ". feladat: " + task.task + ".\n");
            }else {
                textAreaLista.append(db.getId(task.task, task.whose) + ". feladat: " + task.task + ", amit " + task.whose + " végezzen el.\n");
            }
        }
    }

    public static void main(String[] args){
        new ToDoView();
    }


}
