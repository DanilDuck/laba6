package commands;

import collection.CollectionManager;
import data.Person;
import exceptions.CommandException;
import exceptions.InvalidDataException;

public class AddCommand extends CommandImpl{
    private CollectionManager<Person> collectionManager;
    public AddCommand(CollectionManager<Person> cm){
        super("add",CommandType.NORMAL);
        collectionManager = cm;
    }
    public CollectionManager<Person> getManager(){
        return collectionManager;
    }
    @Override
    public String execute() throws InvalidDataException, CommandException {
        getManager().add(getPersonArg());
        return "Added element: " + getPersonArg().toString();
    }
}
