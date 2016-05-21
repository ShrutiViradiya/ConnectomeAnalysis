package BrainMapper_ver4.core;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by issey on 2016/04/30.
 */
public class MindmapEdge extends JPanel {

    String ElementID = "";
    MindmapField mmField;
    MindmapNote mmNote;
    JTextArea textarea;
    MindmapNode srcNode, destNode;
    double line_start_X, line_start_Y;
    double edge_panel_x, edge_panel_y;
    double line_end_X, line_end_Y;
    double edge_panel_width, edge_panel_height;

    ArrayList<MindmapNode> NodeList = new ArrayList<>();

    /**
     * IDを指定してコンストラクト
     *
     * @param elementID
     * @param srcNode
     * @param destNode
     */
    public MindmapEdge(String elementID, MindmapNode srcNode, MindmapNode destNode) {
        System.out.println("MindmapEdgeコンストラクタ");
        System.out.println("srcNode.getElementID()=" + srcNode.getElementID());
        System.out.println("destNode.getElementID()=" + destNode.getElementID());
        this.srcNode = srcNode;//起点ノードの登録
        this.srcNode.addToSrcEdgeList(this);//逆に起点ノードにこのエッジを登録する。
        this.destNode = destNode;//終点ノードの登録
        this.destNode.addToDestEdgeList(this);//逆に終点ノードにこのエッジを登録する。
        setDrawPosition();
        this.ElementID = elementID;
        this.textarea = new JTextArea("");

        //setBackground(Color.GREEN);
        setOpaque(false);

        //add(textarea);
    }

    /**
     * IDを指定しないでコンストラクト
     *
     * @param srcNode
     * @param destNode
     */
    public MindmapEdge(MindmapNode srcNode, MindmapNode destNode) {
        this(makeElementID(), srcNode, destNode);
    }

    /**
     * @param field
     */
    public void setBelongingMindmapField(MindmapField field) {
        this.mmField = field;
    }

    public void setBelongingMindmapNote(MindmapNote note){
        this.mmNote = note;
    }

    double arrow_head_1_x, arrow_head_1_y;
    double arrow_head_2_x, arrow_head_2_y;
    double arrow_head_3_x, arrow_head_3_y;

    /**
     *
     */
    public void setDrawPosition() {
        System.out.println("------ MindmapEdge#setDrawPosition() -------");
        System.out.println("srcNode.getElementID()=" + srcNode.getElementID());
        System.out.println("srcNode.getCenterX()=" + srcNode.getCenterX());
        System.out.println("srcNode.getCenterY()=" + srcNode.getCenterY());
        System.out.println("destNode.getElementID()=" + destNode.getElementID());
        System.out.println("destNode.getCenterX()=" + destNode.getCenterX());
        System.out.println("destNode.getCenterY()=" + destNode.getCenterY());

        //destNodeの中心とsrcNodeの中心と結ぶ線と水平線のなす角
        double radian1 = Math.atan2(destNode.getCenterY() - srcNode.getCenterY(), destNode.getCenterX() - srcNode.getCenterX());
        //destNodeの左上角と中心点を結ぶ直線と水平線のなす角
        double radian2 = Math.atan2(destNode.getCenterY() - destNode.getLeftUpperCornerY(), destNode.getCenterX() - destNode.getLeftUpperCornerX());
        //destNodeの右上角と中心点を結ぶ直線と水平線のなす角
        double radian3 = Math.atan2(destNode.getCenterY() - destNode.getRightUpperCornerY(), destNode.getCenterX() - destNode.getRightUpperCornerX());
        //destNodeの左下角と中心点を結ぶ直線と水平線のなす角
        double radian4 = Math.atan2(destNode.getCenterY() - destNode.getLeftLowerCornerY(), destNode.getCenterX() - destNode.getLeftLowerCornerX());
        //destNodeの右下角と中心点を結ぶ直線と水平線のなす角
        double radian5 = Math.atan2(destNode.getCenterY() - destNode.getRightLowerCornerY(), destNode.getCenterX() - destNode.getRightLowerCornerX());

        if (srcNode.getCenterX() < destNode.getCenterX()) {
            if (srcNode.getCenterY() < destNode.getCenterY()) {
                edge_panel_x = srcNode.getCenterX();
                edge_panel_y = srcNode.getCenterY();
                edge_panel_width = destNode.getCenterX() - srcNode.getCenterX();
                edge_panel_height = destNode.getCenterY() - srcNode.getCenterY();

                if (radian1 > radian2) {//上縁に交点
                    System.out.println("■左上に起点Node→右下に終点Node、上縁に交点");
                    //矢印の第１頂点をarrow_head_1とする。destNodeの縁とEdgeとの交点。
                    arrow_head_1_x = edge_panel_width - (destNode.getHeight() / 2) / Math.tan(radian1);
                    arrow_head_1_y = edge_panel_height - destNode.getHeight() / 2;
                } else {//左縁に交点
                    System.out.println("■左上に起点Node→右下に終点Node、左縁に交点");
                    //destNodeの縁とEdgeとの交点をarrow_head1とする。
                    arrow_head_1_x = edge_panel_width - destNode.getWidth() / 2;
                    arrow_head_1_y = edge_panel_height - (destNode.getWidth() / 2) * Math.tan(radian1);
                }
            } else {
                edge_panel_x = srcNode.getCenterX();
                edge_panel_y = destNode.getCenterY();
                edge_panel_width = destNode.getCenterX() - srcNode.getCenterX();
                edge_panel_height = srcNode.getCenterY() - destNode.getCenterY();

                if (radian1 > radian4) {//左縁に交点
                    System.out.println("■左下に起点Node→右上に終点Node、左縁に交点");
                    //destNodeの縁とEdgeとの交点をarrow_head1とする。
                    arrow_head_1_x = edge_panel_width - destNode.getWidth() / 2;
                    arrow_head_1_y = (destNode.getWidth() / 2) * Math.tan(2 * Math.PI - radian1);
                } else {//下縁に交点
                    System.out.println("■左下に起点Node→右上に終点Node、下縁に交点");
                    //destNodeの縁とEdgeとの交点をarrow_head1とする。
                    arrow_head_1_x = edge_panel_width - (destNode.getHeight() / 2) / Math.tan(2 * Math.PI - radian1);
                    arrow_head_1_y = destNode.getHeight() / 2;
                }
            }
        } else if (srcNode.getCenterX() >= destNode.getCenterX()) {

            if (srcNode.getCenterY() < destNode.getCenterY()) {
                edge_panel_x = destNode.getCenterX();
                edge_panel_y = srcNode.getCenterY();
                edge_panel_width = srcNode.getCenterX() - destNode.getCenterX();
                edge_panel_height = destNode.getCenterY() - srcNode.getCenterY();

                if (radian1 > radian3) {//右縁に交点
                    System.out.println("■右上に起点Node→左下に終点Node、右縁に交点");
                    //destNodeの縁とEdgeとの交点をarrow_head1とする。
                    arrow_head_1_x = destNode.getWidth() / 2;
                    arrow_head_1_y = edge_panel_height - (destNode.getWidth() / 2) * Math.tan(Math.PI - radian1);
                } else {//上縁に交点
                    System.out.println("■右上に起点Node→左下に終点Node、上縁に交点");
                    //destNodeの縁とEdgeとの交点をarrow_head1とする。
                    arrow_head_1_x = (destNode.getHeight() / 2) / Math.tan(Math.PI - radian1);
                    arrow_head_1_y = edge_panel_height - destNode.getHeight() / 2;
                }
            } else {
                edge_panel_x = destNode.getCenterX();
                edge_panel_y = destNode.getCenterY();
                edge_panel_width = srcNode.getCenterX() - destNode.getCenterX();
                edge_panel_height = srcNode.getCenterY() - destNode.getCenterY();

                if (radian1 < radian5) {//右縁に交点
                    System.out.println("■右下に起点Node→左上に終点Node、右縁に交点");
                    //destNodeの縁とEdgeとの交点をarrow_head1とする。
                    arrow_head_1_x = destNode.getWidth() / 2;
                    arrow_head_1_y = (destNode.getWidth() / 2) * Math.tan(radian1 - Math.PI);
                } else {//下縁に交点
                    System.out.println("■右下に起点Node→左上に終点Node、下縁に交点");
                    //destNodeの縁とEdgeとの交点をarrow_head1とする。
                    arrow_head_1_x = (destNode.getHeight() / 2) / Math.tan(radian1 - Math.PI);
                    arrow_head_1_y = destNode.getHeight() / 2;
                }
            }
        }

        //srcNodeとdestNodeが水平や垂直に並んだ時にEdgePanelの幅が０になる現象対策
        if (edge_panel_width < srcNode.getWidth() + destNode.getWidth()) {
            double min = srcNode.getWidth() + destNode.getWidth();
            edge_panel_width = edge_panel_width + min;
            edge_panel_x = edge_panel_x - min / 2;
            arrow_head_1_x = arrow_head_1_x + min / 2;
        }
        if (edge_panel_height < srcNode.getHeight() + destNode.getHeight()) {
            double min = srcNode.getHeight() + destNode.getHeight();
            edge_panel_height = edge_panel_height + min;
            edge_panel_y = edge_panel_y - min / 2;
            arrow_head_1_y = arrow_head_1_y + min / 2;
        }

        //Edgeの始点と終点を決める。
        line_start_X = srcNode.getCenterX() - edge_panel_x;
        line_start_Y = srcNode.getCenterY() - edge_panel_y;
        line_end_X = arrow_head_1_x;
        line_end_Y = arrow_head_1_y;
        System.out.println("edge_panel_x = " + edge_panel_x);
        System.out.println("edge_panel_y = " + edge_panel_y);
        System.out.println("line_end_X = " + line_end_X);
        System.out.println("line_end_Y = " + line_end_Y);

        //矢印描画の為に使った角度の確認表示
        double degree1 = radian1 * 180d / Math.PI;
        System.out.println("degree1=" + degree1);
        double degree2 = radian2 * 180d / Math.PI;
        System.out.println("degree2=" + degree2);
        double degree3 = radian1 * 180d / Math.PI;
        System.out.println("degree3=" + degree3);
        double degree4 = radian2 * 180d / Math.PI;
        System.out.println("degree4=" + degree4);
        double degree5 = radian1 * 180d / Math.PI;
        System.out.println("degree5=" + degree5);

        //矢印の第1頂点
        System.out.println("arrow_head_1=(" + arrow_head_1_x + ", " + arrow_head_1_y + ")");
        //矢印の第2頂点
        arrow_head_2_x = arrow_head_1_x - 10 * Math.cos(25 * Math.PI / 180 + radian1);
        arrow_head_2_y = arrow_head_1_y - 10 * Math.sin(25 * Math.PI / 180 + radian1);
        System.out.println("arrow_head_2=(" + arrow_head_2_x + ", " + arrow_head_2_y + ")");
        //矢印の第3頂点
        arrow_head_3_x = arrow_head_1_x - 10 * Math.cos(-25 * Math.PI / 180 + radian1);
        arrow_head_3_y = arrow_head_1_y - 10 * Math.sin(-25 * Math.PI / 180 + radian1);
        System.out.println("arrow_head_3=(" + arrow_head_3_x + ", " + arrow_head_3_y + ")");

        //EdgePanelの位置・大きさ指定
        setBounds((int) edge_panel_x, (int) edge_panel_y, (int) edge_panel_width, (int) edge_panel_height);
        if (mmField != null) {
            mmField.setComponentZOrder(this, mmField.getComponentCount() - 1);//最背面へ
            //mmField.setComponentZOrder(this, 0);//最前面へ
        }
        //System.out.println("@setDrawingPosition: " + (int) edge_panel_x + ", " + (int) edge_panel_y + ":" + (int) edge_panel_width + "x" + (int) edge_panel_height);
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

    public MindmapNode getSrcNode() {
        return srcNode;
    }

    public MindmapNode getDestNode() {
        return destNode;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //	大きさ取得
        Dimension wh = this.getSize();

        //色の指定
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.BLUE);

        //線の太さの指定
        BasicStroke wideStroke = new BasicStroke(2.0f);
        g2.setStroke(wideStroke);

        //アンチエイリアシング
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        //矢印を描く
        int xPoints[] = {(int) arrow_head_1_x, (int) arrow_head_2_x, (int) arrow_head_3_x};
        int yPoints[] = {(int) arrow_head_1_y, (int) arrow_head_2_y, (int) arrow_head_3_y};
        g.drawPolygon(xPoints, yPoints, 3);


        //	直線を引く
        //g2.drawLine((int) line_start_X, (int) line_start_Y, (int) line_end_X, (int) line_end_Y);

        // 曲線を描く
        GeneralPath gp = new GeneralPath();
        gp.moveTo(line_start_X, line_start_Y);
        //double x2 = (line_start_X + line_end_X) * 1 / 6;
        double x2 = 0;
        //double x2 = edge_panel_width * 1 / 2;
        //double y2 = (line_start_Y + line_end_Y) * 1 / 6;
        double y2 = edge_panel_height * 1 / 2;
        //double x3 = (line_start_X + line_end_X) * 2 / 6;
        double x3 = edge_panel_width * 1 / 2;
        //double y3 = (line_start_Y + line_end_Y) * 2 / 6;
        //double y3 = 0;
        double y3 = edge_panel_height * 1 / 2;
        gp.curveTo(x2, y2, x3, y3, line_end_X, line_end_Y);
        g2.draw(gp);

        //セルフループを描く
        if (destNode == srcNode) {
            //System.out.println("セルフループ描く");
            g2.drawArc((int)(edge_panel_width / 6), (int)(edge_panel_height / 6), 40, 40, 90, 90 + 270);
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

}
