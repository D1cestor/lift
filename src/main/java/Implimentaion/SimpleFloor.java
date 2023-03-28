package Implimentaion;

import Abstarct.Command;
import Abstarct.User;
import Interface.IController;
import Interface.IFloor;

import java.util.Vector;

public class SimpleFloor implements IFloor
{
    private int floorNumber;
    private boolean up;
    private boolean down;
    private Vector<User> users;
    private IController controller;

    public SimpleFloor(int floorNumber)
    {
        this.users = new Vector<>();
        this.floorNumber = floorNumber;
    }


    @Override
    public void pressUp()
    {
        this.up = true;
        Command command = new SimpleCommand("floor", this.floorNumber);
        controller.addCommand(command);
    }

    @Override
    public void pressDown()
    {
        this.down = true;
        Command command = new SimpleCommand("floor", this.floorNumber);
        controller.addCommand(command);
    }

    @Override
    public void addUser(User user)
    {
        users.add(user);
        if (user.getDirection() == User.Direction.up)
            pressUp();
        else
            pressDown();
    }

    @Override
    public void setController(IController controller)
    {
        this.controller = controller;
    }

    @Override
    public Vector<User> getUsers()
    {
        return this.users;
    }

    @Override
    public int getFloorNumber()
    {
        return this.floorNumber;
    }
}
