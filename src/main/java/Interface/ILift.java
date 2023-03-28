package Interface;

import java.util.List;

public interface ILift
{
    enum State{up, down, stop};
    public void up();
    public void down();
    public void openDoor();
//    public void selectFloor(int floor);
    public void setState(State state);
    public State getState();
    public void setController(IController controller);
    public int getCurrentFloor();
    public void setTargetFloor(int targetFloor);
    public int getTargetFloor();
    public List getUsers();
    public void closeDoor();

    public void setCurrentFloor(int currentFloor);

}
