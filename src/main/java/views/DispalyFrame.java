package views;

import player.Player;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class DispalyFrame extends JFrame{
    private JPanel contentPane;

    EmbeddedMediaPlayerComponent playerComponent;
    private JPanel panel;
    private JButton stopButton;
    private JButton playButton;
    private JPanel controlsPane;
    private JProgressBar progressBar;
    private JSlider volumControlerSlider;
    private JMenuBar menuBar;
    private JMenu mnFile;
    private JMenuItem mntmOpenVideo;
    private JMenuItem mntmOpenSubtitle;
    private JMenuItem mntmExit;
    private JButton forwardButton;
    private JButton backwardButton;
    private JButton fullScreenButton;
    private int flag =0;
    private KeyBordListenerEven kble;
    private JLabel volumLabel;
    private JPanel progressTimePanel;
    private JLabel currentLabel;
    private JLabel totalLabel;
    private static PlayListFrame playListFrame;
    private JButton listButton;


    public DispalyFrame(){
        playListFrame = new PlayListFrame();
        setIconImage(new ImageIcon("picture/icon.png").getImage());
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if(playListFrame.getFlag() == 0){
                    playListFrame.setVisible(true);
                    playListFrame.setBounds(Player.getFrame().getX() + Player.getFrame().getWidth()-15,Player.getFrame().getY(),400,Player.getFrame().getHeight());
                }
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                if(playListFrame.getFlag() == 0 && !Player.getFrame().getMediaPlayer().fullScreen().isFullScreen()){
                    playListFrame.setVisible(true);
                    if(Math.abs(Player.getFrame().getWidth() - Toolkit.getDefaultToolkit().getScreenSize().width ) <= 20){
                        playListFrame.setBounds((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 400), 0,400,Player.getFrame().getHeight());
                        playListFrame.setAlwaysOnTop(true);
                    }
                    else
                        playListFrame.setBounds(Player.getFrame().getX() + Player.getFrame().getWidth() - 15, Player.getFrame().getY(),400,Player.getFrame().getHeight());
                }
            }
        });
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100,100,653,394);
        kble = new KeyBordListenerEven();
        kble.keyBordListner();
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        mnFile = new JMenu("File");
        menuBar.add(mnFile);

        mntmOpenVideo = new JMenuItem("Open Video");
        mntmOpenVideo.setSelected(true);
        mnFile.add(mntmOpenVideo);

        mntmOpenSubtitle = new JMenuItem("Open Subtitle");
        mnFile.add(mntmOpenSubtitle);

        mntmExit = new JMenuItem("Exit");
        mnFile.add(mntmExit);

        mntmOpenVideo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.openVedio();
                playListFrame.setList(Player.getListView().getList());
                playListFrame.getScrollPane().setViewportView(playListFrame.getList());
            }
        });

        mntmOpenSubtitle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.openSubtitle();
            }
        });

        mntmExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player.exit();
            }
        });

        contentPane = new JPanel();
        contentPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                System.out.println();
            }
        });
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.setLayout(new BorderLayout(0,0));
        setContentPane(contentPane);

        JPanel videoPanel = new JPanel();
        contentPane.add(videoPanel,BorderLayout.CENTER);
        videoPanel.setLayout(new BorderLayout(0,0));

        playerComponent = new EmbeddedMediaPlayerComponent();
        final Canvas videoSurface = (Canvas) playerComponent.videoSurfaceComponent();
        videoSurface.addMouseListener(new MouseAdapter() {
            String btnText = ">";
            String btnText1 ="Full";
            Timer mouseTime;

            @Override
            public void mouseClicked(MouseEvent e) {
                int i = e.getButton();
                if(i == MouseEvent.BUTTON1){
                    if(e.getClickCount()==1){
                        mouseTime = new Timer(350, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(playButton.getText() ==">"){
                                    Player.play();
                                    btnText = "||";
                                    playButton.setText(btnText);
                                }else {
                                    Player.pause();
                                    btnText = ">";
                                    playButton.setText(btnText);
                                }
                                mouseTime.stop();
                            }
                        });
                        mouseTime.restart();
                    }else if(e.getClickCount() == 2 && mouseTime.isRunning()){
                        mouseTime.stop();
                        if(flag == 0){
                            Player.fullScreen();
                        }else if(flag == 1){
                            Player.originalScreen();
                        }
                    }
                }
            }
        });
        videoPanel.add(playerComponent,BorderLayout.CENTER);
        panel = new JPanel();
        videoPanel.add(panel,BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout(0,0));

        controlsPane = new JPanel();
        FlowLayout flowLayout = (FlowLayout) controlsPane.getLayout();
        panel.add(controlsPane);

        playButton = new JButton(">");
        playButton.addMouseListener(new MouseAdapter() {

            String btnText = ">";

            @Override
            public void mouseClicked(MouseEvent e) {
                if(playButton.getText()==">"){
                    Player.play();
                    btnText = "||";
                    playButton.setText(btnText);
                }else {
                    Player.pause();
                    btnText=">";
                    playButton.setText(btnText);
                }
            }
        });
        controlsPane.add(playButton);

        backwardButton = new JButton("<<");
        backwardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player.jumpTo((float)(progressBar.getPercentComplete()*progressBar.getWidth() - 5)/progressBar.getWidth());

            }
        });
        controlsPane.add(backwardButton);

        volumControlerSlider = new JSlider();
        volumControlerSlider.setPaintLabels(true);
        volumControlerSlider.setSnapToTicks(true);
        volumControlerSlider.setPaintTicks(true);
        volumControlerSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                volumControlerSlider.setValue((int)(e.getX()*((float)volumControlerSlider.getMaximum()/volumControlerSlider.getWidth())));

            }
        });
        volumControlerSlider.setValue(100);
        volumControlerSlider.setMaximum(120);
        volumControlerSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Player.setVolum(volumControlerSlider.getValue());
            }
        });

        forwardButton = new JButton(">>");
        forwardButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player.jumpTo((float)(progressBar.getPercentComplete()*progressBar.getWidth() + 10)/progressBar.getWidth());

            }
        });

        stopButton = new JButton("Stop");
        stopButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Player.stop();
                playButton.setText(">");
            }
        });
        controlsPane.add(stopButton);
        controlsPane.add(forwardButton);

        fullScreenButton = new JButton("Full");
        fullScreenButton.addMouseListener(new MouseAdapter() {

            String btnText = "Full";
            int flag = 0;

            @Override
            public void mouseClicked(MouseEvent e) {
                Player.fullScreen();
            }
        });
        controlsPane.add(fullScreenButton);

        controlsPane.add(volumControlerSlider);

        volumLabel = new JLabel("" + volumControlerSlider.getValue());
        controlsPane.add(volumLabel);

        listButton = new JButton();
        if(playListFrame.getFlag() == 1){
            listButton.setText("List>>");
        }else if(playListFrame.getFlag() == 0){
            listButton.setText("<<List");
        }
        listButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(listButton.getText() == "List>>"){
                    playListFrame.setVisible(true);
                    if(Math.abs(Player.getFrame().getWidth() - Toolkit.getDefaultToolkit().getScreenSize().width)<=2)
                        playListFrame.setBounds(Toolkit.getDefaultToolkit().getScreenSize().width - 400,0,400,Player.getFrame().getHeight());
                    else
                        playListFrame.setBounds(Player.getFrame().getX() + Player.getFrame().getWidth() -15,Player.getFrame().getY(),400,Player.getFrame().getHeight());
                    playListFrame.setFlag(0);
                    listButton.setText("<<List");
                }else if(listButton.getText() == "<<List"){
                    playListFrame.setVisible(false);
                    listButton.setText("List>>");
                }
            }
        });
        controlsPane.add(listButton);

        progressTimePanel = new JPanel();
        panel.add(progressTimePanel, BorderLayout.NORTH);
        progressTimePanel.setLayout(new BorderLayout(0, 0));

        progressBar = new JProgressBar();
        progressTimePanel.add(progressBar, BorderLayout.CENTER);
        progressBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                Player.jumpTo((float)x/progressBar.getWidth());
            }
        });

        currentLabel = new JLabel("00:00");
        progressTimePanel.add(currentLabel,BorderLayout.WEST);

        totalLabel = new JLabel("02:13");
        progressTimePanel.add(totalLabel,BorderLayout.EAST);


    }

    public EmbeddedMediaPlayer getMediaPlayer(){
        return playerComponent.mediaPlayer();
    }

    public JProgressBar getProgressBar(){
        return progressBar;
    }

    public EmbeddedMediaPlayerComponent getPlayerComponent(){
        return playerComponent;
    }

    public JButton getPlayButton() {
        return playButton;
    }

    public JPanel getControlsPane() {
        return controlsPane;
    }

    public JSlider getVolumControlerSlider() {
        return volumControlerSlider;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag){
        this.flag = flag;
    }

    public JLabel getVolumLabel() {
        return volumLabel;
    }

    public JPanel getProgressTimePanel() {
        return progressTimePanel;
    }

    public JLabel getCurrentLabel() {
        return currentLabel;
    }

    public JLabel getTotalLabel() {
        return totalLabel;
    }

    public static PlayListFrame getPlayListFrame() {
        return playListFrame;
    }

    public JButton getListButton() {
        return listButton;
    }
}
