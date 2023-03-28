package Interface;

import Abstarct.User;

import java.util.Vector;

public interface IFloor
{
    public void pressUp();
    public void pressDown();
    public void addUser(User user);
    public void setController(IController controller);
    public Vector<User> getUsers();
    public int getFloorNumber();
}
