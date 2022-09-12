package collection;

import data.Person;

import java.util.TreeSet;
/**
 * interface for storing elements
 * @param <T> type of elements
 */
public interface CollectionManager<T> {
    long generateID();
    TreeSet<T> getTreeMap();
    String getInfo();
    /**
     * checks if collection contains element with particular id
     * @param ID
     * @return
     */
    boolean checkID(Long ID);
    boolean removeByID(Long id);
    /**
     * updates element by id
     * @param id
     * @param newElement
     */
    void updateID(Long id, T newElement);
    public boolean updateIdBoolean(Long id, T newElement);
    int getSize();
    /**
     * get collection size
     * @return
     */
    void clear();
    /**
     * convert collection to json
     * @param json
     * @return true if success, false if not
     */
    void add(T element);
    /**
     * parse collection from json
     * @return
     */
    void removeElement(T element);
    /**
     * adds element if it is greater than max
     * @param element
     */
    boolean addIfMax(T element);
    String printContainsName(String string);
}
