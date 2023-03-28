package Implimentaion;

import Abstarct.User;

public class SimpleUser extends User
{
    public SimpleUser(int currentFloor, int targetFloor)
    {
        this.targetFloor = targetFloor;
        this.currentFloor = currentFloor;
        if (targetFloor > currentFloor)
            this.direction = Direction.up;
        else
            this.direction = Direction.down;
    }
}
