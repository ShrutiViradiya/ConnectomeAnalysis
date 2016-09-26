package BrainMapper_ver4.core;


import BrainMapper_ver4.core_support.GraphEdgeList;
import BrainMapper_ver4.core_support.GraphElement;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by issey on 2016/02/14.
 */
public class GraphNode extends GraphElement {

    GraphField gField;
    JTextArea textarea;

    protected double ViewPortW;
    protected double ViewPortH;
    protected double startX;
    protected double startY;
    protected double endX, endY;
    protected double dX, dY;
    protected double PressedTimingViewPortX, PressedTimingViewPortY;
    protected double ReleasedTimingViewPortX, ReleasedTimingViewPortY;
    protected double DraggedTimingViewPortX, DraggedTimingViewPortY;
    protected double ClickedTimingViewPortX, ClickedTimingViewPortY;

    GraphEdgeList EnteringEdgeList = new GraphEdgeList();
    GraphEdgeList OutgoingEdgeList = new GraphEdgeList();


    /**
     * IDを指定してコンストラクト
     *
     * @param elementID
     * @param textarea_value
     */
    public GraphNode(String elementID, String textarea_value) {
        this.textarea = new JTextArea(textarea_value);
        add(textarea);
        this.ElementID = elementID;
        setEnabled(false);
        this.addMouseListener(new PanelAreaMouseActionHandler());
        this.addMouseMotionListener(new PanelAreaMouseActionHandler());
        textarea.addKeyListener(new TextAreaKeyActionHandler());
        textarea.addMouseListener(new TextAreaMouseActionHandler());
        textarea.addMouseMotionListener(new TextAreaMouseActionHandler());


        setOpaque(false);
    }

    /**
     * IDを指定しないでコンストラクト
     */
    public GraphNode(String text) {
        this(makeElementID(), text);
    }

    public void setBelongingGraphField(GraphField field) {
        this.gField = field;
    }

    public JTextArea getTextarea() {
        return textarea;
    }


    public GraphEdgeList getOutoingEdgeList() {
        return this.EnteringEdgeList;
    }

    public GraphEdgeList getEnteringEdgeList() {
        return this.OutgoingEdgeList;
    }

    public void addToSrcEdgeList(GraphEdge edge) {
        EnteringEdgeList.add(edge);
    }

    public void addToDestEdgeList(GraphEdge edge) {
        OutgoingEdgeList.add(edge);
    }

    /**
     * TextAreaKeyActionHandler
     * -----------------------------------
     */
    private class TextAreaKeyActionHandler implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            //System.out.println("keyTyped@TextAreaKeyActionHandler");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println("KeyPressed@TextAreaKeyActionHandler");
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                System.out.println("編集終了");
                textarea.setCaretPosition(0);
                textarea.setEnabled(false);
                adjustNodeSize();
            }


        }

        @Override
        public void keyReleased(KeyEvent e) {
            //System.out.println("keyReleased@TextAreaKeyActionHandler");
        }

    }

    /**
     * TextAreaMouseActionHandler
     * -----------------------------------
     *
     */
    private class TextAreaMouseActionHandler implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent e) {

            //System.out.println("mouseClicked!");

            if (e.getClickCount() == 2) {
                if (!isEnabled()) {
                    System.out.println("編集開始");
                    //setBorder(ActiveBorder);
                    textarea.setEnabled(true);
                    textarea.requestFocus();
                    int initialCaretPosition = textarea.getCaretPosition();
                    textarea.setCaretPosition(initialCaretPosition);
                    repaint();
                }
            } else {
                //System.out.println("CaretPosition:" + getCaretPosition());
                repaint();
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //System.out.println("mouseEntered@TextAreaMouseActionHandler");
            //選択ノードに設定する
            gField.setSelectedNode(GraphNode.this);

            //ノードに関する情報を西パネルに表示
            if (main_frame == null) {
                System.out.println("main_frameがnull");
            } else {
                main_frame.getWest_panel().showAllNodeInfo(GraphNode.this);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    /**
     * PanelAreaMouseActionHandler
     * -----------------------------------
     */
    private class PanelAreaMouseActionHandler implements MouseListener, MouseMotionListener {
        protected double nextX = 0;
        protected double nextY = 0;

        @Override
        public void mouseClicked(MouseEvent e) {
            //System.out.println("mouseClicked!");
            ClickedTimingViewPortX = GraphNode.this.getX();
            ClickedTimingViewPortY = GraphNode.this.getY();
            //System.out.println("  (ClickedTimingViewPortX, ClickedTimingViewPortY) = (" + ClickedTimingViewPortX + ", " + ClickedTimingViewPortY + ")");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            PressedTimingViewPortX = GraphNode.this.getX();
            PressedTimingViewPortY = GraphNode.this.getY();
            //System.out.println("  (PressedTimingViewPortX, PressedTimingViewPortY) = (" + PressedTimingViewPortX + ", " + PressedTimingViewPortY + ")");
            startX = e.getXOnScreen();
            startY = e.getYOnScreen();
            //System.out.println("  (startX, startY) = (" + startX + ", " + startY + ")");
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            DraggedTimingViewPortX = GraphNode.this.getX();
            DraggedTimingViewPortY = GraphNode.this.getY();
            //System.out.println("  (DraggedTimingViewPortX, DraggedTimingViewPortY) = (" + DraggedTimingViewPortX + ", " + DraggedTimingViewPortY + ")");
            endX = e.getXOnScreen();
            endY = e.getYOnScreen();
            //System.out.println("  (endX, endY) = (" + endX + ", " + endY + ")");
            dX = endX - startX;
            dY = endY - startY;
            nextX = PressedTimingViewPortX + dX;
            nextY = PressedTimingViewPortY + dY;
            //System.out.println("  (nextViewPortX, nextViewPortY) = (" + nextViewPortX + ", " + nextViewPortY + ")");
            setBounds((int) nextX, (int) nextY, GraphNode.this.getWidth(), GraphNode.this.getHeight());

            //Edgeオブジェクトの再描画命令
            for (GraphEdge edge : EnteringEdgeList) {
                edge.setDrawPosition();
                //edge.textarea.revalidate();
                edge.revalidate();
            }
            for (GraphEdge edge : OutgoingEdgeList) {
                edge.setDrawPosition();
                //edge.textarea.revalidate();
                edge.revalidate();
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            ReleasedTimingViewPortX = GraphNode.this.getX();
            ReleasedTimingViewPortY = GraphNode.this.getY();
            //System.out.println("  (ReleasedTimingViewPortX, ReleasedTimingViewPortY) = (" + ReleasedTimingViewPortX + ", " + ReleasedTimingViewPortY + ")");

            /**
             * ノードの位置情報を記録
             */
            positionX = GraphNode.this.getX();
            positionY = GraphNode.this.getY();

            /**
             * オブジェクト再描画命令
             */
            //Edgeオブジェクトの再描画命令
            for (GraphEdge edge : EnteringEdgeList) {
                edge.repaint();
                //edge.textarea.revalidate();
                //edge.revalidate();
                edge.adjustTextAreaSize();
            }
            for (GraphEdge edge : OutgoingEdgeList) {
                edge.repaint();
                //edge.textarea.revalidate();
                //edge.revalidate();
                edge.adjustTextAreaSize();
            }

            //Nodeオブジェクトの再描画命令
            repaint();


        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }


        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }

    public Border DisactiveBorder = new LineBorder(Color.BLACK, 1, true);
    public Border ActiveBorder = new LineBorder(Color.RED, 2, true);

    /**
     * 1=DisactiveBorder
     * 2=ActiveBorder
     *
     * @param PatternNumber
     */
    public void changeBorder(int PatternNumber) {
        switch (PatternNumber) {
            case 1:
                this.getTextarea().setBorder(DisactiveBorder);
                //repaint();
                break;
            case 2:
                this.getTextarea().setBorder(ActiveBorder);
                //repaint();
                break;
        }
    }


    /**
     * Nodeの表示サイズをの最適化
     */
    public void adjustNodeSize() {
        FontMetrics fMetrics = textarea.getFontMetrics(textarea.getFont());
        textarea.setLineWrap(true);
        int newWidth = fMetrics.stringWidth(textarea.getText());
        int newHeight = fMetrics.getHeight();
        textarea.setSize(newWidth + 5, newHeight + 5);
        this.setSize(newWidth + 20, newHeight + 20);
    }


    /**
     *
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //色の指定
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.CYAN);

        //線の太さの指定
        BasicStroke wideStroke = new BasicStroke(2.0f);
        g2.setStroke(wideStroke);

        //アンチエイリアシング
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawRoundRect(1, 1, this.getWidth() - 3, this.getHeight() - 3, 5, 5);
        //丸いコーナー付きの輪郭の矩形を、このグラフィックスコンテキストの現在
        //の色を使用して描きます。描かれる矩形は、左端と右端がそれぞれ x と
        //x + width、上端と下端がそれぞれ y と y +height で指定されます。

        //パラメータ:
        //x - 描画される矩形の x 座標
        //y - 描画される矩形の y 座標
        //width - 描画される矩形の幅
        //height - 描画される矩形の高さ
        //arcWidth - 4 隅の弧の水平方向の直径
        //arcHeight - 4 隅の弧の垂直方向の直径
    }
}
