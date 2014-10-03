/*
 * Edge.java
 *
 * Created on 26 October 2005, 12:22
 */

/**
 *
 * @author Mad_Fool
 */
public class Edge
{   
    public Node backwardNode;
    public Node forwardNode;
    public int flow;
    public int capacity;
    boolean isForwardEdge;
    /** Creates a new instance of Edge */
    public Edge(Node newBackwardNode, Node newForwardNode, int newCapacity)
    {
        backwardNode = newBackwardNode;
        forwardNode = newForwardNode;
        capacity = newCapacity;
        flow = 0;
        isForwardEdge = true;
    }
}
