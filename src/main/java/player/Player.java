package player;

import com.sun.jna.NativeLibrary;
import history.ListHistory;
import play_list.ViewList;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.binding.RuntimeUtil;
import views.ControlFrame;
import views.DispalyFrame;
import views.MyLogo;
import views.VideoTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import uk.co.caprica.vlcj.player.embedded.fullscreen.adaptive.AdaptiveFullScreenStrategy;
import java.io.File;
import java.io.IOException;

public class Player {

    private static DispalyFrame frame;
    private static String filePath;
    private static ControlFrame controlFrame;
    private static VideoTime videoTime;
    private static ViewList listView = new ViewList();
    private static ListHistory listHistory = new ListHistory();

    public static void main(String[] args) {

        try {
            listView.setList(listHistory.readHistory());
            listView.setMap(listHistory.readHistoryMap());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //不同平台，加载vlclib
        if(RuntimeUtil.isWindows())
            filePath = "D:\\VLC";
        else if(RuntimeUtil.isMac())
            filePath = "/Applications/VLC.app/Contents/MacOS/lib";
        else if(RuntimeUtil.isNix())
            filePath = "home/linux/vlc/install/lib";

        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),filePath);
        System.out.println(LibVlc.libvlc_get_version());

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    frame = new DispalyFrame();
                    frame.setVisible(true);
                    controlFrame = new ControlFrame();

                    videoTime = new VideoTime();
                    //String[] optionDecode = {"--subsdec-encoding=GB18030--"};

                    new SwingWorker<String, Integer>() {

                        @Override
                        protected String doInBackground() throws Exception {
                            while (true) {
                                //当前时间和总时间
                                long totalTime = frame.getMediaPlayer().status().length();
                                long currentTime = frame.getMediaPlayer().status().time();
                                videoTime.timeCalculate(totalTime, currentTime);
                                frame.getCurrentLabel().setText(videoTime.getMinitueCurrent() + ":" + videoTime.getSecondCurrent());
                                frame.getTotalLabel().setText(videoTime.getMinitueTotal() + ":" + videoTime.getSecondTotal());
                                controlFrame.getCurrentLabel().setText(frame.getCurrentLabel().getText());
                                controlFrame.getTotalLabel().setText(frame.getTotalLabel().getText());

                                //获取进度百分比
                                float percent = (float) currentTime / totalTime;
                                publish((int) (percent * 100));
                                Thread.sleep(200);
                            }

                        }

                        protected void process(java.util.List<Integer> chunks) {
                            for (int v : chunks) {
                                frame.getProgressBar().setValue(v);
                                controlFrame.getProgressBar().setValue(v);
                            }
                        }
                    }.execute();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    //播放已打开的视频
    public static  void play(){
        frame.getMediaPlayer().controls().play();
        frame.getPlayButton().setText("||");
    }
    //暂停
    public static void pause(){
        frame.getMediaPlayer().controls().pause();
        frame.getPlayButton().setText(">");
    }
    //停止
    public static void stop(){
        frame.getMediaPlayer().controls().stop();
        frame.getPlayButton().setText(">");
    }
    //前进
    public static void forward(float to){
        frame.getMediaPlayer().controls().setTime((long)(to*frame.getMediaPlayer().status().length()));
    }
    public static void backward(){
        frame.getPlayerComponent().backward(frame.getMediaPlayer());
    }
    //后退
    public static void jumpTo(float to){
        frame.getMediaPlayer().controls().setTime((long)(to*frame.getMediaPlayer().status().length()));
    }

    //设置音量
    public static void setVolum(int v){
        frame.getMediaPlayer().audio().setVolume(v);
        frame.getVolumLabel().setText(""+frame.getMediaPlayer().audio().volume());
        controlFrame.getVolumLabel().setText("" +frame.getMediaPlayer().audio().volume());
    }

    //在电脑上打开
    public static void openVedio(){
        JFileChooser chooser = new JFileChooser();
        int v = chooser.showOpenDialog(null);
        if(v == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            String name = file.getName();
            String path = file.getAbsolutePath();

            if(listView.getList().contains(name)){
                listView.getList().remove(name);
                listView.setList(name);
                listView.setMap(name,path);
            }else {
                listView.setList(name);
                listView.setMap(name,path);
            }

            try {
                listHistory.writeHistory(listView.getList());
                listHistory.writeHistory(listView.getMap());
                System.out.println("read:" +listHistory.readHistory());
                System.out.println("read1:" + listHistory.readHistoryMap());
            }catch (Exception e){
                e.printStackTrace();
            }

            frame.getMediaPlayer().media().play(file.getAbsolutePath());
            frame.getPlayButton().setText("||");
        }
    }

    //从列表打开视频
    public static void openVedioFromList(String name){
        String path = listView.getMap().get(name);
        if(listView.getList().contains(name)){
            listView.getList().remove(name);
            listView.setList(name);
            listView.setMap(name,path);
        }else {
            listView.setList(name);
            listView.setMap(name,path);
        }

        try {
            listHistory.writeHistory(listView.getList());
            listHistory.writeHistory(listView.getMap());
        }catch (Exception e){
            e.printStackTrace();
        }

        frame.getMediaPlayer().media().play(path);
        frame.getPlayButton().setText("||");
    }

    //打开视频字幕
    public static void openSubtitle(){
        JFileChooser chooser = new JFileChooser();
        int v = chooser.showOpenDialog(null);
        if(v == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            frame.getMediaPlayer().subpictures().setSubTitleFile(file);
        }
    }

    //退出程序
    public static void exit(){
        frame.getMediaPlayer().release();
        System.exit(0);
    }

    //进入全屏
    public static void fullScreen(){
        frame.getMediaPlayer().fullScreen().strategy(new AdaptiveFullScreenStrategy(frame));
        frame.getProgressBar().setVisible(false);
        frame.getControlsPane().setVisible(false);
        frame.getProgressTimePanel().setVisible(false);
        frame.getJMenuBar().setVisible(false);
        frame.getMediaPlayer().fullScreen().set(true);
        controlFrame.getVolumLabel().setText("" + frame.getMediaPlayer().audio().volume());
        controlFrame.getListButton().setText("List>>");

        frame.setFlag(1);
        frame.getPlayerComponent().videoSurfaceComponent().addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (frame.getFlag() == 1){
                    controlFrame.setLocation((frame.getWidth() -controlFrame.getWidth())/2,frame.getHeight() - controlFrame.getHeight());
                    controlFrame.setVisible(true);
                    controlFrame.getVolumConrolerSlider().setValue(frame.getVolumControlerSlider().getValue());
                    if(frame.getMediaPlayer().status().isPlaying()){
                        controlFrame.getPlayButton().setText("||");
                    }else
                        controlFrame.getPlayButton().setText(">");
                }
            }
        });
    }

    //退出全屏
    public static void originalScreen(){
        frame.getProgressBar().setVisible(true);
        frame.getControlsPane().setVisible(true);
        frame.getProgressTimePanel().setVisible(true);
        frame.getJMenuBar().setVisible(true);
        frame.getMediaPlayer().fullScreen().set(false);
        frame.setFlag(0);
        if (frame.getMediaPlayer().status().isPlaying())
            frame.getPlayButton().setText("||");
        else
            frame.getPlayButton().setText(">");

        if (frame.getPlayListFrame().getFlag() == 1) {
            frame.getListButton().setText("List>>");
        } else if (frame.getPlayListFrame().getFlag() == 0) {
            frame.getListButton().setText("<<List");
        }
        controlFrame.setVisible(false);
    }

    public static DispalyFrame getFrame(){
        return frame;
    }

    public static ControlFrame getControlFrame(){
        return controlFrame;
    }

    public void setLogo(){
        MyLogo logo = new MyLogo();
        frame.getMediaPlayer().logo().set(logo.getLogo());
    }

    public static ViewList getListView(){
        return listView;
    }

    public static ListHistory getListHistory(){
        return listHistory;
    }
}
