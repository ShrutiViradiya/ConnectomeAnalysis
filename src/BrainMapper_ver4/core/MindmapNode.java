package BrainMapper_ver4.core;


import BrainMapper_ver4.core_support.Element;
import BrainMapper_ver4.core_support.MindmapEdgeList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by issey on 2016/02/14.
 */
public class MindmapNode extends JPanel implements Element, Serializable {

    String ElementID = "";
    int positionX;
    int positionY;
    int centerX;
    int centerY;
    int Width;
    int Height;
    MindmapField mmField;
    MindmapNote mmNote;
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

    MindmapEdgeList SrcEdgeList = new MindmapEdgeList();
    MindmapEdgeList DestEdgeList = new MindmapEdgeList();


    /**
     * IDを指定してコンストラクト
     *
     * @param elementID
     * @param text
     */
    public MindmapNode(String elementID, String text) {
        this.textarea = new JTextArea(text);
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
    public MindmapNode(String text) {
        this(makeElementID(), text);
    }

    public void setBelongingMindmapField(MindmapField field) {
        this.mmField = field;
    }

    public void setBelongingMindmapNote(MindmapNote note){
        this.mmNote = note;
    }

    public String getElementID() {
        return ElementID;
    }

    public static String makeElementID() {
        Calendar cal = Calendar.getInstance();
        return String.format("%04d", cal.get(Calendar.YEAR)) + String.format("%02d", cal.get(Calendar.MONTH) + 1) + String.format("%02d", cal.get(Calendar.DATE)) + "_" +
                String.format("%02d", cal.get(Calendar.HOUR)) + "_" + String.format("%02d", cal.get(Calendar.MINUTE)) + "_" + String.format("%02d", cal.get(Calendar.SECOND)) + "_" + String.format("%03d", cal.get(Calendar.MILLISECOND));
    }

    public JTextArea getTextarea() {
        return textarea;
    }

    public void setElementID(String elementID) {
        ElementID = elementID;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getLeftUpperCornerX() {
        return this.getX();
    }

    public int getLeftUpperCornerY() {
        return this.getY();
    }

    public int getWidth() {
        return super.getWidth();
    }

    public int getHeight() {
        return super.getHeight();
    }

    public int getCenterX() {
        return (int) (getLeftUpperCornerX() + (getWidth() / 2.0));
    }

    public int getCenterY() {
        return (int) (getLeftUpperCornerY() + (getHeight() / 2.0));
    }

    public int getRightUpperCornerX() {
        return getLeftUpperCornerX() + getWidth();
    }

    public int getRightUpperCornerY() {
        return getLeftUpperCornerY();
    }

    public int getRightLowerCornerX() {
        return getLeftUpperCornerX() + getWidth();
    }

    public int getRightLowerCornerY() {
        return getLeftUpperCornerY() + getHeight();
    }

    public int getLeftLowerCornerX() {
        return getLeftUpperCornerX();
    }

    public int getLeftLowerCornerY() {
        return getLeftUpperCornerY() + getHeight();
    }

    public MindmapEdgeList getSrcEdgeList() {
        return this.SrcEdgeList;
    }

    public MindmapEdgeList getDestEdgeList() {
        return this.DestEdgeList;
    }

    public void addToSrcEdgeList(MindmapEdge edge) {
        SrcEdgeList.add(edge);
    }

    public void addToDestEdgeList(MindmapEdge edge) {
        DestEdgeList.add(edge);
    }

    /**
     * TextAreaKeyActionHandler
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
            mmField.setSelectedNode(MindmapNode.this);//選択ノードに設定する
            mmField.setAsDescribeTarget(MindmapNode.this);

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
     */
    private class PanelAreaMouseActionHandler implements MouseListener, MouseMotionListener {
        protected double nextX = 0;
        protected double nextY = 0;

        @Override
        public void mouseClicked(MouseEvent e) {
            //System.out.println("mouseClicked!");
            ClickedTimingViewPortX = MindmapNode.this.getX();
            ClickedTimingViewPortY = MindmapNode.this.getY();
            //System.out.println("  (ClickedTimingViewPortX, ClickedTimingViewPortY) = (" + ClickedTimingViewPortX + ", " + ClickedTimingViewPortY + ")");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            PressedTimingViewPortX = MindmapNode.this.getX();
            PressedTimingViewPortY = MindmapNode.this.getY();
            //System.out.println("  (PressedTimingViewPortX, PressedTimingViewPortY) = (" + PressedTimingViewPortX + ", " + PressedTimingViewPortY + ")");
            startX = e.getXOnScreen();
            startY = e.getYOnScreen();
            //System.out.println("  (startX, startY) = (" + startX + ", " + startY + ")");
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            DraggedTimingViewPortX = MindmapNode.this.getX();
            DraggedTimingViewPortY = MindmapNode.this.getY();
            //System.out.println("  (DraggedTimingViewPortX, DraggedTimingViewPortY) = (" + DraggedTimingViewPortX + ", " + DraggedTimingViewPortY + ")");
            endX = e.getXOnScreen();
            endY = e.getYOnScreen();
            //System.out.println("  (endX, endY) = (" + endX + ", " + endY + ")");
            dX = endX - startX;
            dY = endY - startY;
            nextX = PressedTimingViewPortX + dX;
            nextY = PressedTimingViewPortY + dY;
            //System.out.println("  (nextViewPortX, nextViewPortY) = (" + nextViewPortX + ", " + nextViewPortY + ")");
            setBounds((int) nextX, (int) nextY, MindmapNode.this.getWidth(), MindmapNode.this.getHeight());

            //Edgeオブジェクトの再描画命令
            for (MindmapEdge edge : SrcEdgeList) {
                edge.setDrawPosition();
            }
            for (MindmapEdge edge : DestEdgeList) {
                edge.setDrawPosition();
            }

        }

        @Override
        public void mouseReleased(MouseEvent e) {
            ReleasedTimingViewPortX = MindmapNode.this.getX();
            ReleasedTimingViewPortY = MindmapNode.this.getY();
            //System.out.println("  (ReleasedTimingViewPortX, ReleasedTimingViewPortY) = (" + ReleasedTimingViewPortX + ", " + ReleasedTimingViewPortY + ")");

            /**
             * ノードの位置情報を記録
             */
            positionX = MindmapNode.this.getX();
            positionY = MindmapNode.this.getY();

            /**
             * オブジェクト再描画命令
             */
            //Edgeオブジェクトの再描画命令
            for (MindmapEdge edge : SrcEdgeList) {
                edge.repaint();
            }
            for (MindmapEdge edge : DestEdgeList) {
                edge.repaint();
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
     * Nodeの表示サイズの最適化
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

        g.drawRoundRect(1, 1, this.getWidth()-3, this.getHeight()-3, 5, 5);
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
