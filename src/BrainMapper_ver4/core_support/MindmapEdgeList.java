package BrainMapper_ver4.core_support;


import BrainMapper_ver4.core.MindmapEdge;
import BrainMapper_ver4.core.MindmapNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by issey on 2016/05/01.
 */
public class MindmapEdgeList extends ArrayList<MindmapEdge> {

    /**
     * IDを指定して目的のNodeを取り出す。
     */
    public MindmapEdge getEdgeByID(String ID){
        for(MindmapEdge edge : this){
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
    public MindmapEdge getEdgeBySrcNodeAndDestNode(MindmapNode SrcNode,MindmapNode DestNode){
         for(MindmapEdge edge : this){
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
    public MindmapEdgeList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public MindmapEdgeList() {
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public MindmapEdgeList(Collection<? extends MindmapEdge> c) {
        super(c);
    }
}
