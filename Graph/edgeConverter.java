package Graph;

public class edgeConverter {
    private int src;
    private double w;
    private int dest;

    public edgeConverter(int s, double wight, int d){
        this.src = s;
        this.w = wight;
        this.dest = d;
    }

    public edgeConverter(MyEdge e){
        this.src = e.getSrc();
        this.w = e.getWeight();
        this.dest = e.getDest();
    }
    public int getSrc(){
        return this.src;
    }
    public double getW(){
        return this.w;
    }

    public int getDest() {
        return this.dest;
    }

    public String toString(){
        return "{src:"+src+",dest:"+dest+",wight:"+w+"}";
    }

}
