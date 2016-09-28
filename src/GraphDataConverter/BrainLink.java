package GraphDataConverter;

import BrainMapper_ver4.elements.GraphEdge;
import BrainMapper_ver4.elements.GraphNode;

/**
 * Created by issey on 2016/09/27.
 */
public class BrainLink extends GraphEdge{

    /**
     * IDを指定してコンストラクト
     *
     * @param elementID
     * @param srcNode
     * @param destNode
     */
    public BrainLink(String elementID, GraphNode srcNode, GraphNode destNode) {
        super(elementID, srcNode, destNode);
    }

    /**
     * IDを指定しないでコンストラクト
     *
     * @param srcNode
     * @param destNode
     */
    public BrainLink(GraphNode srcNode, GraphNode destNode) {
        super(srcNode, destNode);
    }

    /**
     * IDを指定しないでコンストラクト
     *
     * @param srcNode
     * @param destNode
     * @param weight
     */
    public BrainLink(GraphNode srcNode, GraphNode destNode, Double weight) {
        super(srcNode, destNode, weight);
    }
}
