package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;

import java.util.Queue;

public class MyAlgo implements ElevatorAlgo {
    public static final int UP = 1, DOWN = -1, LEVEL = 0;
    private int _direction;
    private Building _building;
    public static SmartQueue[] Elevators;
    private boolean[] _firstTime;

    public MyAlgo(Building b) {
        this._building = b;
        this._direction = LEVEL;
        this.Elevators = new SmartQueue[_building.numberOfElevetors()];
        this._firstTime = new boolean[_building.numberOfElevetors()];
        for (int i = 0; i < _firstTime.length; i++) {
            _firstTime[i] = true;
        }
        for (int i = 0; i < Elevators.length; i++) {
            Elevators[i] = new SmartQueue();
        }
    }


    @Override
    public Building getBuilding() {
        return _building;
    }

    @Override
    public String algoName() {
        return "ElevAlgo_1";
    }

    /**
     * This method is the main optimal allocation (aka load-balancing) algorithm for allocating the
     * "best" elevator for a call (over all the elevators in the building).
     *
     * @param c the call for elevator (src, dest)
     * @return the index of the elevator to which this call was allocated to.
     */
    @Override
    public int allocateAnElevator(CallForElevator c) {
        int ans = FindFastestElevator(c, c.getSrc());
        if (ans == Integer.MAX_VALUE) {
            return FindFastestElevatorOppo(c, c.getSrc());
        } else {
            return ans;
        }
    }

    /**
     * This method calculates the time it takes an elevator travelling in the same direction as the call, to arrive
     * at the call source
     *
     * @param i
     * @param src
     * @return
     */

    private double timeToSrc(int i, int src) {
        double ans = 0;
        int currpos = _building.getElevetor(i).getPos();
        double speed = _building.getElevetor(i).getSpeed();
        double diff = Math.abs(src - currpos);
        double t1 = (diff / speed);
        int relevantsize = Elevators[i].getPointer().size();
        double doors = (_building.getElevetor(i).getTimeForOpen() + _building.getElevetor(i).getTimeForClose()) * (relevantsize);
        double StartTime = (_building.getElevetor(i).getStartTime())* (relevantsize) ;
        double StopTime = (_building.getElevetor(i).getStopTime())* (relevantsize) ;
        ans = t1 + StartTime + StopTime + doors + 1 / speed;
        ;

        return ans;
    }

    /**
     * This method calculates the time it takes an elevator travelling in the opposite direction as the call, to arrive
     * at the call source.
     *
     * @param i
     * @param src
     * @return
     */
    private double timeToSrcForOppo(int i, int src) {
        double ans = 0;
        int dest = Elevators[i].getDest();
        double speed = _building.getElevetor(i).getSpeed();
        double t1 = (Math.abs(src - dest) / speed);
        int relevantsize = Elevators[i].getPointer().size();
        double doors = (_building.getElevetor(i).getTimeForOpen() + _building.getElevetor(i).getTimeForClose())* (relevantsize);
        double StartTime = (_building.getElevetor(i).getStartTime())* (relevantsize) ;
        double StopTime = (_building.getElevetor(i).getStopTime())* (relevantsize) ;
        ans = timeToSrc(i, src) + t1 + StartTime + StopTime + doors  + 1 / speed;
        return ans;
    }

    /**
     * Finds the fastest elevator to reach the call source that is going in the opposite direction.
     *
     * @param c
     * @param src
     * @return
     */
    private int FindFastestElevatorOppo(CallForElevator c, int src) {
        int ChosenElev = 0;
        int numOfElev = _building.numberOfElevetors();
        double min = Double.MAX_VALUE;
        for (int i = 0; i < numOfElev; i++) {
            if (min > (timeToSrcForOppo(i, src))) {
                min = timeToSrcForOppo(i, src);
                ChosenElev = i;
            }
        }
        if (ChosenElev != Integer.MAX_VALUE) {
            if (Elevators[ChosenElev].getPointer().isEmpty()) Elevators[ChosenElev].Switch();
            Elevators[ChosenElev].add(c, c.getSrc());
            Elevators[ChosenElev].add(c, c.getDest());

        }
        return ChosenElev;
    }

    /**
     * This method is the low level command for each elevator in terms of the elevator API: GoTo, Stop,
     * The simulator calls the method every time stamp (dt), note: in most cases NO action is needed.
     *
     * @param elev the current Elevator index on which the operation is performs.
     */
    @Override
    public void cmdElevator(int elev) {
        if (Elevators[elev].getPointer().isEmpty()) {
            Elevators[elev].Switch();
        }
        if (_firstTime[elev]) {
            if (elev > _firstTime.length / 2) {
                Elevators[elev].setNextdest(0);
                _building.getElevetor(elev).goTo(0);
                _firstTime[elev] = false;
            }
        }
        /**
         if there are queued calls and the elevator is level send the elevator to the call.
         */
        if (!Elevators[elev].getPointer().isEmpty() && _building.getElevetor(elev).getState() == 0) {
            Elevators[elev].setNextdest(Elevators[elev].getPointer().peek());
            _building.getElevetor(elev).goTo(Elevators[elev].getPointer().remove());
            /**
             * else if the elevator is not level and we can stop the elevator to pick up calls on the way, pick them up.
             */
        } else if (!Elevators[elev].getPointer().isEmpty() && Elevators[elev].getPointer().equals(Elevators[elev].getUpQueue()) && _building.getElevetor(elev).getState() != 0  && _building.getElevetor(elev).getPos() < Elevators[elev].getPointer().peek() && Elevators[elev].getPointer().peek() < Elevators[elev].getNextdest()) {
            int nextdest = Elevators[elev].getNextdest();
            Elevators[elev].setNextdest(Elevators[elev].getPointer().peek());
            _building.getElevetor(elev).stop(Elevators[elev].getPointer().remove());
            Elevators[elev].getPointer().add(nextdest);
            Elevators[elev].SortUP();

        } else if (!Elevators[elev].getPointer().isEmpty() && Elevators[elev].getPointer().equals(Elevators[elev].getDownQueue()) && _building.getElevetor(elev).getState() != 0  && _building.getElevetor(elev).getPos() > Elevators[elev].getPointer().peek() && Elevators[elev].getPointer().peek() > Elevators[elev].getNextdest()) {
            int nextdest = Elevators[elev].getNextdest();
            Elevators[elev].setNextdest(Elevators[elev].getPointer().peek());
            _building.getElevetor(elev).stop(Elevators[elev].getPointer().remove());
            Elevators[elev].getPointer().add(nextdest);
            Elevators[elev].SortDown();
        }
            if (Elevators[elev].getPointer().isEmpty()) {
                Elevators[elev].Switch();

    }

}

        /**
         * Finds the fastest elevator to reach the call source that is going in the call direction.
         *
         * @param c
         * @param src
         * @return
         */
        private int FindFastestElevator (CallForElevator c,int src){
            int ChosenElev = Integer.MAX_VALUE;
            int numOfElev = _building.numberOfElevetors();
            double min = Double.MAX_VALUE;
            for (int i = 0; i < numOfElev; i++) {
                if (min > timeToSrc(i, src) && CorrectState(src, _building.getElevetor(i))) {
                    min = timeToSrc(i, src);
                    ChosenElev = i;
                }
            }
            if (ChosenElev != Integer.MAX_VALUE) {

                Elevators[ChosenElev].add(c, c.getSrc());
                Elevators[ChosenElev].add(c, c.getDest());
                if (Elevators[ChosenElev].getPointer().isEmpty()) Elevators[ChosenElev].Switch();
            }
            return ChosenElev;
        }

        /**
         * checks if elevator can pick up the call on its path.
         *
         * @param src
         * @param e
         * @return
         */
        private boolean CorrectState ( int src, Elevator e){
            return (e.getState() == UP && (src > e.getPos())) || (e.getState() == DOWN && (src < e.getPos())) || e.getState() == LEVEL;
        }


    }


