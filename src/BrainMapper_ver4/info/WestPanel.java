package BrainMapper_ver4.info;

import BrainMapper_ver4.elements.GraphEdge;
import BrainMapper_ver4.elements.GraphNode;
import BrainMapper_ver4.elements.GraphEdgeList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by issey on 2016/05/21.
 */
public class WestPanel extends JPanel {

    JTextArea WestTextArea = new JTextArea("西領域");
    JScrollPane WestScrollPane = new JScrollPane();
    JPanel MainPanel;

    /**
     * コンストラクタ
     */
    public WestPanel() {
        super();
        WestScrollPane.setViewportView(WestTextArea);
        WestScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        WestScrollPane.setEnabled(true);
        WestScrollPane.setPreferredSize(new Dimension(200, 600));
        this.add(WestScrollPane);

        /**
         * アクションの登録
         */
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                WestPanel.this.requestFocus();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (MainPanel == null) {
                    System.err.println("WestPanelにMainPanelが指定されていない。");
                    System.exit(1);
                }
                MainPanel.requestFocus();
            }
        });


    }

    /**
     * コンストラクタ
     *
     * @param MainPanel
     */
    public WestPanel(JPanel MainPanel) {
        this();
        setMainPanel(MainPanel);
    }

    /**
     * Setter、Getter
     */
    public void setTextToTextArea(String value) {
        this.WestTextArea.setText(value);
    }

    public JScrollPane getWestScrollPane() {
        return WestScrollPane;
    }

    public JTextArea getWestTextArea() {
        return WestTextArea;
    }

    public void setMainPanel(JPanel MainPanel) {
        this.MainPanel = MainPanel;
    }

    /**
     * 解析メソッド
     */
    public void showAllNodeInfo(GraphNode node) {
        String all_info = "";
        all_info = "<About this node>\n";
        all_info += node.getElementID() + "\n";
        all_info += node.getTextarea().getText() + "\n";

        all_info += "\n";

        all_info += "<About Outgoing Edges>\n";
        GraphEdgeList OutgoingEdges = node.getOutoingEdgeList();
        for (GraphEdge edge : OutgoingEdges) {
            all_info += edge.getElementID() + "\n";
        }
        all_info += "<About Entering Edges>\n";
        GraphEdgeList EnteringEdges = node.getEnteringEdgeList();
        for (GraphEdge edge : EnteringEdges) {
            all_info += edge.getElementID() + "\n";
        }

        WestTextArea.setText(all_info);
    }


    /**
     * テストコード
     */
    public static void main() {

    }

}
