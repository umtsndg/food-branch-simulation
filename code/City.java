/**
 * The city class
 */
public class City {
    public String name; //Name of the city
    public HashTable<Branch> branches; // A hash table to store all branches inside a city

    /**
     * Constructor
     * @param name name of the city
     */
    public City(String name){
        this.name = name;
        branches = new HashTable<>();
    }


}
