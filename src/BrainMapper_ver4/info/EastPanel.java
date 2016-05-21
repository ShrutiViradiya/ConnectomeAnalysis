package BrainMapper_ver4.info;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by issey on 2016/05/21.
 */
public class EastPanel extends JPanel {

    JTextArea EastTextArea = new JTextArea("東領域");
    JScrollPane EastScrollPane = new JScrollPane();
    JPanel MainPanel;

    /**
     * コンストラクタ
     */
    public EastPanel() {
        super();
        EastScrollPane.setViewportView(EastTextArea);
        EastScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        EastScrollPane.setEnabled(true);
        EastScrollPane.setPreferredSize(new Dimension(200, 600));
        this.add(EastScrollPane);

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
                EastPanel.this.requestFocus();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (MainPanel == null) {
                    System.err.println("EastPanelにMainPanelが指定されていない。");
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
    public EastPanel(JPanel MainPanel) {
        this();
        setMainPanel(MainPanel);
    }

    /**
     * Setter、Getter
     */
    public void setTextToTextArea(String value) {
        this.EastTextArea.setText(value);
    }

    public JScrollPane getEastScrollPane() {
        return EastScrollPane;
    }

    public JTextArea getEastTextArea() {
        return EastTextArea;
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
