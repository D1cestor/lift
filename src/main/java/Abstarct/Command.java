package Abstarct;

public abstract class Command
{
    public enum Type{floor, lift};
    protected int floor;
    protected Type type;

    public int getFloor()
    {
        return this.floor;
    }
    public Type getType()
    {
        return this.type;
    }
}
