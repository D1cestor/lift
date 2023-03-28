package Interface;

import Abstarct.Command;

public interface IController
{
    void process();
    void addCommand(Command command);

    void changeTargetFloor(Command command);
}
