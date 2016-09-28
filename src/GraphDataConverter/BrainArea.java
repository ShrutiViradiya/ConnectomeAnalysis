package GraphDataConverter;

import BrainMapper_ver4.elements.GraphNode;

/**
 * Created by issey on 2016/09/26.
 */
public class BrainArea extends GraphNode{

    int AreaNumber = 0;
    String AreaId = "";
    String AreaLongId = "";
    double MNI_X = 0.0;
    double MNI_Y = 0.0;
    double MNI_Z = 0.0;
    String AreaJpName ="";
    String Description="";

    /**
     * IDを指定してコンストラクト
     *
     * @param elementID
     * @param textarea_value
     */
    public BrainArea(String elementID, String textarea_value) {
        super(elementID, textarea_value);
    }

    /**
     * IDを指定しないでコンストラクト
     *
     * @param text
     */
    public BrainArea(String text) {
        super(text);
    }
}
