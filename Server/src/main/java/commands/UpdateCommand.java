package commands;

import collection.CollectionManager;
import data.Person;
import exceptions.*;

public class UpdateCommand extends CommandImpl{
    private CollectionManager<Person> collectionManager;
    public UpdateCommand(CollectionManager<Person> cm){
        super("update_id",CommandType.NORMAL);
        collectionManager = cm;
    }
    @Override
    public String execute() throws InvalidDataException {
        if(collectionManager.getTreeMap().isEmpty()) throw new EmptyCollectionException();
        if(!hasStringArg()||!hasWorkerArg()) throw new MissedCommandArgumentException();
        Long id = Long.parseLong(getStringArg());
        if(!collectionManager.checkID(id)) throw new InvalidCommandArgumentException("no such id");

        boolean success = collectionManager.updateIdBoolean(id,getPersonArg());
        if (success) return "element #" + id + " updated";
        else throw new CommandException("cannot update");
    }
}
