package BrainMapper_ver4.info;

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
    public void showAllNodeInfo() {

    }


    /**
     * テストコード
     */
    public static void main() {

    }

}
