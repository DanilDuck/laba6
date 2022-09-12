package commands;

import collection.CollectionManager;
import data.Person;
import exceptions.FileException;
import file.ReaderWriter;

import java.io.IOException;

public class LoadCommand extends CommandImpl {
    ReaderWriter fileManager;
    CollectionManager<Person> collectionManager;
    public LoadCommand(CollectionManager<Person> cm, ReaderWriter fm){
        super("load",CommandType.SERVER_ONLY);
        collectionManager = cm;
        fileManager = fm;
    }
    @Override
    public String execute() {
        if(hasStringArg()) {
            fileManager.setPath(getStringArg());
        };
        collectionManager.deserializeCollection(fileManager.read());
        return "collection successfully loaded";
    }
}
