package views;

import player.Player;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

public class KeyBordListenerEven {
    public void keyBordListner(){
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if(((KeyEvent)event).getID()== KeyEvent.KEY_PRESSED){
                    switch (((KeyEvent)event).getKeyCode()){
                        case KeyEvent.VK_RIGHT:{
                            int a = Player.getFrame().getVolumControlerSlider().getValue();
                            Player.getFrame().getVolumControlerSlider().setValue(a);
                            Player.forward((float)((Player.getFrame().getProgressBar().getPercentComplete()*Player.getFrame().getProgressBar().getWidth() +10)/Player.getFrame().getProgressBar().getWidth()));
                        }
                        break;
                        case KeyEvent.VK_LEFT:{
                            Player.jumpTo((float)((Player.getFrame().getProgressBar().getPercentComplete()*Player.getFrame().getProgressBar().getWidth() - 5)/Player.getFrame().getProgressBar().getWidth()));
                        }
                        break;
                        case KeyEvent.VK_ESCAPE:{
                            if(!Player.getFrame().getMediaPlayer().fullScreen().isFullScreen())
                                Player.fullScreen();
                            else
                                Player.originalScreen();
                        }
                        break;
                        case KeyEvent.VK_UP:{
                            Player.getFrame().getVolumControlerSlider().setValue(Player.getFrame().getVolumControlerSlider().getValue() + 1);
                            Player.getControlFrame().getVolumConrolerSlider().setValue(Player.getFrame().getVolumControlerSlider().getValue());
                        }
                        break;
                        case KeyEvent.VK_DOWN:{
                            Player.getFrame().getVolumControlerSlider().setValue(Player.getFrame().getVolumControlerSlider().getValue() - 1);
                            Player.getControlFrame().getVolumConrolerSlider().setValue(Player.getFrame().getVolumControlerSlider().getValue());
                        }
                        break;
                        case KeyEvent.VK_SPACE:{
                            if (Player.getFrame().getMediaPlayer().status().isPlaying()){
                                Player.pause();
                                Player.getControlFrame().getPlayButton().setText(Player.getFrame().getPlayButton().getText());
                            }else {
                                Player.play();
                                Player.getControlFrame().getPlayButton().setText(Player.getFrame().getPlayButton().getText());
                            }
                            break;

                        }
                    }
                }
            }
        },AWTEvent.KEY_EVENT_MASK);
    }
}
