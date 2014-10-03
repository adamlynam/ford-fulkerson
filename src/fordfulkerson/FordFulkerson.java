/*
 * FordFulkerson.java
 *
 * Created on 26 October 2005, 11:12
 */

import java.util.*;
import java.io.*;

/**
 *
 * @author Mad_Fool
 */
public class FordFulkerson
{
    Map<String, Node> nodes;
    ArrayList<Edge> edges;
    
    /** Creates a new instance of Main */
    public FordFulkerson()
    {
        nodes = new HashMap<String, Node>();
        edges = new ArrayList<Edge>();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        FordFulkerson graph = new FordFulkerson();
        /*
        graph.nodes.put("SOURCE", new Node());
        graph.nodes.put("n1", new Node());
        graph.nodes.put("n2", new Node());
        graph.nodes.put("TARGET", new Node());
        
        Edge edgeOne = new Edge(graph.nodes.get("SOURCE"), graph.nodes.get("n1"), 50);
        graph.edges.add(edgeOne);
        graph.nodes.get("SOURCE").addOutgoingEdge(edgeOne);
        graph.nodes.get("n1").addIncomingEdge(edgeOne);
        
        Edge edgeTwo = new Edge(graph.nodes.get("SOURCE"), graph.nodes.get("n2"), 50);
        graph.edges.add(edgeTwo);
        graph.nodes.get("SOURCE").addOutgoingEdge(edgeTwo);
        graph.nodes.get("n2").addIncomingEdge(edgeTwo);
        
        Edge edgeThree = new Edge(graph.nodes.get("n1"), graph.nodes.get("n2"), 1);
        graph.edges.add(edgeThree);
        graph.nodes.get("n1").addOutgoingEdge(edgeThree);
        graph.nodes.get("n2").addIncomingEdge(edgeThree);
        
        Edge edgeFour = new Edge(graph.nodes.get("n1"), graph.nodes.get("TARGET"), 50);
        graph.edges.add(edgeFour);
        graph.nodes.get("n1").addOutgoingEdge(edgeFour);
        graph.nodes.get("TARGET").addIncomingEdge(edgeFour);
        
        Edge edgeFive = new Edge(graph.nodes.get("n2"), graph.nodes.get("TARGET"), 50);
        graph.edges.add(edgeFive);
        graph.nodes.get("n2").addOutgoingEdge(edgeFive);
        graph.nodes.get("TARGET").addIncomingEdge(edgeFive);
        */
        //
        BufferedReader input = null;
        
        try
        {
            input = new BufferedReader(new FileReader("simple100000.txt"));
        }
        catch (IOException e)
        {
            System.out.println("File could not be opened correctly, exiting");
            System.exit(1);
        }
        
        String fileLine = null;
        try
        {
            fileLine = input.readLine();
        }
        catch (IOException e)
        {
            System.out.println("File read error, exiting");
            System.exit(1);
        }
        
        while(fileLine != null)
        {
            String[] edgeInformation = fileLine.split(" ");
            
            //DEBUG System.out.println(edgeInformation[0] + ":" + edgeInformation[1] + ":" + edgeInformation[2]);
            
            //if the first node in this edge doesnt exist, create it
            if (!graph.nodes.containsKey(edgeInformation[0]))
            {
                graph.nodes.put(edgeInformation[0], new Node());
            }
            //if the second node in this edge doesnt exist, create it
            if (!graph.nodes.containsKey(edgeInformation[1]))
            {
                graph.nodes.put(edgeInformation[1], new Node());
            }
            Edge newEdge = new Edge(graph.nodes.get(edgeInformation[0]), graph.nodes.get(edgeInformation[1]), Integer.parseInt(edgeInformation[2]));
            graph.edges.add(newEdge);
            graph.nodes.get(edgeInformation[0]).addOutgoingEdge(newEdge);
            graph.nodes.get(edgeInformation[1]).addIncomingEdge(newEdge);
            
            try
            {
                fileLine = input.readLine();
            }
            catch (IOException e)
            {
                System.out.println("File read error, exiting");
                System.exit(1);
            }
        }
        //
        
        graph.resetNodes();
        ArrayList<Edge> edgesInCurrentPath = graph.findAugmentingPath();
        while (edgesInCurrentPath != null)
        {            
            int residualCapacity = Integer.MAX_VALUE;
            Iterator edgeList = edgesInCurrentPath.iterator();
            while (edgeList.hasNext())
            {
                Edge currentEdge = (Edge) edgeList.next();
                if (currentEdge.isForwardEdge && currentEdge.capacity - currentEdge.flow < residualCapacity)
                {
                    residualCapacity = currentEdge.capacity - currentEdge.flow;
                }
                else if (!currentEdge.isForwardEdge && currentEdge.flow < residualCapacity)
                {
                    residualCapacity = currentEdge.flow;
                }
            }
            
            edgeList = edgesInCurrentPath.iterator();
            while (edgeList.hasNext())
            {
                Edge currentEdge = (Edge) edgeList.next();
                
                if (currentEdge.isForwardEdge)
                {
                    currentEdge.flow += residualCapacity;
                }
                else
                {
                    currentEdge.flow -= residualCapacity;
                }
            }
            
            graph.resetNodes();
            edgesInCurrentPath = graph.findAugmentingPath();
        }
        
        //printEdges(graph.edges);
        graph.calculateMaxFlow();
    }
    
    public ArrayList<Edge> findAugmentingPath()
    {
        return depthFirstSearch(nodes.get("SOURCE"));
    }
    
    public ArrayList<Edge> depthFirstSearch(Node currentNode)
    {
        if (!currentNode.visted)
        {
            Iterator edgeList = currentNode.getOutgoingEdges().iterator();
            while (edgeList.hasNext())
            {
                Edge currentEdge = (Edge)edgeList.next();

                if (currentEdge.capacity > currentEdge.flow)
                {
                    if (currentEdge.forwardNode == nodes.get("TARGET"))
                    {
                        ArrayList<Edge> augmentedPath = new ArrayList<Edge>();
                        currentEdge.isForwardEdge = true;
                        augmentedPath.add(currentEdge);
                        //DEBUG System.out.println("path found!!");
                        return augmentedPath;
                    }
                    else
                    {
                        currentNode.visted = true;
                        ArrayList<Edge> augmentedPath = depthFirstSearch(currentEdge.forwardNode);
                        currentNode.visted = false;
                        if (augmentedPath != null)
                        {
                            currentEdge.isForwardEdge = true;
                            augmentedPath.add(currentEdge);
                            return augmentedPath;
                        }
                    }
                }
            }

            edgeList = currentNode.getIncomingEdges().iterator();
            while (edgeList.hasNext())
            {
                Edge currentEdge = (Edge)edgeList.next();

                if (currentEdge.flow > 0)
                {
                    currentNode.visted = true;
                    ArrayList<Edge> augmentedPath = depthFirstSearch(currentEdge.backwardNode);
                    currentNode.visted = false;
                    if (augmentedPath != null)
                    {
                        currentEdge.isForwardEdge = false;
                        augmentedPath.add(currentEdge);
                        return augmentedPath;
                    }
                }
            }
        }
        return null;
    }
    
    public void resetNodes()
    {
        Iterator nodeList = nodes.values().iterator();
        
        while (nodeList.hasNext())
        {
            Node currentNode = (Node) nodeList.next();
        
            currentNode.visted = false;
        }
    }
    
    public void calculateMaxFlow()
    {
        Iterator edgeList = nodes.get("SOURCE").getOutgoingEdges().iterator();
        
        int totalFlow = 0;
        while(edgeList.hasNext())
        {
            Edge currentEdge = (Edge)edgeList.next();
            
            totalFlow += currentEdge.flow;
        }
        
        System.out.println("The maximum flow is : " + totalFlow);
    }
    
    public static void printEdges(ArrayList<Edge> toPrint)
    {
        Iterator edgeList = toPrint.iterator();
        while(edgeList.hasNext())
        {
            Edge currentEdge = (Edge)edgeList.next();
            
            System.out.println(currentEdge.backwardNode + " to " + currentEdge.forwardNode + " with a value of " + currentEdge.flow);            
        }
    }
}
