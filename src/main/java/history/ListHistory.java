package history;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

public class ListHistory {

    //默认保存路径
    private static String path1 = "H:\\output.txt";
    private static String path2 = "H:\\output1.txt";

    //写入历史:覆盖已有
    public static void writeHistory(ArrayList<String> list) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path1);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(list);
        objectOutputStream.close();
    }

    //读取历史：来自已读文件
    public static ArrayList<String> readHistory() throws ClassNotFoundException, IOException {
        FileInputStream fileInputStream = new FileInputStream(path1);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        ArrayList<String> historyList = (ArrayList<String>) objectInputStream.readObject();
        objectInputStream.close();
        return historyList;
    }

    //写入历史：文件路径和文件名
    public static void writeHistory(HashMap<String,String> map) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(path2);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(map);
        objectOutputStream.close();
    }

    //读取历史:文件名及路径
    public static HashMap<String,String> readHistoryMap() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(path2);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        HashMap<String,String> historyMap = (HashMap<String, String>) objectInputStream.readObject();
        objectInputStream.close();
        return historyMap;
    }

    //清除储存历史
    public static void cleanHistory() throws IOException {
        RandomAccessFile file1 = new RandomAccessFile(path1,"rw");
        RandomAccessFile file2 = new RandomAccessFile(path2,"rw");
        FileChannel fileChannel1 = file1.getChannel();
        FileChannel fileChannel2 = file2.getChannel();
        fileChannel1.truncate(1);
        fileChannel2.truncate(1);
    }

}
