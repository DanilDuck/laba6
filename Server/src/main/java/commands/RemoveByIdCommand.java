package commands;

import collection.CollectionManager;
import data.Person;
import exceptions.*;

public class RemoveByIdCommand extends CommandImpl{
    private CollectionManager<Person> collectionManager;
    public RemoveByIdCommand(CollectionManager<Person> cm){
        super("remove_by_id",CommandType.NORMAL);
        collectionManager = cm;
    }


    @Override
    public String execute() throws InvalidDataException {
        if(collectionManager.getTreeMap().isEmpty()) throw new EmptyCollectionException();
        if(!hasStringArg()) throw new MissedCommandArgumentException();
        long id = Long.parseLong(getStringArg());
        if(!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("no such id");
        boolean success = collectionManager.removeByID(id);
        if (success) return "element #" + id + " removed";
        else throw new CommandException("cannot remove");
    }

}
