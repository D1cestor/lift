import Abstarct.Command;
import Abstarct.User;
import Implimentaion.*;
import Interface.IFloor;
import Interface.ILift;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Vector;

public class SimpleControllerTest
{
    SimpleController controller;
    SimpleLift lift;

    @Before
    public void init()
    {
//        System.out.close();
        controller = new SimpleController();
        lift = new SimpleLift();
        Vector<SimpleFloor> floors = new Vector<>();
        for (int i = 0; i < 3; i++)
            floors.add(new SimpleFloor(i));
        for (SimpleFloor floor: floors)
        {
            controller.addFloor(floor);
            floor.setController(controller);
        }
        controller.addLift(lift);
    }


    @Test
    public void processUserMovingTest()
    {
        lift.setCurrentFloor(1);
        lift.setTargetFloor(1);
        User user1 = new SimpleUser(2,0);
        User user2 = new SimpleUser(2,1);
        controller.addCommand(new SimpleCommand("lift", 0));
        lift.setState(ILift.State.down);
        lift.getUsers().add(user1);
        lift.getUsers().add(user2);
        User user3 = new SimpleUser(1,0);
        IFloor floor = controller.getFloors().get(1);
        floor.getUsers().add(user3);
        Assert.assertEquals(1, floor.getUsers().size());
        Assert.assertEquals(2, lift.getUsers().size());
        controller.processUserMoving();
        Assert.assertTrue(lift.getState() == ILift.State.down);
        Assert.assertEquals(0, lift.getTargetFloor());
        Assert.assertEquals(0, floor.getUsers().size());
        Assert.assertEquals(2, lift.getUsers().size());
        Assert.assertTrue(lift.getUsers().get(0).getTargetFloor() == 0);
        lift.setCurrentFloor(0);
        lift.setCurrentFloor(0);
        floor = controller.getFloors().get(0);
        controller.processUserMoving();
        Assert.assertEquals(0, floor.getUsers().size());
        Assert.assertEquals(0, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.stop);

        lift.setCurrentFloor(0);
        lift.setState(ILift.State.down);
        controller.getCommands().clear();
        controller.getCommands().add(new SimpleCommand("floor", 2));
        controller.getCommands().add(new SimpleCommand("floor", 1));
        controller.processUserMoving();
        Assert.assertEquals(1, lift.getTargetFloor());
        Assert.assertEquals(ILift.State.up, lift.getState());

        lift.setCurrentFloor(2);
        lift.setTargetFloor(2);
        lift.setState(ILift.State.up);
        controller.getCommands().clear();
        controller.getCommands().add(new SimpleCommand("floor", 1));
        controller.getCommands().add(new SimpleCommand("floor", 0));
        controller.processUserMoving();
        Assert.assertEquals(1, lift.getTargetFloor());
        Assert.assertEquals(ILift.State.down, lift.getState());

        lift.setCurrentFloor(2);
        lift.setTargetFloor(2);
        lift.setState(ILift.State.down);
        controller.getCommands().clear();
        controller.getCommands().add(new SimpleCommand("floor", 1));
        controller.getCommands().add(new SimpleCommand("floor", 0));
        controller.processUserMoving();
        Assert.assertEquals(1, lift.getTargetFloor());
        Assert.assertEquals(ILift.State.down, lift.getState());
    }


    @Test
    public void addCommandTest()
    {
        lift.setTargetFloor(0);
        lift.setCurrentFloor(0);
        controller.addCommand(new SimpleCommand("floor", 1));
        Assert.assertEquals(1, controller.getCommands().size());
        Assert.assertEquals(1, lift.getTargetFloor());
        lift.setCurrentFloor(1);
        lift.setState(ILift.State.stop);
        controller.addCommand(new SimpleCommand("floor", 1));
        Assert.assertEquals(0, controller.getCommands().size());
        controller.addCommand(new SimpleCommand("lift", 2));
        controller.addCommand(new SimpleCommand("lift", 0));
        Assert.assertTrue(lift.getState() == ILift.State.up);
        Assert.assertEquals(2, controller.getCommands().size());
    }

    @Test
    public void changeTargetFloor()
    {
        lift.setCurrentFloor(1);
        lift.setTargetFloor(-3);
        lift.setState(ILift.State.down);
        controller.changeTargetFloor(new SimpleCommand("floor", -3));
        Assert.assertEquals(-3, lift.getTargetFloor());
        controller.changeTargetFloor(new SimpleCommand("floor", -2));
        controller.changeTargetFloor(new SimpleCommand("floor", -1));
        Assert.assertEquals(-1, lift.getTargetFloor());
        controller.changeTargetFloor(new SimpleCommand("floor", 5));
        Assert.assertEquals(-1, lift.getTargetFloor());
    }

    @Test
    public void processTest()
    {
        lift.setTargetFloor(0);
        lift.setCurrentFloor(2);
        controller.process();
        Assert.assertEquals(1, lift.getCurrentFloor());
        controller.process();
        Assert.assertEquals(0, lift.getCurrentFloor());
        controller.process();
        Assert.assertEquals(0, lift.getCurrentFloor());
        lift.setTargetFloor(2);
        controller.process();
        Assert.assertEquals(1, lift.getCurrentFloor());
        controller.process();
        Assert.assertEquals(2, lift.getCurrentFloor());
        controller.process();
        Assert.assertEquals(2, lift.getCurrentFloor());
    }

}
