package Implimentaion;

import Abstarct.Command;

public class SimpleCommand extends Command
{
    public SimpleCommand(String source, int floor)
    {
        if (source.equals("floor"))
            this.type = Type.floor;
        else
            this.type = Type.lift;
        this.floor = floor;
    }
}
