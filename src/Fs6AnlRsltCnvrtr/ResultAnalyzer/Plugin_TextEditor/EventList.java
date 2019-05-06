package Fs6AnlRsltCnvrtr.ResultAnalyzer.Plugin_TextEditor;

import javax.swing.*;

/**
 * Created by issey on 2017/05/19.
 */
public class EventList extends JList{


    private DefaultListModel toDoListModel;
    /**
     * Constructs a <code>JList</code> with an empty, read-only, model.
     */
    public EventList() {
         // 一覧を生成
            toDoListModel = new DefaultListModel();//リストモデルを生成
            this.setModel(toDoListModel);
            toDoListModel.addElement("a");
            toDoListModel.addElement("b");
            toDoListModel.addElement("c");

    }


}
