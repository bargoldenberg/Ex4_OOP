package Graph;

public class nodeConverter {
    private String pos;
    private int id;

    public nodeConverter(MyNode n){
        this.pos = n.getLocation().x() + "," +n.getLocation().y() + "," +n.getLocation().z();
        this.id =n.getKey();
    }

    public nodeConverter(String pos, int id) {
        this.pos = pos;
        this.id = id;
    }

    public double getX(){
        double ans = 0.0;
        String str = this.pos.substring(0,this.pos.indexOf(","));
        return Double.parseDouble(str);
    }
    public double getY(){
        double ans = 0.0;
        int index1 = this.pos.indexOf(",");
        int index2 = index1 +1;
        while(this.pos.charAt(index2) != ','){
            index2++;
        }
        String str = this.pos.substring(index1+1,index2);
        return Double.parseDouble(str);
    }
    public double getZ(){
        double ans = 0.0;
        return ans;
    }
    public Point3D getLocation(){
        return new Point3D(getX(),getY(),getZ());
    }
    public MyNode getNode(){
        return new MyNode(getLocation(), id);
    }
    public int getID(){
        return this.id;
    }
    public String toString(){
        return "id:"+id+"["+pos+"]";
    }
}
