package Implimentaion;

import Abstarct.Command;
import Abstarct.User;
import Interface.IController;
import Interface.ILift;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SimpleLift implements ILift
{
    private int currentFloor;
    private int targetFloor;
    private boolean door;
    private State state;
    private List<User> users;
    private IController controller;

    public SimpleLift()
    {
        this.users = new ArrayList<>();
        this.door = false;
        this.state = State.stop;
    }


    @Override
    public void up()
    {
        this.currentFloor++;
    }

    @Override
    public void down()
    {
        this.currentFloor--;
    }

    @Override
    public void openDoor()
    {
        this.door = true;
        System.out.println("Door is opened at floor " + this.currentFloor);

    }

//    @Override
//    public void selectFloor(int floor)
//    {
//        Command command = new SimpleCommand("lift", floor);
//        this.controller.addCommand(command);
//    }

    @Override
    public void setState(State state)
    {
        this.state = state;
    }

    @Override
    public State getState()
    {
        return this.state;
    }

    @Override
    public void setController(IController controller)
    {
        this.controller = controller;
    }

    @Override
    public int getCurrentFloor()
    {
        return this.currentFloor;
    }

    @Override
    public void setTargetFloor(int targetFloor)
    {
        this.targetFloor = targetFloor;
    }

    @Override
    public int getTargetFloor()
    {
        return this.targetFloor;
    }

    @Override
    public List<User> getUsers()
    {
        return users;
    }

    @Override
    public void closeDoor()
    {
        this.door = false;
        System.out.println("Door is closed at floor " + this.currentFloor);
    }

    @Override
    public void setCurrentFloor(int currentFloor)
    {
        this.currentFloor = currentFloor;
    }
}
