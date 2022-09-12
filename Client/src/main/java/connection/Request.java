package connection;

import data.Person;

import java.io.Serializable;

public interface Request extends Serializable {
    public String getStringArg();
    public Person getPerson();
    public String getCommandName();
}
