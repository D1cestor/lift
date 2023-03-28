package Implimentaion;

import Abstarct.Command;
import Abstarct.User;
import Interface.IController;
import Interface.IFloor;
import Interface.ILift;

import java.util.List;
import java.util.Vector;

public class SimpleController implements IController
{
    public Vector<Command> commands;
    private ILift lift;
    private Vector<IFloor> floors;
    private int minFloor;
    private int maxFloor;

    public static final int POSITIVE = 10000;

    public SimpleController()
    {
        commands = new Vector<>();
        floors = new Vector<>();
        this.minFloor = 1;
        this.maxFloor = 1;
    }

    public void processUserMoving()
    {
        int originTargetFloor = lift.getTargetFloor();
        boolean change = false;
        int currentFloor = lift.getCurrentFloor();
        commands.removeIf(command -> command.getFloor() == currentFloor);
        List<User> users = lift.getUsers();
        users.removeIf(user -> user.getTargetFloor() == currentFloor);
        if (lift.getState() == ILift.State.down) lift.setTargetFloor(-POSITIVE);
        else lift.setTargetFloor(POSITIVE);
        for (Command command: commands)
        {
            changeTargetFloor(command);
            if (!change)
                if ((lift.getState() == ILift.State.up && lift.getTargetFloor() != POSITIVE) ||
                        (lift.getState() == ILift.State.down && lift.getTargetFloor() != -POSITIVE))
                    change = true;
        }
        for (IFloor floor: floors)
        {
            if (floor.getFloorNumber() == currentFloor)
            {
                Vector<User> usersAtTheFloor = floor.getUsers();
                for (User userAtTheFloor: usersAtTheFloor)
                {
                    this.lift.getUsers().add(userAtTheFloor);
                    Command command = new SimpleCommand("lift", userAtTheFloor.getTargetFloor());
                    addCommand(command);
                    if (!change)
                        if ((lift.getState() == ILift.State.up && lift.getTargetFloor() != POSITIVE) ||
                                (lift.getState() == ILift.State.down && lift.getTargetFloor() != -POSITIVE))
                            change = true;
                }
                usersAtTheFloor.clear();
            }
        }
        // If there is no commands, set the state to stop
        if (commands.isEmpty())
        {
            System.out.println("Lift stops");
            lift.setState(ILift.State.stop);
        }
        else if (!change)
        {
            // Change the direction
            if (lift.getState() == ILift.State.up) lift.setState(ILift.State.down);
            else lift.setState(ILift.State.up);
            lift.setTargetFloor(commands.get(0).getFloor());
            for (Command command: commands)
            {
                if (Math.abs(lift.getTargetFloor()) - lift.getCurrentFloor() >
                        Math.abs(command.getFloor() - lift.getCurrentFloor() ) )
                {
                    lift.setTargetFloor(command.getFloor());
                }
            }
            System.out.println("TargetFloor is "+ lift.getTargetFloor());
        }

    }



    public void addFloor(IFloor floor)
    {
        this.floors.add(floor);
        int floorNum = floor.getFloorNumber();
        if (floorNum > maxFloor) maxFloor = floorNum;
        if (floorNum < minFloor) minFloor = floorNum;
    }

    public void addLift(ILift lift)
    {
        this.lift = lift;
    }

    public Vector<Command> getCommands()
    {
        return commands;
    }

    @Override
    public void process()
    {
        if (lift.getTargetFloor() > lift.getCurrentFloor())
        {
            lift.setState(ILift.State.up);
            System.out.println("Lift is going up");
            lift.up();
            System.out.println("Lift is currently at Floor " + lift.getCurrentFloor());
        }
        else if (lift.getTargetFloor() < lift.getCurrentFloor())
        {
            lift.setState(ILift.State.down);
            System.out.println("Lift is going down");
            lift.down();
            System.out.println("Lift is currently at Floor " + lift.getCurrentFloor());
        }
        else
        {
            lift.openDoor();
            processUserMoving();
            lift.closeDoor();
        }

    }

    @Override
    public void addCommand(Command command)
    {
        // if the lift is not working
        if (lift.getState() == ILift.State.stop || command.getFloor() == lift.getCurrentFloor())
        {
            int targetFloor = command.getFloor();
            if (targetFloor != lift.getCurrentFloor())
            {
                lift.setTargetFloor(targetFloor);
                if (targetFloor > lift.getCurrentFloor())
                    lift.setState(ILift.State.up);
                else
                    lift.setState(ILift.State.down);
                this.commands.add(command);
                System.out.println("TargetFloor is "+ lift.getTargetFloor());
            }
            else
            {
                lift.openDoor();
                processUserMoving();
                lift.closeDoor();
            }
        }
        else
        {
            changeTargetFloor(command);
            this.commands.add(command);
        }



    }

    @Override
    public void changeTargetFloor(Command command)
    {
        ILift.State targetState;
        if (command.getFloor() > lift.getCurrentFloor()) targetState = ILift.State.up;
        else targetState = ILift.State.down;

        // If the current direction is the same as the direction of the command
        if (targetState == lift.getState())
        {
            //If the destination of the command is closer than the targetFloor
            if (Math.abs(lift.getCurrentFloor() - command.getFloor()) <
                    Math.abs(lift.getCurrentFloor() - lift.getTargetFloor()))
            {
                lift.setTargetFloor(command.getFloor());
                System.out.println("TargetFloor is "+ lift.getTargetFloor());
            }
        }
    }


    public Vector<IFloor> getFloors()
    {
        return floors;
    }
}
