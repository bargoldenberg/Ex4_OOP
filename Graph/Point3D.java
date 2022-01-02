package Graph;

import api.GeoLocation;

public class Point3D implements GeoLocation {

    private double x;
    private double y;
    private double z;

    public Point3D(){
        this.x=0;
        this.y=0;
        this.z=0;
    }
    public Point3D(double x,double y,double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(GeoLocation g) {
        double dx = g.x()-this.x();
        double dy = g.y()-this.y();
        double dz = g.z()-this.z();
        return Math.sqrt(dx*dx+dy*dy+dz*dz);
    }

    public String toString() {
        return "("+"x: "+this.x+", "+"y: "+
                this.y+", "+"z: "+this.z+")";
    }
}
