import Abstarct.Command;
import Implimentaion.*;
import Interface.IController;
import Interface.IFloor;
import Interface.ILift;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

public class SimpleFloorTest
{
    IFloor floor;
    SimpleController controller;
    ILift lift;

    @Before
    public void init()
    {
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
    public void pressUpTest()
    {
        floor = controller.getFloors().get(1);
        floor.pressUp();
        Assert.assertEquals(1, lift.getTargetFloor());
    }


    @Test
    public void pressDownTest()
    {
        floor = controller.getFloors().get(1);
        floor.pressDown();
        Assert.assertEquals(1, lift.getTargetFloor());
    }

    @Test
    public void addUserTest()
    {
        floor = controller.getFloors().get(1);
        SimpleUser user1 = new SimpleUser(1, 2);
        floor.addUser(user1);
        Assert.assertEquals(1, lift.getTargetFloor());
        controller.process();
        SimpleUser user2 = new SimpleUser(1, 0);
        floor.addUser(user2);
        Command command = controller.getCommands().get(controller.getCommands().size() - 1);
        Assert.assertEquals(0, command.getFloor());
        Assert.assertEquals(Command.Type.lift, command.getType());
    }


}
