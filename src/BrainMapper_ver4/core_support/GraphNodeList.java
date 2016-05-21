package BrainMapper_ver4.core_support;


import BrainMapper_ver4.core.GraphNode;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by issey on 2016/05/01.
 */
public class GraphNodeList extends ArrayList<GraphNode> {

    /**
     * IDを指定して目的のNodeを取り出す。
     */
    public GraphNode getNodeByID(String ID){
        for(GraphNode node : this){
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
    public GraphNodeList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public GraphNodeList() {
    }

    /**
     * Constructs a list containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this list
     * @throws NullPointerException if the specified collection is null
     */
    public GraphNodeList(Collection<? extends GraphNode> c) {
        super(c);
    }
}
