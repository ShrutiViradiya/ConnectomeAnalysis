package BrainMapper_ver4.core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by issey on 2016/02/14.
 */
public class GraphFieldScrollPane extends JScrollPane {

    //フィールド
    protected ImageIcon imageicon;
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


    public GraphFieldScrollPane() {
        super();

        //getViewport().add(new GraphField());
        //getViewport().setView(new GraphField());
        //setViewportView(new GraphField());
        //getViewport().setViewPosition(new Point(2000, 1200));

        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        setPreferredSize(new Dimension(100, 60));
        //this.setPreferredSize(new Dimension((int) ViewPortW, (int) ViewPortH));

        addMouseListener(new MaouseActionHandler());
        addMouseMotionListener(new MaouseActionHandler());

    }


    //InnerClass
    private class MaouseActionHandler implements MouseListener, MouseMotionListener {

        protected double nextViewPortX = 0;
        protected double nextViewPortY = 0;

        public void mousePressed(java.awt.event.MouseEvent event) {
            // System.out.println("mousePressed");
            Point p = getViewport().getViewPosition();
            PressedTimingViewPortX = p.x;/*PressedTimingViewPortX = getViewport().getX();ではダメなようだ*/
            PressedTimingViewPortY = p.y;/*PressedTimingViewPortY = getViewport().getY();ではダメなようだ*/
            //System.out.println("  (PressedTimingViewPortX, PressedTimingViewPortY) = (" + PressedTimingViewPortX + ", " + PressedTimingViewPortY + ")");

            startX = event.getXOnScreen();
            startY = event.getYOnScreen();
            //System.out.println("  (startX, startY) = (" + startX + ", " + startY + ")");
        }

        public void mouseDragged(java.awt.event.MouseEvent event) {
            //System.out.println("mouseDragged");
            Point p = getViewport().getViewPosition();
            DraggedTimingViewPortX = p.x;/*DraggedTimingViewPortX = getViewport().getX();ではダメなようだ*/
            DraggedTimingViewPortY = p.y;/*DraggedTimingViewPortY = getViewport().getY();ではダメなようだ*/
            //System.out.println("  (DraggedTimingViewPortX, DraggedTimingViewPortY) = (" + DraggedTimingViewPortX + ", " + DraggedTimingViewPortY + ")");

            endX = event.getXOnScreen();
            endY = event.getYOnScreen();
            //System.out.println("  (endX, endY) = (" + endX + ", " + endY + ")");
            dX = endX - startX;
            dY = endY - startY;

            nextViewPortX = PressedTimingViewPortX - dX;
            nextViewPortY = PressedTimingViewPortY - dY;
            //System.out.println("  (nextViewPortX, nextViewPortY) = (" + nextViewPortX + ", " + nextViewPortY + ")");

            getViewport().setViewPosition(new Point((int) nextViewPortX, (int) nextViewPortY));

        }

        public void mouseReleased(java.awt.event.MouseEvent event) {
            //System.out.println("mouseReleased");
            Point p = getViewport().getViewPosition();
            ReleasedTimingViewPortX = p.x;/*ReleasedTimingViewPortX = getViewport().getX();ではダメなようだ*/
            ReleasedTimingViewPortY = p.y;/*ReleasedTimingViewPortY = getViewport().getY();ではダメなようだ*/
            //System.out.println("  (ReleasedTimingViewPortX, ReleasedTimingViewPortY) = (" + ReleasedTimingViewPortX + ", " + ReleasedTimingViewPortY + ")");
            repaint();
        }

        public void mouseClicked(java.awt.event.MouseEvent event) {
            //System.out.println("mouseClicked");
            Point p = getViewport().getViewPosition();
            ClickedTimingViewPortX = p.x;/*ReleasedTimingViewPortX = getViewport().getX();ではダメなようだ*/
            ClickedTimingViewPortY = p.y;/*ReleasedTimingViewPortY = getViewport().getY();ではダメなようだ*/
            //System.out.println("  (ClickedTimingViewPortX, ClickedTimingViewPortY) = (" + ClickedTimingViewPortX + ", " + ClickedTimingViewPortY + ")");


            if (event.getClickCount() == 2) {
                //System.out.println("nextViewPortX, nextViewPortY = (" + (int) nextViewPortX + ", " + (int) nextViewPortY + ")");
                //System.out.println("getViewport().getX(), getViewport().getY() = (" + getViewport().getX() + ", " + getViewport().getY() + ")");

            }
        }

        public void mouseMoved(java.awt.event.MouseEvent e) {
        }

        public void mouseEntered(java.awt.event.MouseEvent e) {
        }

        public void mouseExited(java.awt.event.MouseEvent e) {
        }
    }

}
