package commands;

import collection.CollectionManager;
import data.Person;
import exceptions.FileException;
import file.ReaderWriter;

import java.io.IOException;

public class SaveCommand extends CommandImpl {
    ReaderWriter fileManager;
    CollectionManager<Person> collectionManager;
    public SaveCommand(CollectionManager<Person> cm, ReaderWriter fm){
        super("save",CommandType.SERVER_ONLY);
        collectionManager = cm;
        fileManager = fm;
    }
    @Override
    public String execute()  {
        if(hasStringArg()) {
            fileManager.setPath(getStringArg());
        };
        fileManager.write(collectionManager.serializeCollection());
        return "collection successfully saved";
    }
}
