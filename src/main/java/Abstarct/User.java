package Abstarct;

public abstract class User
{
    public enum Direction{up, down};
    protected int targetFloor;
    protected int currentFloor;
    protected Direction direction;
    public Direction getDirection()
    {
        return this.direction;
    }
    public int getTargetFloor() {return this.targetFloor;}
}
