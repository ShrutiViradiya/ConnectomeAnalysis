package BrainMapper_ver4.core_support;


import BrainMapper_ver4.core.GraphEdge;
import BrainMapper_ver4.core.GraphNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by issey on 2016/05/01.
 */
public class GraphEdgeList extends ArrayList<GraphEdge> {

    /**
     * IDを指定して目的のNodeを取り出す。
     */
    public GraphEdge getEdgeByID(String ID){
        for(GraphEdge edge : this){
            if(edge.getElementID().equals(ID)){
               return edge;
            }
        }
        return null;
    }

    /**
     * 起点と終点を指定してEdgeを特定するメソッド。
     * 新たなEdgeを追加する際にEdgeの重複を避ける為に作成。
     * @param SrcNode
     * @param DestNode
     * @return
     */
    public GraphEdge getEdgeBySrcNodeAndDestNode(GraphNode SrcNode, GraphNode DestNode){
         for(GraphEdge edge : this){
            if(edge.getSrcNode() == SrcNode && edge.getDestNode() == DestNode){
               return edge;
            }
        }
        return null;

    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     * @throws IllegalArgumentException if the specified initial capacity
     *                                  is negative
     */
    public GraphEdgeList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public GraphEdgeList() {
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public GraphEdgeList(Collection<? extends GraphEdge> c) {
        super(c);
    }
}
