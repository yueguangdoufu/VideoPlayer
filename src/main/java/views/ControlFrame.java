package views;

import player.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlFrame extends JFrame{

    private JPanel contentPane;
    private JButton playButton;
    private JButton backwardButton;
    private JProgressBar progressBar;
    private JSlider volumConrolerSlider;
    private JButton smallButton;
    private JLabel volumLabel;
    private JPanel progressTimepanel;
    private JLabel currentLabel;
    private JLabel totalLabel;
    private JButton listButton;

    //生成frame，全屏后生成新的frame
    public ControlFrame(){
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setType(Type.UTILITY);
        setResizable(false);
        setUndecorated(true);
        setOpacity(0.5f);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100,100,623,66);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.setLayout(new BorderLayout(0,0));
        setContentPane(contentPane);
        JPanel panel = new JPanel();
        contentPane.add(panel,BorderLayout.CENTER);

        backwardButton = new JButton("<<");
        backwardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player.jumpTo((float)((progressBar.getPercentComplete()*progressBar.getWidth() - 5)/progressBar.getWidth()));

            }
        });

        playButton = new JButton(">");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        playButton.addMouseListener(new MouseAdapter() {

            String btnText = ">";

            @Override
            public void mouseClicked(MouseEvent e) {
                if (playButton.getText() == ">"){
                    Player.play();
                    btnText = "||";
                    playButton.setText(btnText);
                }else{
                    Player.pause();
                    btnText = ">";
                    playButton.setText(btnText);
                }
            }
        });

        panel.add(playButton);
        panel.add(backwardButton);

        JButton stopButton = new JButton("Stop");
        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player.stop();
                playButton.setText(">");
            }
        });
        panel.add(stopButton);

        JButton forwaodButton = new JButton(">>");
        forwaodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.jumpTo((float)((progressBar.getPercentComplete()*progressBar.getWidth() + 15)/progressBar.getWidth()));
            }
        });
        panel.add(forwaodButton);

        smallButton = new JButton("small");
        smallButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player.originalScreen();
            }
        });
        panel.add(smallButton);

        volumConrolerSlider = new JSlider();
        volumConrolerSlider.setPaintTicks(true);
        volumConrolerSlider.setSnapToTicks(true);
        volumConrolerSlider.setPaintLabels(true);
        panel.add(volumConrolerSlider);

        volumConrolerSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                volumConrolerSlider.setValue((int) (e.getX() * ((float) volumConrolerSlider.getMaximum() / volumConrolerSlider.getWidth())));
            }
        });
        volumConrolerSlider.setMaximum(120);

        volumLabel = new JLabel("0");
        panel.add(volumLabel);

        listButton = new JButton();
        listButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(listButton.getText() == "List>>"){
                    Player.getFrame().getPlayListFrame().setVisible(true);
                    Player.getFrame().getPlayListFrame().setBounds((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()-Player.getFrame().getPlayListFrame().getWidth(),0,400,Player.getFrame().getPlayListFrame().getWidth());
                    Player.getFrame().getPlayListFrame().setFlag(0);
                    listButton.setText("<<List");
                }else if (listButton.getText() == ("<<List")){
                    Player.getFrame().getPlayListFrame().setVisible(false);
                    listButton.setText("List>>");
                }
            }
        });
        panel.add(listButton);

        progressTimepanel = new JPanel();
        contentPane.add(progressTimepanel,BorderLayout.NORTH);
        progressTimepanel.setLayout(new BorderLayout(0,0));

        progressBar = new JProgressBar();
        progressTimepanel.add(progressBar,BorderLayout.CENTER);
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                Player.jumpTo((float)x/progressBar.getWidth());
            }
        });

        currentLabel = new JLabel("00:00");
        progressTimepanel.add(currentLabel,BorderLayout.WEST);

        totalLabel = new JLabel("00:00");
        progressTimepanel.add(totalLabel,BorderLayout.WEST);
        volumConrolerSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Player.setVolum(volumConrolerSlider.getValue());
            }
        });
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JSlider getVolumConrolerSlider() {
        return volumConrolerSlider;
    }

    public JLabel getVolumLabel() {
        return volumLabel;
    }

    public JLabel getCurrentLabel() {
        return currentLabel;
    }

    public JLabel getTotalLabel() {
        return totalLabel;
    }

    public JButton getListButton() {
        return listButton;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
