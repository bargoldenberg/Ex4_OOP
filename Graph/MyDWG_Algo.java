package Graph;
import api.EdgeData;
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.NodeData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import src.ex4_java_client.Agents;
import src.ex4_java_client.Pokemon;
import src.ex4_java_client.Pokemons;

import java.io.*;
import java.util.*;


public class MyDWG_Algo implements DirectedWeightedGraphAlgorithms {
    MyDWG gr;

    /**
     * initiates graph from abstract DirectedWeightedGraph.
     * @param g
     */
    @Override
    public void init(DirectedWeightedGraph g) {
        gr = new MyDWG();
        try {
            Iterator<NodeData> nodeiter = g.nodeIter();
            while(nodeiter.hasNext()){
                NodeData n = nodeiter.next();
                MyNode a = new MyNode();
                a.setLocation(n.getLocation());
                a.setInfo(n.getInfo());
                a.setWeight(n.getWeight());
                a.setTag(n.getTag());
                a.setKey(n.getKey());
                this.gr.addNode(a);
            }
            Iterator<EdgeData> edgeiter = g.edgeIter();
            while(edgeiter.hasNext()){
                EdgeData edge = edgeiter.next();
                this.gr.connectinit(edge);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public DirectedWeightedGraph getGraph() {
        return this.gr;
    }

    /**
     * creates a Deep Copy.
     * @return DirectedWeightedGraph
     */
    @Override
    public DirectedWeightedGraph copy() {
        MyDWG cop = new MyDWG(this.gr);
        return cop;
    }
    /**
     * BFS (Breath First Search) algorithm to traverse a graph.
     */

    public void BFS(DirectedWeightedGraph g, int node, HashMap<Integer, Boolean> visited) throws Exception {
        LinkedList<Integer> queue = new LinkedList<Integer>();
        visited.put(node, true);
        queue.add(node);
        while (!queue.isEmpty()) {
            node = queue.poll();
            Iterator<EdgeData> iter = g.edgeIter(node);
            while (iter.hasNext()) {
                EdgeData n = iter.next();
                if (!visited.get(n.getDest())) {
                    visited.put(n.getDest(), true);
                    queue.add(n.getDest());
                }
            }
        }
    }
    /**
     * This Function takes a vertex runs the BFS algorithm, transposes the graph and then runs a second
     * BFS. if we traverse all vertices in both BFS runs then the graph is Strongly Connected.
     * @return Boolean value representing if the graph is Strongly Connected.
     */
    @Override
    public boolean isConnected() throws Exception {
        Iterator<NodeData> it = this.getGraph().nodeIter();
        NodeData v = it.next();
        HashMap<Integer, Boolean> visited = new HashMap<>();
        Iterator<NodeData> init = this.getGraph().nodeIter();
        while (init.hasNext()) {
            visited.put(init.next().getKey(), false);
        }
        //boolean[] visited = new boolean[this.getGraph().nodeSize()];
        BFS(this.getGraph(), v.getKey(), visited);
        Iterator<NodeData> checkfalse = this.getGraph().nodeIter();
        while (checkfalse.hasNext()) {
            if (!visited.get(checkfalse.next().getKey())) {
                return false;
            }
        }
        Iterator<NodeData> init2 = this.getGraph().nodeIter();
        while (init2.hasNext()) {
            visited.put(init2.next().getKey(), false);
        }
        MyDWG reversedgraph = new MyDWG();
        Iterator<NodeData> nodeiterator = this.getGraph().nodeIter();
        while(nodeiterator.hasNext()){
            MyNode currnode =new MyNode(nodeiterator.next());
            currnode.getEdgeOutList().clear();
            currnode.getEdgeInList().clear();
            reversedgraph.addNode(currnode);
        }
        Iterator<EdgeData> edgeiterator = this.getGraph().edgeIter();
        while (edgeiterator.hasNext()) {
            EdgeData originalEdge = edgeiterator.next();
            MyEdge reversedEdge = new MyEdge(originalEdge.getDest(), originalEdge.getWeight(), originalEdge.getSrc());
           // boolean condition1 = reversedgraph.E.containsValue(originalEdge);
            ArrayList<Integer> key = new ArrayList<Integer>();
            key.add(reversedEdge.getSrc());
            key.add(reversedEdge.getDest());
            //boolean condition2 = reversedgraph.E.containsKey(key);
            if (false) {
                continue;
            } else {
                //reversedgraph.removeEdge(originalEdge.getSrc(), originalEdge.getDest());
                reversedgraph.connect(originalEdge.getDest(), originalEdge.getSrc(), originalEdge.getWeight());
            }
        }
        BFS(reversedgraph, v.getKey(), visited);
        Iterator<NodeData> checkfalse2 = this.getGraph().nodeIter();
        while (checkfalse2.hasNext()) {
            if (!visited.get(checkfalse2.next().getKey())) {
                return false;
            }
        }
        return true;
    }

    /**
     * This Function based on Dijkstra's algorithm (https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm).
     * @param src  - start node
     * @param dest - end (target) node
     * @return the distance (as a Double) of the shortest path between src and dest that exist in the graph.
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        HashMap<Integer, Double> distance = new HashMap<Integer, Double>(); //Map to keep all distance between src to the other nodes (the key is the id).
        HashMap<Integer, Integer> prev = new HashMap<Integer, Integer>(); //Map to keep the previews node before this one in the shortest path from src.
        PriorityQueue<Integer> nodesQueue = new PriorityQueue<Integer>((a,b)-> (int) (distance.get(a)-distance.get(b)));
        // PriorityQueue to keep Next node to check - its comparator is the distance of two nodes.
        List<NodeData> path = new ArrayList<NodeData>(); // ArrayList of the final best path.
        //this loop is for enqueueing src to the queue and initialize src distance value to 0, all other nodes set to infinity.
        for (Map.Entry<Integer, MyNode> node : this.gr.V.entrySet()) {
            if (node.getKey() == src) {
                distance.put(node.getKey(), 0.0);
                nodesQueue.add(node.getKey());
            } else {
                distance.put(node.getKey(), Double.MAX_VALUE);
            }
            prev.put(node.getKey(), null);
        }

        while (!nodesQueue.isEmpty()) {
            int smallest = nodesQueue.poll();
            if (smallest == dest) {  // check for breaking the loop, IF we got to the destination.
                while (prev.get(smallest) != null) {
                    path.add(this.gr.V.get(smallest));
                    smallest = prev.get(smallest);
                }
                path.add(this.gr.V.get(smallest));
            } else if (distance.get(smallest) == Double.MAX_VALUE) { // that means we got to dead end.
                break;
            } else { // for given Node, we will check all his neighbors distance from him (and from them him to the src).
                for (int i = 0; i < this.gr.V.get(smallest).getEdgeOutList().size(); i++) {
                    ArrayList<Integer> tmpKey = new ArrayList<Integer>(2);
                    tmpKey.add(smallest);
                    tmpKey.add(this.gr.V.get(this.gr.V.get(smallest).getEdgeOutList().get(i)).getKey());
                    EdgeData neighborEdge = this.gr.E.get(tmpKey);
                    double dis = distance.get(smallest) + neighborEdge.getWeight();
                    if (dis < distance.get(neighborEdge.getDest())) {
                        distance.put(neighborEdge.getDest(), dis);
                        prev.put(neighborEdge.getDest(), smallest);
                        if (!nodesQueue.contains(neighborEdge.getDest())) {
                            nodesQueue.add(neighborEdge.getDest());
                        }
                    }
                }
            }
        }
        if (distance.get(dest) == Double.MAX_VALUE) {
            return -1;
        } else {
            return distance.get(dest);
        }
    }

    /**
     * This Function based on Dijkstra's algorithm (https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm).
     * @param src  - start node
     * @param dest - end (target) node
     * @return the shortest path (as a List of nodes [NodeData]) between src and dest that exist in the graph.
     */
    @Override
    public List<NodeData> shortestPath(int src, int dest) {
        HashMap<Integer, Double> distance = new HashMap<Integer, Double>();//Map to keep all distance between src to the other nodes (the key is the id).
        HashMap<Integer, Integer> prev = new HashMap<Integer, Integer>();//Map to keep the previews node before this one in the shortest path from src.
        PriorityQueue<Integer> nodesQueue = new PriorityQueue<Integer>((a,b)-> (int) (distance.get(a)-distance.get(b)));
        // PriorityQueue to keep Next node to check - its comparator is the distance of two nodes.
        List<NodeData> path = new ArrayList<NodeData>();// ArrayList of the final best path.
        //this loop is for enqueueing src to the queue and initialize src distance value to 0, all other nodes set to infinity.

        for (Map.Entry<Integer, MyNode> node : this.gr.V.entrySet()) {
            if (node.getKey() == src) {     // check for breaking the loop, IF we got to the destination.
                distance.put(node.getKey(), 0.0);
                nodesQueue.add(node.getKey());
            } else {
                distance.put(node.getKey(), Double.MAX_VALUE);
            }
            prev.put(node.getKey(), null);
        }

        while (!nodesQueue.isEmpty()) {
            int smallest = nodesQueue.poll();
            // check for breaking the loop, IF we got to the destination.
            if (smallest == dest) {
                while (prev.get(smallest) != null) {
                    path.add(this.gr.V.get(smallest));
                    smallest = prev.get(smallest);
                }
                path.add(this.gr.V.get(smallest));
                Collections.reverse(path);

            } else if (distance.get(smallest) == Double.MAX_VALUE) {    // that means we got to dead end.
                break;
            } else {// for given Node, we will check all his neighbors distance from him (and from them him to the src).
                for (int i = 0; i < this.gr.V.get(smallest).getEdgeOutList().size(); i++) {
                    ArrayList<Integer> tmpKey = new ArrayList<Integer>(2);
                    tmpKey.add(smallest);
                    tmpKey.add(this.gr.V.get(smallest).getEdgeOutList().get(i));
                    MyEdge neighborEdge = this.gr.E.get(tmpKey);
                    double dis = distance.get(smallest) + neighborEdge.getWeight();
                    if (dis < distance.get(neighborEdge.getDest())) {
                        distance.put(neighborEdge.getDest(), dis);
                        prev.put(neighborEdge.getDest(), smallest);

                        if (!nodesQueue.contains(neighborEdge.getDest())) {
                            nodesQueue.add(neighborEdge.getDest());
                        }
                    }
                }
            }
        }
        if (path.size() > 0) {
            return path;
        } else {
            return null;
        }

    }


    /**
     * This Function based on Dijkstra's algorithm (https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm).
     * @param src  - start node
     * @return HashMap of all the shortest distances from one src,Basically the full distance list in shortestPathDist but
     * instead of returning a specific element (the dest) it returns the List.
     */
    private HashMap<Integer, Double> shortestPathMap(int src) {
        HashMap<Integer, Double> distance = new HashMap<Integer, Double>();
        HashMap<Integer, Integer> prev = new HashMap<Integer, Integer>();
        PriorityQueue<Integer> nodesQueue = new PriorityQueue<Integer>((a,b)-> (int) (distance.get(a)-distance.get(b)));
        HashSet<Integer> queueset = new HashSet<Integer>();
        double max = Double.MIN_VALUE;
        //List<NodeData> path = new ArrayList<NodeData>();

        for (Map.Entry<Integer, MyNode> node : this.gr.V.entrySet()) {
            if (node.getKey() == src) {
                distance.put(node.getKey(), 0.0);
                nodesQueue.add(node.getKey());
            } else {
                distance.put(node.getKey(), Double.MAX_VALUE);
            }
            prev.put(node.getKey(), null);
        }

        while (!nodesQueue.isEmpty()) {
            int smallest = nodesQueue.poll();
            queueset.remove(smallest);
            if (distance.get(smallest) == Double.MAX_VALUE) {
                break;
            } else {
                for (int i = 0; i < this.gr.V.get(smallest).getEdgeOutList().size(); i++) {
                    ArrayList<Integer> tmpKey = new ArrayList<Integer>(2);
                    tmpKey.add(smallest);
                    tmpKey.add(this.gr.V.get(this.gr.V.get(smallest).getEdgeOutList().get(i)).getKey());
                    EdgeData neighborEdge = this.gr.E.get(tmpKey);
                    double dis = distance.get(smallest) + neighborEdge.getWeight();
                    if (dis < distance.get(neighborEdge.getDest())) {
                        distance.put(neighborEdge.getDest(), dis);
                        prev.put(neighborEdge.getDest(), smallest);

                        if (!queueset.contains(neighborEdge.getDest())) {
                            nodesQueue.add(neighborEdge.getDest());
                            queueset.add(neighborEdge.getDest());
                        }
                    }
                }
            }
        }
        return distance;
    }

    /**
     * center of the graph - the Node whose max distance from any other node is minimal.
     * @return the center node of the graph (NodeData).
     * @throws Exception
     */
    @Override
    public NodeData center() throws Exception {
        Iterator<NodeData> it1 = this.gr.nodeIter(); // Setting iterator to go on all the graph nodes.
        double eccentricity;
        double dist;
        ArrayList<double[]> sumofdistance = new ArrayList<>(); // ArrayList that will contain all the distances.
        while (it1.hasNext()) {
            NodeData a = it1.next();
            //using shortestPathMap to get all the smallest distances from this node.
            HashMap<Integer, Double> distance = shortestPathMap(a.getKey());
            Iterator<NodeData> it2 = this.gr.nodeIter();// Setting second iterator to go on all the graph nodes.
            eccentricity = Double.MIN_VALUE;
            while (it2.hasNext()) {
                NodeData b = it2.next();
                if (a.getKey() == b.getKey()) {  // if both iterators is the same node - do nothing.
                    continue;
                }
                dist = distance.get(b.getKey());
                if (dist > eccentricity) {
                    eccentricity = dist;
                }
            }
            double[] arr = {eccentricity, a.getKey()};
            sumofdistance.add(arr);
        }
        double min = Double.MAX_VALUE;
        int key = Integer.MAX_VALUE;
        for (int i = 0; i < sumofdistance.size(); i++) {
            if (sumofdistance.get(i)[0] < min) {
                min = sumofdistance.get(i)[0];
                key = (int) sumofdistance.get(i)[1];
            }
        }
        if (!this.isConnected()) {
            return null;
        } else {
            return this.getGraph().getNode(key);
        }
    }

    /**
     * TSP (Traveling Salesman Problem),given graph and list of nodes, what is the shortest path that contain them all?
     * Implemented with Upgraded Greedy Algorithm:
     *      - the Greedy Algorithm: take random Node, check what is the closest node in the list and go to him.
     *                              do it until no Nodes left on the list.
     *      - Upgrade: insted of choosing random Node, check All tsp from all nodes in the list, them return the best result.
     * @param cities - List of Nodes in the graph.
     * @return List of a path that passes through all cities,(where List[0] = Strating Node).
     */
    @Override
    public List<NodeData> tsp(List<NodeData> cities) {
//      Implementing with Upgraded Greedy Algorithm. Assuming that all the functions' spouse to work with a million Nodes graph,
//      so with more advanced algorithms it might be problematic.

        List<List<NodeData>> allPaths = new ArrayList<List<NodeData>>(); // List that will contain all tsp paths.
        int index = 0;
        double[] myDist = new double[cities.size()]; // List that will contain all the paths final weight.
        try {
            //  First check if the sub graph is connected
            boolean flag = true;
            for (int i = 1; i < cities.size(); i++) {
                double pathCheck = shortestPathDist(cities.get(0).getKey(), cities.get(i).getKey());
                if (pathCheck <= 0.0) {
                    flag = false;
                }
            }
            for (int i = 1; i < cities.size(); i++) {
                double pathCheck = shortestPathDist(cities.get(i).getKey(), cities.get(0).getKey());
                if (pathCheck <= 0.0) {
                    flag = false;
                }
            }
            if (!flag) { //if the sub graph is not connected (flag = false) return null.
                return null;
            } else {// this is the TSP main function, do it every time for different start node.
                int curr = 0;
                for (int num = 0; num < cities.size(); num++) {
                    curr = num;
                    List<NodeData> rightOrder = new ArrayList<NodeData>(); // List of single path.
                    int tmp = 0, counter = 0;
                    double[] distance = new double[cities.size()];
                    NodeData ptr = cities.get(curr);    ///WE START WITH 0
                    rightOrder.add(ptr);
                    while (counter < cities.size() && rightOrder.size() < cities.size()) {//
                        ptr = cities.get(curr);
                        tmp = curr;
                        for (int i = 0; i < cities.size(); i++) {
                            if (ptr.getKey() != cities.get(i).getKey()) {
                                distance[i] = shortestPathDist(ptr.getKey(), cities.get(i).getKey());
                            } else {
                                distance[i] = Double.MAX_VALUE;
                            }
                            while (curr == tmp) {
                                int smallest = smallestDist(distance); /// smallest is the index of the smallest number in the array.
                                if (!rightOrder.contains(cities.get(smallest))) {
                                    rightOrder.add(cities.get(smallest));
                                    curr = smallest;
                                    counter++;
                                } else if (rightOrder.size() != cities.size()) {
                                    distance[smallest] = Double.MAX_VALUE;
                                } else {
                                    break;
                                }
                            }
                            if (rightOrder.size() == cities.size()) {
                                break;
                            }
                        }
                        if (rightOrder.size() == cities.size()) {
                            allPaths.add(rightOrder);
                        }
                    }
                }
                for (int i = 0; i < allPaths.size(); i++) {
                    myDist[i] = totalWeight(allPaths.get(i));
                }
                index = smallestDist(myDist);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<NodeData> tsppath = new ArrayList<>();
        for (int i = 0; i < allPaths.get(index).size() - 1; i++) {
            ArrayList<NodeData> tmp = (ArrayList) shortestPath(allPaths.get(index).get(i).getKey(), allPaths.get(index).get(i + 1).getKey());
            if (i != allPaths.get(index).size() - 2) {
                tmp.remove(tmp.get(tmp.size() - 1));
            }
            for (int k = 0; k < tmp.size(); k++) {
                tsppath.add(tmp.get(k));
            }
        }
        return tsppath;
    }

    /**
     * Helper for tcp, return the smallest number (index) from array.
     * @param arr - array
     * @return index of the smallest number.
     */
    private int smallestDist(double[] arr) {
        int smallest = 0;
        double check = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < check) {
                check = arr[i];
                smallest = i;
            }
        }
        return smallest;
    }

    /**
     * Helper for tcp, gets a list of nodes and return total the weight (int the order the nodes set).
     * @param cities - list of nodes.
     * @return total the weight (int the order the nodes set).
     */
    private double totalWeight(List<NodeData> cities){
        double pathW = 0.0;
        for(int i=0; i<=cities.size()-2; i++){
            pathW += this.shortestPathDist(cities.get(i).getKey(),cities.get(i+1).getKey());
        }
        return pathW;
    }

    /**
     * simple save using GSON library and the convertor classes (I made them for the save/load).
     * @param file - the file name (may include a relative path).
     * @return boolean - if the save was successful it returns true, else false.
     */
    @Override
    public boolean save(String file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        fromJsonToGraph graphJson = new fromJsonToGraph(this.gr);
        String json = gson.toJson(graphJson);
        try {
            FileWriter fw = new FileWriter("" + file);
            fw.write(json);
            fw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * load function using GSON library and the convertor classes (I made them for the save/load).
     * @param file - file name of JSON file
     * @return boolean - if the load was successful it returns true, else false.
     */
    @Override
    public boolean load(String file) {
        try {
            Gson gson = new Gson();
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ;
            fromJsonToGraph graph = gson.fromJson(bufferedReader, fromJsonToGraph.class);
            MyDWG myGraph = new MyDWG(graph);
            init(new MyDWG(graph));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
    /**
     * Graph generator - with a random values.
     * @param nodes - number of nodes (int).
     * @param seed - number of edges (int).
     */
    public void generateGraph(int nodes,int seed) {
        MyDWG g = new MyDWG();
        Random ra = new Random(seed);
        for (int i = 0; i < nodes; i++) { // Generate all the nodes and adding them to the graph
            Point3D p = new Point3D( ra.nextInt(nodes),  ra.nextInt(nodes),  ra.nextInt(nodes));
            int key = (ra.nextInt(nodes));
            while (g.V.containsKey(key)) {
                key = (ra.nextInt(nodes));
            }
            MyNode n = new MyNode(p, key);
            g.addNode(n);
        }
        for (int i = 0; i < nodes; i++) { // Generate all the edges and generate random connections to the Nodes.
            MyNode a = g.V.get(i);
            if (nodes > 10) {
                for (int j = 0; j < 9; j++) {
                    ArrayList<Integer> key = new ArrayList<>(2);
                    key.add(a.getKey());
                    int id = g.V.get(ra.nextInt(nodes)).getKey();
                    key.add(id);
                    while (g.E.containsKey(key) || a.getKey() == id) {
                        key.remove(1);
                        id = g.V.get(ra.nextInt(nodes)).getKey();
                        key.add(id);
                    }
                    g.connect(a.getKey(), id, ra.nextDouble() * 1000);
                }
            }else{
                for (int j = 0; j < nodes-1; j++) {
                    ArrayList<Integer> key = new ArrayList<>(2);
                    key.add(a.getKey());
                    int id = g.V.get(ra.nextInt(nodes)).getKey();
                    key.add(id);
                    while (g.E.containsKey(key) || a.getKey() == id) {
                        key.remove(1);
                        id = g.V.get(ra.nextInt(nodes)).getKey();
                        key.add(id);
                    }
                    g.connect(a.getKey(), id, ra.nextDouble() * 1000);
                }
        }

        }
        this.gr = g;
    }

    public int getKeyOfPosition(Point3D point){
        int key = -1;
        for (Map.Entry<Integer, MyNode> node : this.gr.V.entrySet()) {
            if(node.getValue().getLocation().y() == point.y() && node.getValue().getLocation().x() == point.x() ){
                key = node.getKey();
                break;
            }
        }
        return key;
    }

    public void loadjsonstring(String graphstr){
        Gson gson = new Gson();
        fromJsonToGraph graph = gson.fromJson(graphstr, fromJsonToGraph.class);
        MyDWG gr = new MyDWG(graph);
        this.init(gr);
    }

    public EdgeData findEdge(Pokemon p){
        double EPS = 0.000001;
        try {
            Iterator<EdgeData> it= this.getGraph().edgeIter();
            while(it.hasNext()){
                EdgeData e = it.next();
                double dist1 = this.getGraph().getNode(e.getSrc()).getLocation().distance(this.getGraph().getNode(e.getDest()).getLocation());
                double dist2 =       (this.getGraph().getNode(e.getSrc()).getLocation().distance(p.getPosition())+
                        p.getPosition().distance(this.getGraph().getNode(e.getDest()).getLocation()));
                double delta = Math.abs(dist1-dist2);
                boolean onedge = delta<EPS;
                if(onedge){
                    if((this.getGraph().getNode(e.getSrc()).getLocation().y()>this.getGraph().getNode(e.getDest()).getLocation().y())&&p.getType()==1){
                        return this.getGraph().getEdge(e.getDest(),e.getSrc());
                    }else if((this.getGraph().getNode(e.getSrc()).getLocation().y()<this.getGraph().getNode(e.getDest()).getLocation().y())&&p.getType()==-1){
                        return this.getGraph().getEdge(e.getDest(),e.getSrc());
                    }else {
                        return e;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sets amount of pokemons on an edge, using tag.
     * @param p
     */
    public void tagPokemonsOnEdges(Pokemons p){
        /**
         * For Tag all the edges with Pokemons on them.
         */
        for(int i=0; i<p.GetPokeList().size();i++){
            MyEdge e = (MyEdge) findEdge(p.GetPokeList().get(i));
            e.setTag(e.getTag()+1);
        }
    }

    private int totalPokemons(List<NodeData> nodes){
        int Tags = 0;
        for(int i=0; i<=nodes.size()-2; i++){
            Tags += this.getGraph().getEdge(nodes.get(i).getKey(),nodes.get(i+1).getKey()).getTag();
        }
        return Tags;
    }

    private int minimumCost(int[] tag,double []weight){
        int biggestTag = 0;
        int bignum=-1;
        ArrayList<Integer> list = new ArrayList<Integer>();
        for(int i=0;i<tag.length;i++){
            if(tag[i]>bignum){
                biggestTag=i;
                bignum=tag[i];
            }
        }
        for(int i=0; i<tag.length;i++){
            if(tag[i] >= tag[biggestTag]){
                list.add(i);
            }
        }
        if(list.size() == 0){
            return -1;
        }
        else if(list.size() == 1){
            return biggestTag;
        }
        else{
            double smallestDist = Integer.MAX_VALUE;
            int indexOfSmallest = -1;
            for(int i=0;i< list.size();i++){
                if(weight[list.get(i)] < smallestDist){
                    smallestDist = weight[list.get(i)];
                    indexOfSmallest = list.get(i);
                }
            }
            return list.get(indexOfSmallest);
        }
    }

    public ArrayList<Integer> nextPos(Pokemons p, Agents a){
        tagPokemonsOnEdges(p); // assuming that we will get each time more pokemons - we tag their edges.
        double[] dist = new double[p.GetPokeList().size()];
        int [] tagCaught = new int[p.GetPokeList().size()];
        int source = -1,dest = -1;
        for(int i=0; i<p.GetPokeList().size();i++){
            MyEdge e = (MyEdge) findEdge(p.GetPokeList().get(i));
//            if(p.GetPokeList().get(i).getType() == 1){
//                source = e.getSrc();
//                dest = e.getDest();
//            }
//            if(p.GetPokeList().get(i).getType() == -1){
//                dest = e.getSrc();
//                source = e.getDest();
//            }
            source = e.getSrc();
            dest =e.getDest();
            double pathDst = shortestPathDist(getKeyOfPosition(a.GetAgentList().get(0).getPos()),source);
            dist[i] = pathDst+e.getWeight();
            ArrayList<NodeData> path = (ArrayList<NodeData>) shortestPath(getKeyOfPosition(a.GetAgentList().get(0).getPos()),source);
            path.add(gr.V.get(dest));
            int tagCount = totalPokemons(path);
            tagCaught[i] = tagCount;
        }

        int result = minimumCost(tagCaught,dist);
        ArrayList<Integer> ans = new ArrayList<Integer>();
        ans.add(0);
        ans.add(findEdge(p.GetPokeList().get(result)).getSrc());
        ans.add(findEdge(p.GetPokeList().get(result)).getDest());
        return ans;
    }
}
