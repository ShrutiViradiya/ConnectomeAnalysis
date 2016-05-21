package BrainMapper_ver4.core;

import BrainMapper_ver4.core_support.GraphElement;
import BrainMapper_ver4.core_support.GraphEdgeList;
import BrainMapper_ver4.core_support.GraphNodeList;

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
 * MainFrame.java は NogNote.java を真似て書いているのに合わせて、
 * この GraphField.java は JTextAreaの骨格を真似て作り始めている。
 * ただし、スーパークラスはJPanelであって欲しいので
 */
public class GraphField extends GraphElement {

    GraphNodeList NodeList = new GraphNodeList();
    GraphEdgeList EdgeList = new GraphEdgeList();
    GraphNode selectedNode;//選択中のノード
    GraphNode anchoredNode;//アンカーをかける（バックグラウンドで選択する）ノード。
    MainFrame main_frame;//MindmapNoteが保持する各種コンポーネントへのアクセスを確保するため

    GraphNode root;
    GraphNode first;

    private UndoManager undoManager = new UndoManager();
    UndoableEditSupport undoableEditSupport;


    /**
     * コンストラクタ
     */
    public GraphField(MainFrame main_frame) {
        System.out.println("---------- Constructor of GraphField ----------");
        this.main_frame = main_frame;

        setPreferredSize(new Dimension(4000, 2400));
        setBackground(Color.DARK_GRAY);
        setLayout(null);


/*
        if (NodeList.size() == 0) {
            System.out.println("NodeListのロードに失敗したようです");
            ArrayList<GraphNode> NodeList = new ArrayList<>();
            root = new GraphNode("root", this);
            first = new GraphNode("first", this);
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

        for (GraphNode mmNode : this.NodeList) {
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
        for (GraphEdge mmEdge : this.EdgeList) {
            this.add(mmEdge);
            //mmEdge.setBounds(mmEdge.getX(), mmEdge.getY(), 80, 30);
            //mmEdge.adjustNodeSize();
        }
    }

    public GraphNode createNewNode(int x, int y, String value) {
        GraphNode NewNode = new GraphNode(value);//新たなノードオブジェクトの生成
        NewNode.setBelongingGraphField(this);//所属するGraphFieldを登録
        NewNode.setBelongingMainFrame(main_frame);
        this.NodeList.add(NewNode);//GraphFieldオブジェクトが持つNodeListにも登録
        this.add(NewNode);//Swingのコンポーネントとしても追加
        NewNode.setBounds(x, y, 100, 100);
        NewNode.adjustNodeSize();

        //UndoableEditSupport#postEdit()を呼び出すと、
        //UndoableEditHappened()用のイベントが作られて、
        //UndoableEditListerとして登録されている全てのUndoManagerに通知される
        //undoableEditSupport.postEdit(new UndoableEditEvent_1());

        return NewNode;
    }


    public void deleteNode(GraphNode node) {
        //まず削除対象ノードに登録されているエッジを消す
        for (GraphEdge edge : node.getSrcEdgeList()) {
            this.remove(edge);//Swingコンポーネントとしての存在を消す
            this.EdgeList.remove(edge);//GraphField上の登録リストから消す
        }
        for (GraphEdge edge : node.getDestEdgeList()) {
            this.remove(edge);//Swingコンポーネントとしての存在を消す
            this.EdgeList.remove(edge);//GraphField上の登録リストから消す
        }

        //続いてGraphFieldから対象ノードを消す
        this.remove(node);//Swingコンポーネントとしての存在を消す
        this.NodeList.remove(node);//GraphField上の登録リストから消す
    }

    public GraphEdge createNewEdge(GraphNode srcNode, GraphNode destNode) {
        GraphEdge ExistingEdge = this.EdgeList.getEdgeBySrcNodeAndDestNode(srcNode, destNode);
        if (ExistingEdge != null) {//既に指定された起点、終点を持つエッジがあるなら既存のEdgeを返す
            System.out.println("The edge from " + srcNode.getElementID() + " to " + destNode.ElementID + "is already exiting.");
            return ExistingEdge;
        } else {//既に指定された起点、終点を持つエッジがないならば新たなEdgeを作る。
            GraphEdge NewEdge = new GraphEdge(srcNode, destNode);
            NewEdge.setBelongingGraphField(this);
            NewEdge.setBelongingMainFrame(main_frame);
            this.EdgeList.add(NewEdge);
            this.add(NewEdge);
            NewEdge.setDrawPosition();
            NewEdge.repaint();
            System.out.println("A new edge from " + srcNode.getElementID() + " to " + destNode.ElementID + "has been added.");
            return NewEdge;
        }
    }

    public void deleteEdge(GraphNode srcNode, GraphNode destNode) {
        GraphEdge ExistingEdge = this.EdgeList.getEdgeBySrcNodeAndDestNode(srcNode, destNode);
        if (ExistingEdge != null) {//既に指定された起点、終点を持つエッジがあるなら既存のEdgeを返す

            //SrcNodeの登録から消す
            srcNode.getSrcEdgeList().remove(ExistingEdge);
            //DestNodeの登録から消す
            destNode.getDestEdgeList().remove(ExistingEdge);
            //GraphFieldの登録から消す
            this.EdgeList.remove(ExistingEdge);
            //GraphFieldのJComponentとしての存在を消す。
            this.remove(ExistingEdge);
            this.repaint();
            System.out.println("The edge from " + srcNode.getElementID() + " to " + destNode.ElementID + "has removed.");
        } else {//既に指定された起点、終点を持つエッジがないならば、何もしない。
            System.out.println("The edge from " + srcNode.getElementID() + " to " + destNode.ElementID + "is not exiting.");
        }
    }

    public ArrayList<GraphNode> getNodeList() {
        return this.NodeList;
    }

    public ArrayList<GraphEdge> getEdgeList() {
        return this.EdgeList;
    }

    /**
     * 設定ファイルをLoadする際に使う
     */
    public void setNodeList(GraphNodeList NodeList) {
        this.NodeList = NodeList;
        for (GraphNode node : NodeList) {
            node.setBelongingGraphField(this);
            node.setBelongingMainFrame(main_frame);
        }
    }

    /**
     * 設定ファイルをLoadする際に使う
     *
     * @param EdgeList
     */
    public void setEdgeList(GraphEdgeList EdgeList) {
        this.EdgeList = EdgeList;
        for (GraphEdge edge : EdgeList) {
            edge.setBelongingGraphField(this);
            edge.setBelongingMainFrame(main_frame);
        }
    }

    public void setMindmapNote(MainFrame mmNote) {
        this.main_frame = mmNote;
    }

    public GraphNode getRootNode() {
        return root;
    }

    public GraphNode getFirstNode() {
        return first;
    }

    public void setSelectedNode(GraphNode node) {
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
        main_frame.setFootRightLbelText(selectedNode.getElementID());
        selectedNode.getTextarea().setBorder(selectedNode.ActiveBorder);
    }

    public void setAnchoredNode(GraphNode node) {
        this.anchoredNode = node;
    }

    public void setAsRootNode(GraphNode node) {
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

    public GraphElement getSelectedNode() {
        return this.selectedNode;
    }


    private class KeyActionHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            //System.out.println("keyTyped@GraphField");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("KeyPressed@GraphField");
            //System.out.println("Selected Node ID: " + selectedNode.getElementID());
            int modifier = e.getModifiersEx();
            int keycode = e.getKeyCode();

            //新しいノードの追加
            if (keycode == KeyEvent.VK_INSERT) {
                System.out.println("ノードの追加");
                GraphNode NewNode = createNewNode(selectedNode.getLeftUpperCornerX() + selectedNode.getWidth() + 10, selectedNode.getLeftUpperCornerY(), "New Node");
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
                    main_frame.setFootRightLbelText("Node [" + selectedNode.getElementID() + "] has been deleted.");
                    selectedNode = null;
                }
                GraphField.this.repaint();
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
                        GraphEdge NewEdge = createNewEdge(selectedNode, anchoredNode);
                    }
                }
            }

            //Edgeを追加 ― アンカーノードから選択中ノードへ
            if (keycode == KeyEvent.VK_F) {
                if (modifier == InputEvent.SHIFT_DOWN_MASK + InputEvent.CTRL_DOWN_MASK) {
                    if (selectedNode != null && anchoredNode != null) {
                        GraphEdge NewEdge = createNewEdge(anchoredNode, selectedNode);
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
            //System.out.println("keyReleased@GraphField");

        }

    }

}

class AddNewNodeAction extends AbstractAction {
    GraphField mmField;

    /**
     * コンストラクター.
     */
    public AddNewNodeAction(GraphField mmField) {
        super("新しいノードの追加");
        this.mmField = mmField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (mmField.selectedNode != null) {
            System.out.println("ノードの追加");
            GraphNode NewNode = mmField.createNewNode(mmField.selectedNode.getLeftUpperCornerX() + mmField.selectedNode.getWidth() + 10, mmField.selectedNode.getLeftUpperCornerY(), "New Node");
            NewNode.repaint();
            mmField.setSelectedNode(NewNode);
            NewNode.getTextarea().setBorder(NewNode.ActiveBorder);
            NewNode.adjustNodeSize();
        } else {
            System.err.println("No node is selected.");
        }
    }
}