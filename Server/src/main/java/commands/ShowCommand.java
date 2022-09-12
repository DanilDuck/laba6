package commands;

import collection.CollectionManager;
import data.Person;
import exceptions.EmptyCollectionException;

import java.util.TreeSet;

public class ShowCommand extends CommandImpl{
    private CollectionManager<Person> collectionManager;
    String s = " ";
    public ShowCommand(CollectionManager<Person> cm){
        super("show",CommandType.NORMAL);
        collectionManager = cm;
    }

    @Override
    public String execute(){
        if (collectionManager.getTreeMap().isEmpty()) throw new EmptyCollectionException();
        else {
            TreeSet<Person> treeSet = collectionManager.getTreeMap();
            return treeSet.toString();
        }
    }

}

