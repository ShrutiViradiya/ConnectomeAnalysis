package BrainMapper_ver4.core_support;

import BrainMapper_ver4.core.MindmapNote;

import javax.swing.*;
import java.util.Calendar;

/**
 * Created by issey on 2016/05/21.
 */
public class GraphElement extends JPanel {
    public MindmapNote mmNote;
    public String ElementID = "";
    public int positionX;
    public int positionY;
    public int centerX;
    public int centerY;
    public int Width;
    public int Height;

    public MindmapNote getMmNote() {
        return mmNote;
    }

    public void setBelongingMindmapNote(MindmapNote mmNote) {
        this.mmNote = mmNote;
    }

    public String getElementID() {
        return ElementID;
    }

    public void setElementID(String elementID) {
        ElementID = elementID;
    }

    public static String makeElementID() {
        Calendar cal = Calendar.getInstance();
        return String.format("%04d", cal.get(Calendar.YEAR)) + String.format("%02d", cal.get(Calendar.MONTH) + 1) + String.format("%02d", cal.get(Calendar.DATE)) + "_" +
                String.format("%02d", cal.get(Calendar.HOUR)) + "_" + String.format("%02d", cal.get(Calendar.MINUTE)) + "_" + String.format("%02d", cal.get(Calendar.SECOND)) + "_" + String.format("%03d", cal.get(Calendar.MILLISECOND));
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

}
