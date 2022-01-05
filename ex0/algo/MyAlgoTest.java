package ex0.algo;


import ex0.Building;
import ex0.simulator.Call_A;
import ex0.simulator.Simulator_A;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class algo1Test {
    Building b1;
    Building b9;
    MyAlgo algoB1;
    MyAlgo algoB9;

    public algo1Test(){
        Simulator_A.initData(1,null);
        b1 = Simulator_A.getBuilding();
        Simulator_A.initData(9,null);
        b9 = Simulator_A.getBuilding();

        algoB1 = new MyAlgo(b1);
        algoB9 =new MyAlgo(b9);
    }

    @Test
    void allocateAnElevator() {
        /**
         * Check if call is allocated to SmartQueue.
         */
        algo1Test a1 = new algo1Test();
        a1.algoB1.allocateAnElevator(new Call_A(0,1,2));
        assertTrue(MyAlgo.Elevators[0].getPointer().contains(1)&& MyAlgo.Elevators[0].getPointer().contains(2));
        /**
        * Check if fastest elevator is allocated (Elevator 3 : speed=12).
        */
        algo1Test a9 = new algo1Test();
        a9.algoB9.allocateAnElevator(new Call_A(0,-10,0));
        assertTrue(MyAlgo.Elevators[3].getPointer().contains(-10)&& MyAlgo.Elevators[3].getPointer().contains(0));

    }

    @Test
    void cmdElevator() {
        /**
         * Check that elevators have started to execute the calls.
         */
        algo1Test a1 = new algo1Test();
        int y = a1.algoB1.allocateAnElevator(new Call_A(0,0,1));
        a1.algoB1.cmdElevator(y);
        assertFalse(MyAlgo.Elevators[y].getPointer().contains(0));
        assertTrue(MyAlgo.Elevators[y].getPointer().contains(1));

        algo1Test a9 = new algo1Test();
        int k = a9.algoB9.allocateAnElevator(new Call_A(0,0,99));
        a9.algoB9.cmdElevator(k);
        assertFalse(MyAlgo.Elevators[k].getPointer().contains(0));
        assertTrue(MyAlgo.Elevators[k].getPointer().contains(99));
    }
}
