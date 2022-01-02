package Graph;

import java.util.ArrayList;
import java.util.Map;

public class fromJsonToGraph {
    ArrayList<edgeConverter> Edges;
    ArrayList<nodeConverter> Nodes;

    public fromJsonToGraph(MyDWG g){
        this.Edges = new ArrayList<edgeConverter>();
        this.Nodes = new ArrayList<nodeConverter>();
        for(Map.Entry<Integer, MyNode> node: g.V.entrySet()){
            nodeConverter n = new nodeConverter(node.getValue());
            this.Nodes.add(n);
        }
        for(Map.Entry<ArrayList<Integer>, MyEdge> edge: g.E.entrySet()){
            edgeConverter e = new edgeConverter(edge.getValue());
            this.Edges.add(e);
        }
    }

    public String toString(){
        String str = "Node:";
        for(int i=0;i<Nodes.size();i++){
            str += Nodes.get(i).toString();
        }
        str += "Edge:";
        for(int i=0; i<Edges.size();i++){
            str += Edges.get(i).toString();
        }
        return str;
    }
}
