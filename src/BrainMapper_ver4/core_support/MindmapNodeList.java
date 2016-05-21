package BrainMapper_ver4.core_support;


import BrainMapper_ver4.core.MindmapNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by issey on 2016/05/01.
 */
public class MindmapNodeList extends ArrayList<MindmapNode> {

    /**
     * IDを指定して目的のNodeを取り出す。
     */
    public MindmapNode getNodeByID(String ID){
        for(MindmapNode node : this){
            if(node.getElementID().equals(ID)){
               return node;
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
    public MindmapNodeList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public MindmapNodeList() {
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public MindmapNodeList(Collection<? extends MindmapNode> c) {
        super(c);
    }
}
