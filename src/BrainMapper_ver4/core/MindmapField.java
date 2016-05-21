package BrainMapper_ver4.core;

import BrainMapper_ver4.core_support.Element;
import BrainMapper_ver4.core_support.MindmapEdgeList;
import BrainMapper_ver4.core_support.MindmapNodeList;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Created by issey on 2016/02/13.
 * MindmapNote.java は NogNote.java を真似て書いているのに合わせて、
 * この MindmapField.java は JTextAreaの骨格を真似て作り始めている。
 * ただし、スーパークラスはJPanelであって欲しいので
 */
public class MindmapField extends JPanel {

    MindmapNodeList NodeList = new MindmapNodeList();
    MindmapEdgeList EdgeList = new MindmapEdgeList();
    MindmapNode selectedNode;//選択中のノード
    MindmapNode anchoredNode;//アンカーをかける（バックグラウンドで選択する）ノード。
    MindmapNote mmNote;//MindmapNoteが保持する各種コンポーネントへのアクセスを確保するため

    MindmapNode root;
    MindmapNode first;

    private UndoManager undoManager = new UndoManager();
    UndoableEditSupport undoableEditSupport;


    /**
     * コンストラクタ
     */
    public MindmapField() {
        System.out.println("---------- MindmapField ----------");
        setPreferredSize(new Dimension(4000, 2400));
        setBackground(Color.PINK);
        setLayout(null);

/*
        if (NodeList.size() == 0) {
            System.out.println("NodeListのロードに失敗したようです");
            ArrayList<MindmapNode> NodeList = new ArrayList<>();
            root = new MindmapNode("root", this);
            first = new MindmapNode("first", this);
            NodeList.add(root);
            NodeList.add(first);
            //this.add(root);
            //root.setBounds(2000, 1200, 80, 30);
            this.NodeList = NodeList;
        }

*/
        //Nodeの展開
        deployNodes();
        //Edgeの展開
        deployEdges();

        /**
         * アクションに関する登録
         */
        addKeyListener(new KeyActionHandler());
        setFocusable(true);
        //addMouseListener(new MouseActionHandler());
        //addMouseMotionListener(new MouseActionHandler());

        //対象コンポーネントをUndoableEditSupportで包む
        //UndoableEditSupportはUndoableEditイベントをUndoManagerに配る役割を果たす。
        undoableEditSupport = new UndoableEditSupport(this);
        //対象コンポーネントを包んだUndoableEditSupportにUndoManagerをUndoableEditListerとして登録
        undoableEditSupport.addUndoableEditListener(undoManager);

    }

    public void deployNodes() {
        //int x = 2000;
        //int y = 1200;

        for (MindmapNode mmNode : this.NodeList) {
            this.add(mmNode);
            mmNode.setBounds(mmNode.getX(), mmNode.getY(), 80, 30);
            mmNode.adjustNodeSize();
            //mmNode.setBounds(x, y, 80, 30);
            //x = x + 100;
            //y = y + 100;
        }
    }

    public void deployEdges() {
        System.out.println("Edge数：" + this.EdgeList.size());
        for (MindmapEdge mmEdge : this.EdgeList) {
            this.add(mmEdge);
            //mmEdge.setBounds(mmEdge.getX(), mmEdge.getY(), 80, 30);
            //mmEdge.adjustNodeSize();
        }
    }

    public MindmapNode createNewNode(int x, int y, String value) {
        MindmapNode NewNode = new MindmapNode(value);//新たなノードオブジェクトの生成
        NewNode.setBelongingMindmapField(this);//所属するMindmapFieldを登録
        this.NodeList.add(NewNode);//MindmapFieldオブジェクトが持つNodeListにも登録
        this.add(NewNode);//Swingのコンポーネントとしても追加
        NewNode.setBounds(x, y, 100, 100);
        NewNode.adjustNodeSize();

        //UndoableEditSupport#postEdit()を呼び出すと、
        //UndoableEditHappened()用のイベントが作られて、
        //UndoableEditListerとして登録されている全てのUndoManagerに通知される
        //undoableEditSupport.postEdit(new UndoableEditEvent_1());

        return NewNode;
    }


    public void deleteNode(MindmapNode node) {
        //まず削除対象ノードに登録されているエッジを消す
        for (MindmapEdge edge : node.getSrcEdgeList()) {
            this.remove(edge);//Swingコンポーネントとしての存在を消す
            this.EdgeList.remove(edge);//MindmapField上の登録リストから消す
        }
        for (MindmapEdge edge : node.getDestEdgeList()) {
            this.remove(edge);//Swingコンポーネントとしての存在を消す
            this.EdgeList.remove(edge);//MindmapField上の登録リストから消す
        }

        //続いてMindmapFieldから対象ノードを消す
        this.remove(node);//Swingコンポーネントとしての存在を消す
        this.NodeList.remove(node);//MindmapField上の登録リストから消す
    }

    public MindmapEdge createNewEdge(MindmapNode srcNode, MindmapNode destNode) {
        MindmapEdge ExistingEdge = this.EdgeList.getEdgeBySrcNodeAndDestNode(srcNode, destNode);
        if (ExistingEdge != null) {//既に指定された起点、終点を持つエッジがあるなら既存のEdgeを返す
            System.out.println("The edge from " + srcNode.getElementID() + " to " + destNode.ElementID + "is already exiting.");
            return ExistingEdge;
        } else {//既に指定された起点、終点を持つエッジがないならば新たなEdgeを作る。
            MindmapEdge NewEdge = new MindmapEdge(srcNode, destNode);
            NewEdge.setBelongingMindmapField(this);
            this.EdgeList.add(NewEdge);
            this.add(NewEdge);
            NewEdge.setDrawPosition();
            NewEdge.repaint();
            System.out.println("A new edge from " + srcNode.getElementID() + " to " + destNode.ElementID + "has been added.");
            return NewEdge;
        }
    }

    public void deleteEdge(MindmapNode srcNode, MindmapNode destNode) {
        MindmapEdge ExistingEdge = this.EdgeList.getEdgeBySrcNodeAndDestNode(srcNode, destNode);
        if (ExistingEdge != null) {//既に指定された起点、終点を持つエッジがあるなら既存のEdgeを返す

            //SrcNodeの登録から消す
            srcNode.getSrcEdgeList().remove(ExistingEdge);
            //DestNodeの登録から消す
            destNode.getDestEdgeList().remove(ExistingEdge);
            //MindmapFieldの登録から消す
            this.EdgeList.remove(ExistingEdge);
            //MindmapFieldのJComponentとしての存在を消す。
            this.remove(ExistingEdge);
            this.repaint();
            System.out.println("The edge from " + srcNode.getElementID() + " to " + destNode.ElementID + "has removed.");
        } else {//既に指定された起点、終点を持つエッジがないならば、何もしない。
            System.out.println("The edge from " + srcNode.getElementID() + " to " + destNode.ElementID + "is not exiting.");
        }
    }

    public ArrayList<MindmapNode> getNodeList() {
        return this.NodeList;
    }

    public ArrayList<MindmapEdge> getEdgeList() {
        return this.EdgeList;
    }

    public void setNodeList(MindmapNodeList NodeList) {
        this.NodeList = NodeList;
        for (MindmapNode node : NodeList) {
            node.setBelongingMindmapField(this);
        }
    }

    public void setEdgeList(MindmapEdgeList EdgeList) {
        this.EdgeList = EdgeList;
        for (MindmapEdge edge : EdgeList) {
            edge.setBelongingMindmapField(this);
        }
    }

    public void setMindmapNote(MindmapNote mmNote) {
        this.mmNote = mmNote;
    }

    public MindmapNode getRootNode() {
        return root;
    }

    public MindmapNode getFirstNode() {
        return first;
    }

    public void setSelectedNode(MindmapNode node) {
        //既に選択されているNodeに対して、Diactivate処理
        //System.out.println("何か選択されていたり、新たに選択されたNodeが既に選択中でないなら");
        //System.out.println("既に選択中のNodeのアクティブ枠を消す");
        if (this.selectedNode != null) {
            if (this.selectedNode != node) {
                this.selectedNode.changeBorder(1);
            }
            if (this.selectedNode != node && this.selectedNode.getTextarea().isEnabled()) {
                //新たに選択されようとしているノードが、既に登録されているノードではなく
                //既に登録されているノードが編集状態になっている時
                //ディアクティベート処理
                this.selectedNode.getTextarea().setCaretPosition(0);
                this.selectedNode.getTextarea().setEnabled(false);
            }
        }
        this.selectedNode = node;
        mmNote.setFootRightLbelText(selectedNode.getElementID());
        selectedNode.getTextarea().setBorder(selectedNode.ActiveBorder);
    }

    public void setAnchoredNode(MindmapNode node) {
        this.anchoredNode = node;
    }

    public void setAsRootNode(MindmapNode node) {
        this.root = node;
    }

    public void selectRootNode() {
        //既に選択されているNodeに対して、Diactivate処理
        //System.out.println("何か選択されていたり、新たに選択されたNodeが既に選択中でないなら");
        //System.out.println("既に選択中のNodeのアクティブ枠を消す");
        if (this.selectedNode != null) {
            if (this.selectedNode != root) {
                this.selectedNode.changeBorder(1);
            }
            if (this.selectedNode != root && this.selectedNode.getTextarea().isEnabled()) {
                //新たに選択されようとしているノードが、既に登録されているノードではなく
                //既に登録されているノードが編集状態になっている時
                //ディアクティベート処理
                this.selectedNode.getTextarea().setCaretPosition(0);
                this.selectedNode.getTextarea().setEnabled(false);
            }
        }
        this.selectedNode = root;
        selectedNode.getTextarea().setBorder(selectedNode.ActiveBorder);
    }

    public Element getSelectedNode() {
        return this.selectedNode;
    }


    public void setAsDescribeTarget(MindmapNode node) {
        //既に選択されているNodeに対して、Diactivate処理
        //System.out.println("何か選択されていたり、新たに選択されたNodeが既に選択中でないなら");
        //System.out.println("既に選択中のNodeのアクティブ枠を消す");
        if (this.selectedNode != null) {
            if (this.selectedNode != node) {
                this.selectedNode.changeBorder(1);
            }
            if (this.selectedNode != node && this.selectedNode.getTextarea().isEnabled()) {
                //新たに選択されようとしているノードが、既に登録されているノードではなく
                //既に登録されているノードが編集状態になっている時
                //ディアクティベート処理
                this.selectedNode.getTextarea().setCaretPosition(0);
                this.selectedNode.getTextarea().setEnabled(false);
            }
        }
        this.selectedNode = node;
        mmNote.setFootRightLbelText(selectedNode.getElementID());
        selectedNode.getTextarea().setBorder(selectedNode.ActiveBorder);
    }

    private class KeyActionHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            //System.out.println("keyTyped@MindmapField");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println("KeyPressed@MindmapField");
            //System.out.println("Selected Node ID: " + selectedNode.getElementID());
            int modifier = e.getModifiersEx();
            int keycode = e.getKeyCode();

            //新しいノードの追加
            if (keycode == KeyEvent.VK_INSERT) {
                System.out.println("ノードの追加");
                MindmapNode NewNode = createNewNode(selectedNode.getLeftUpperCornerX() + selectedNode.getWidth() + 10, selectedNode.getLeftUpperCornerY(), "New Node");
                NewNode.repaint();
                setSelectedNode(NewNode);
                NewNode.getTextarea().setBorder(NewNode.ActiveBorder);
                NewNode.adjustNodeSize();
            }

            //ノードの削除
            if (keycode == KeyEvent.VK_DELETE) {
                System.out.println("ノードの削除");
                if (selectedNode != null) {
                    deleteNode(selectedNode);
                    mmNote.setFootRightLbelText("Node [" + selectedNode.getElementID() + "] has been deleted.");
                    selectedNode = null;
                }
                MindmapField.this.repaint();
            }

            //アンカー設定
            if (keycode == KeyEvent.VK_A) {
                if (modifier == InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK) {
                    System.out.println("Anchored Node ID: " + selectedNode.getElementID());
                    setAnchoredNode(selectedNode);
                }
            }

            //Edgeを追加 ― アンカーノードへ選択中ノードから
            if (keycode == KeyEvent.VK_T) {
                if (modifier == InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK) {
                    if (selectedNode != null && anchoredNode != null) {
                        MindmapEdge NewEdge = createNewEdge(selectedNode, anchoredNode);
                    }
                }
            }

            //Edgeを追加 ― アンカーノードから選択中ノードへ
            if (keycode == KeyEvent.VK_F) {
                if (modifier == InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK) {
                    if (selectedNode != null && anchoredNode != null) {
                        MindmapEdge NewEdge = createNewEdge(anchoredNode, selectedNode);
                    }
                }
            }

            //Edgeを削除 ― アンカーノードへ選択中ノードから
            if (keycode == KeyEvent.VK_T) {
                if (modifier == InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK) {
                    if (selectedNode != null && anchoredNode != null) {
                        deleteEdge(selectedNode, anchoredNode);
                    }
                }
            }

            //Edgeを削除 ― アンカーノードから選択中ノードへ
            if (keycode == KeyEvent.VK_F) {
                if (modifier == InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK) {
                    if (selectedNode != null && anchoredNode != null) {
                        deleteEdge(anchoredNode, selectedNode);
                    }
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //System.out.println("keyReleased@MindmapField");

        }

    }

}

class AddNewNodeAction extends AbstractAction {
    MindmapField mmField;

    /**
     * コンストラクター.
     *
     */
    public AddNewNodeAction(MindmapField mmField) {
        super("新しいノードの追加");
        this.mmField = mmField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (mmField.selectedNode != null) {
            System.out.println("ノードの追加");
            MindmapNode NewNode = mmField.createNewNode(mmField.selectedNode.getLeftUpperCornerX() + mmField.selectedNode.getWidth() + 10, mmField.selectedNode.getLeftUpperCornerY(), "New Node");
            NewNode.repaint();
            mmField.setSelectedNode(NewNode);
            NewNode.getTextarea().setBorder(NewNode.ActiveBorder);
            NewNode.adjustNodeSize();
        }else{
            System.err.println("No node is selected.");
        }
    }
}