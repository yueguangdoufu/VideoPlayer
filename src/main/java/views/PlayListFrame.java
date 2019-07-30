package views;

import play_list.MyDialog;
import player.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class PlayListFrame extends JFrame{

    private JPanel contentPane;
    private int flag = 0;
    private JList list = new JList();
    private JScrollPane scrollPane;
    private JPanel panel;
    private JButton historyClearButton;
    private DefaultListModel defaultListModel = new DefaultListModel();
    private JButton searchButton;
    private JTextField searchtField;
    private JPanel panel_1;

    //创建frame显示历史
    public PlayListFrame(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                flag = 1;
                Player.getFrame().getListButton().setText("List>>");
                Player.getControlFrame().getListButton().setText(Player.getFrame().getListButton().getText());
            }
        });
        setType(Type.UTILITY);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMaximizedBounds(new Rectangle((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 400,0,400,(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
        try {
            if(!Player.getListHistory().readHistory().isEmpty()){
                setList(Player.getListHistory().readHistory());
            }
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5,5,5,5));
        contentPane.setLayout(new BorderLayout(0,0));
        setContentPane(contentPane);

        scrollPane = new JScrollPane();
        scrollPane.setEnabled(false);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    String name = Player.getListView().getList().get(Player.getListView().getList().size() - 1 - list.getSelectedIndex());
                    Player.openVedioFromList(name);
                    setList(Player.getListView().getList());
                    getScrollPane().setViewportView(getList());
                }
            }
        });
        contentPane.add(scrollPane,BorderLayout.CENTER);
        scrollPane.setViewportView(getList());

        panel = new JPanel();
        contentPane.add(panel,BorderLayout.NORTH);
        panel.setLayout(new BorderLayout(0,0));

        panel_1 = new JPanel();
        panel.add(panel_1,BorderLayout.CENTER);
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER,5,5));

        searchtField = new JTextField();
        searchtField.setText("soon come!");
        panel_1.add(searchtField);
        searchtField.setColumns(10);

        //浏览历史,当检测到当前空闲时
        searchButton = new JButton("Search History");
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final MyDialog dialog = new MyDialog();
                dialog.setVisible(true);
                dialog.getCancelButton().setVisible(false);
                dialog.setText("The Performance will come soom!!");
                dialog.setBounds(Player.getFrame().getPlayListFrame().getX() + 15,Player.getFrame().getPlayListFrame().getY() + 100,350,115);
                dialog.getOkButton().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        dialog.setVisible(false);
                    }
                });
            }
        });
        panel_1.add(searchButton);

        historyClearButton = new JButton("Clear History");
        panel_1.add(historyClearButton);

        //清除历史
        historyClearButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final MyDialog dialog =new MyDialog();
                dialog.setVisible(true);
                dialog.setBounds(Player.getFrame().getPlayListFrame().getX() + 15,Player.getFrame().getPlayListFrame().getY() + 100,350,115);
                dialog.getCancelButton().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        dialog.setVisible(false);
                    }
                });

                dialog.getOkButton().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try{
                            dialog.setVisible(false);
                            Player.getListHistory().cleanHistory();
                            defaultListModel.clear();
                            list.setModel(defaultListModel);
                            scrollPane.setViewportView(getList());
                        }catch (IOException e1){
                            e1.printStackTrace();
                        }
                    }
                });


            }
        });

    }

    public int getFlag() {
        return flag;
    }


    @Override
    public JPanel getContentPane() {
        return contentPane;
    }

    public JList getList() {
        return list;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setList(ArrayList<String> arrayList) {

        defaultListModel =new DefaultListModel();
        for(int i = arrayList.size()-1;i>=0;i--){
            defaultListModel.addElement(arrayList.get(i));
        }

        list.setModel(defaultListModel);
    }
}
