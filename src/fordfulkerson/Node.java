/*
 * Node.java
 *
 * Created on 26 October 2005, 12:23
 */

import java.util.ArrayList;

/**
 *
 * @author Mad_Fool
 */
public class Node {
    
    private ArrayList<Edge> outgoingEdges;
    private ArrayList<Edge> incomingEdges;
    
    boolean visted;
    
    /** Creates a new instance of Node */
    public Node()
    {
        outgoingEdges = new ArrayList<Edge>();
        incomingEdges = new ArrayList<Edge>();
        visted = false;
    }
    
    public void addOutgoingEdge(Edge outgoingEdge)
    {
        outgoingEdges.add(outgoingEdge);
    }
    
    public void addIncomingEdge(Edge incomingEdge)
    {
        incomingEdges.add(incomingEdge);
    }
    
    public ArrayList<Edge> getOutgoingEdges()
    {
        return outgoingEdges;
    }
    
    public ArrayList<Edge> getIncomingEdges()
    {
        return incomingEdges;
    }
}
