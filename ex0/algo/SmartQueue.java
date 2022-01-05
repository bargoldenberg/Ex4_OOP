package ex0.algo;

import ex0.CallForElevator;

import java.util.*;

public class SmartQueue {
    private Queue<Integer> UpQueue;
    private Queue<Integer> DownQueue;
    private Queue<Integer> Pointer;
    private int dest;
    private int nextdest;
    /** SmartQueue data structure- consists of two queues and a pointer, UpQueue holds up calls and sorts them accordingly
     * DownQueue holds down calls and sorts them accordingly.
     *Pointer is used as a pointer (obviously) that switches between UpQueue and DownQueue, we will add calls to the pointer.
    */
    public SmartQueue(){
        this.UpQueue = new LinkedList<Integer>();
        this.DownQueue = new LinkedList<Integer>();
        this.Pointer = this.DownQueue;
        this.dest = Integer.MAX_VALUE;
    }

    /**
     * Setters and getters.
     */
    public int getNextdest() {
        return nextdest;
    }

    public void setNextdest(int nextdest) {
        this.nextdest = nextdest;
    }

    public int getDest() {
        if(dest == Integer.MAX_VALUE){
            return 0;
        }
        else{
            return dest;
        }
    }

    public void setDest(int dest) {
        this.dest = dest;
    }

    public Queue<Integer> getUpQueue() {
        return UpQueue;
    }

    public void setUpQueue(Queue<Integer> upQueue) {
        UpQueue = upQueue;
    }

    public Queue<Integer> getDownQueue() {
        return DownQueue;
    }

    public void setDownQueue(Queue<Integer> downQueue) {
        DownQueue = downQueue;
    }

    public Queue<Integer> getPointer() {
        return Pointer;
    }

    public void setPointer(Queue<Integer> pointer) {
        Pointer = pointer;
    }

    /**
     * Adds calls to DownQueue or UpQueue according to call type (UP,DOWN).
     * @param c
     * @param call
     * @return boolean (true if call has been added)
     */
    public boolean add(CallForElevator c,int call){
        int dir=c.getType();
    if(dir==1){
        this.UpQueue.add(call);
        this.SortUP();
        return true;
    }else{
        this.DownQueue.add(call);
        this.SortDown();
        return true;
    }
}

    /**
     * Sorts UpQueue.
     */
    public void SortUP(){
    Object [] arrsort;
   HashSet<Integer> set= new HashSet<Integer>();

    while (!this.UpQueue.isEmpty()){
            set.add(this.UpQueue.remove());
        }


    arrsort=set.toArray();
    Arrays.sort(arrsort);
    this.dest=(Integer) arrsort[arrsort.length-1];
    for(int i=0;i<arrsort.length;i++){
        this.UpQueue.add((Integer) arrsort[i]);
    }

}

    /**
     * Sorts DownQueue.
     */
    public void SortDown(){
        Object [] arrsort;
        HashSet<Object> set= new HashSet<Object>();
        while (!this.DownQueue.isEmpty()){
            set.add(this.DownQueue.remove());
        }


        arrsort=set.toArray();
        Arrays.sort(arrsort);
        this.dest=(Integer) arrsort[0];
        for(int i=arrsort.length-1;i>-1;i--){
            this.DownQueue.add((Integer) arrsort[i]);
        }

    }

    /**
     * Switches Pointer from DownQueue to UpQueue and vice versa.
     */
    public void Switch(){
    if(this.Pointer==this.UpQueue){
        this.Pointer=this.DownQueue;
    }else{
        this.Pointer=this.UpQueue;
    }
    }

}
