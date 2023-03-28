import Implimentaion.SimpleController;
import Implimentaion.SimpleFloor;
import Implimentaion.SimpleLift;
import Implimentaion.SimpleUser;
import Interface.ILift;
import org.junit.Assert;
import org.junit.Test;

import javax.naming.ldap.Control;
import java.util.Vector;

public class SimpleTest
{

    @Test
    public void IntegrationTest()
    {
        SimpleController controller = new SimpleController();
        SimpleLift lift = new SimpleLift();
        lift.setCurrentFloor(0);
        lift.setTargetFloor(SimpleController.POSITIVE);
        Vector<SimpleFloor> floors = new Vector<>();
        for (int i = 0; i < 3; i++)
            floors.add(new SimpleFloor(i));
        for (SimpleFloor floor: floors)
        {
            controller.addFloor(floor);
            floor.setController(controller);
        }
        lift.setController(controller);
        controller.addLift(lift);

        //Lift starts at floor 0
        System.out.println("Lift is currently at Floor " + lift.getCurrentFloor());

        //user1 from floor 0 wants to go to floor 2
        SimpleUser user1 = new SimpleUser(0, 2);
        floors.get(0).addUser(user1);
        Assert.assertTrue(lift.getState() == ILift.State.up);
        Assert.assertEquals(1, controller.getCommands().size());
        Assert.assertEquals(2, lift.getTargetFloor());

        //user2 from floor 0 wants to go to floor 1
        SimpleUser user2 = new SimpleUser(0, 1);
        floors.get(0).addUser(user2);
        Assert.assertEquals(2, lift.getUsers().size());
        Assert.assertEquals(2, controller.getCommands().size());
        Assert.assertEquals(1, lift.getTargetFloor());

        //user3 from floor 1 wants to go to floor 2
        SimpleUser user3 = new SimpleUser(1, 2);
        floors.get(1).addUser(user3);
        Assert.assertEquals(2, lift.getUsers().size());
        Assert.assertEquals(3, controller.getCommands().size());
        Assert.assertEquals(1, lift.getTargetFloor());

        //lift goes up, now in floor 1
        controller.process();
        Assert.assertEquals(1, lift.getCurrentFloor());
        Assert.assertEquals(2, lift.getUsers().size());

        //user 3 goes in, user2 goes out
        Assert.assertTrue(lift.getState() == ILift.State.up);
        controller.process();
        Assert.assertEquals(2, lift.getTargetFloor());
        Assert.assertEquals(2, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.up);

        //lift goes up, now in floor 2
        controller.process();
        Assert.assertEquals(2, lift.getCurrentFloor());
        Assert.assertEquals(2, lift.getUsers().size());

        //user1,2 goes out, lift stops
        controller.process();
        Assert.assertEquals(2, lift.getCurrentFloor());
        Assert.assertEquals(0, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.stop);

        //user4 from floor 2 wants to go to floor 1
        SimpleUser user4 = new SimpleUser(2, 1);
        floors.get(2).addUser(user4);
        Assert.assertEquals(1, lift.getUsers().size());
        Assert.assertEquals(1, controller.getCommands().size());
        Assert.assertEquals(1, lift.getTargetFloor());
        Assert.assertTrue(lift.getState() == ILift.State.down);

        //user5 from floor 1 wants to go to floor 0
        SimpleUser user5 = new SimpleUser(1, 0);
        floors.get(1).addUser(user5);
        Assert.assertEquals(1, lift.getUsers().size());
        Assert.assertEquals(2, controller.getCommands().size());
        Assert.assertEquals(1, lift.getTargetFloor());
        Assert.assertTrue(lift.getState() == ILift.State.down);

        //lift goes down
        controller.process();
        Assert.assertEquals(2, controller.getCommands().size());
        Assert.assertEquals(1, lift.getCurrentFloor());
        Assert.assertEquals(1, lift.getTargetFloor());
        Assert.assertEquals(1, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.down);

        //user4  goes out, user5 goes in
        controller.process();
        Assert.assertEquals(1, controller.getCommands().size());
        Assert.assertEquals(1, lift.getCurrentFloor());
        Assert.assertEquals(0, lift.getTargetFloor());
        Assert.assertEquals(1, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.down);

        //lift goes down
        controller.process();
        Assert.assertEquals(1, controller.getCommands().size());
        Assert.assertEquals(0, lift.getCurrentFloor());
        Assert.assertEquals(0, lift.getTargetFloor());
        Assert.assertEquals(1, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.down);

        //user5 goes out, lift stops at floor 0
        controller.process();
        Assert.assertEquals(0, controller.getCommands().size());
        Assert.assertEquals(0, lift.getCurrentFloor());
        Assert.assertEquals(-SimpleController.POSITIVE, lift.getTargetFloor());
        Assert.assertEquals(0, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.stop);

        //user6 from floor 1 wants to go to floor 0
        SimpleUser user6 = new SimpleUser(1, 0);
        floors.get(1).addUser(user6);
        Assert.assertEquals(0, lift.getUsers().size());
        Assert.assertEquals(1, controller.getCommands().size());
        Assert.assertEquals(1, lift.getTargetFloor());
        Assert.assertTrue(lift.getState() == ILift.State.up);

        //lift goes up
        controller.process();
        Assert.assertEquals(1, controller.getCommands().size());
        Assert.assertEquals(1, lift.getCurrentFloor());
        Assert.assertEquals(1, lift.getTargetFloor());
        Assert.assertEquals(0, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.up);

        //user6 goes in
        controller.process();
        Assert.assertEquals(1, controller.getCommands().size());
        Assert.assertEquals(1, lift.getCurrentFloor());
        Assert.assertEquals(0, lift.getTargetFloor());
        Assert.assertEquals(1, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.down);

        //lift goes down
        controller.process();
        Assert.assertEquals(1, controller.getCommands().size());
        Assert.assertEquals(0, lift.getCurrentFloor());
        Assert.assertEquals(0, lift.getTargetFloor());
        Assert.assertEquals(1, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.down);

        //user6 goes out and the lift stops
        controller.process();
        Assert.assertEquals(0, controller.getCommands().size());
        Assert.assertEquals(0, lift.getCurrentFloor());
        Assert.assertEquals(-SimpleController.POSITIVE, lift.getTargetFloor());
        Assert.assertEquals(0, lift.getUsers().size());
        Assert.assertTrue(lift.getState() == ILift.State.stop);

    }
}
