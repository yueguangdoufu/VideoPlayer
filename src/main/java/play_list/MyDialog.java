package play_list;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MyDialog extends JDialog{
    private final JPanel contentPane = new JPanel();
    private JTextField txtAreYouSure;
    private JButton okButton;
    private JButton cancelButton;

    public MyDialog(){
        setAlwaysOnTop(true);
        setIconImage(new ImageIcon("picture/icon.png").getImage());
        setSize(350,155);
        getContentPane().setLayout(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        getContentPane().add(contentPane,BorderLayout.CENTER);
        contentPane.setLayout(new BorderLayout(0,0));
        {
            txtAreYouSure = new JTextField();
            txtAreYouSure.setEditable(false);
            txtAreYouSure.setForeground(new Color(255,0,0));
            txtAreYouSure.setHorizontalAlignment(SwingConstants.CENTER);
            contentPane.add(txtAreYouSure);
            txtAreYouSure.setColumns(10);
        }
        {
            JPanel buttonPane = new JPanel();
            getContentPane().add(buttonPane,BorderLayout.SOUTH);
            buttonPane.setLayout(new BorderLayout(0,0));
            {
                JPanel panel =new JPanel();
                buttonPane.add(panel);
                {
                    okButton = new JButton("OK");
                    panel.add(okButton);
                    okButton.setActionCommand("OK");
                    getRootPane().setDefaultButton(okButton);
                }
                {
                    cancelButton = new JButton("Cancel");
                    panel.add(cancelButton);
                    cancelButton.setActionCommand("Cancel");
                }
            }
        }



    }

    public JButton getOkButton() {
        return okButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void setText(String string) {
        txtAreYouSure.setText(string);
    }
}
