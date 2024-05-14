import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Implements a hash tree with generic type
 * @param <E> Generic
 */
public class HashTable <E> {

    public static final int DEFAULT_SIZE = 101; //Default size of a hash table

    public LinkedList<E>[] lists; // An array of linked lists to store data

    public int currentSize; //Current number of elements in the hash table

    public ArrayList<Double> valList = new ArrayList<>(); //An array list to store the indexes of the array which store an element

    /**
     * Default Constructor
     */
    HashTable(){
        this(DEFAULT_SIZE);
    }

    /**
     * Constructor
     * @param size of the array inside hash table
     */
    HashTable(int size){
        lists = new LinkedList[nextPrime(size)];
        for(int i = 0; i < lists.length; i++){
            lists[i] = new LinkedList<>();
        }
        currentSize = 0 ;
    }

    /**
     * Inserts an element to the hash table
     * @param val the element to insert
     * @return true if the item is inserted. False if there is an item with the same name in the hash table
     * @throws IOException
     */
    public Boolean insert(E val) throws IOException {
        LinkedList<E> aimList = lists[(hash(val))];
        if(!contains(val)){
            valList.add((double)hash(val));
            aimList.push(val);
            currentSize++;
            if (currentSize > lists.length){
                valList.clear();
                rehash();
            }
            return true;
        }
        return false;

    }

    /**
     * Gets an element from the hash table with its name
     * @param string the name of the element
     * @return the element as an object
     */
    public E get(String string){
        int hashVal = string.hashCode();
        hashVal = hashVal % lists.length;
        if (hashVal < 0){
            hashVal += lists.length;
        }
        for( E element :lists[hashVal]){
            String name = "";
            if (element instanceof City){
                name = ((City) element).name;
            }
            else if (element instanceof Branch){
                name = ((Branch) element).name;
            }
            else if(element instanceof Employee){
                name = ((Employee) element).name;
            }

            if (Objects.equals(name, string)){
                return element;
            }
        }
        return null;
    }

    /**
     * Removes an element from the hash table
     * @param val the element to remove
     */
    public void remove(E val){
        LinkedList<E> toRemove = lists[hash(val)];
        if(toRemove.contains(val)){
            valList.remove((double)hash(val));
            toRemove.remove(val);
            currentSize --;
        }
    }

    /**
     * Hashes an object with respect to its name
     * @param element the object to be hashed
     * @return the hash value
     */
    public int hash(E element){
        int out = -1;
        if(element instanceof City city) {
            out = city.name.hashCode();
        }
        else if(element instanceof Branch branch){
            out = branch.name.hashCode();
        }
        else if(element instanceof Employee employee){
            out = employee.name.hashCode();
        }
        else if(element instanceof String){
            out = element.hashCode();
        }

        out = out% lists.length;
        if(out < 0){
            out += lists.length;
        }
        return out;
    }

    /**
     * Rehashes an array with doubling the size
     * @throws IOException
     */
    public void rehash() throws IOException {
        LinkedList<E>[] old = lists;
        lists = new LinkedList[nextPrime(lists.length * 2)];
        for ( int i = 0; i < lists.length; i++){
            lists[i] = new LinkedList<>();
        }
        currentSize = 0;
        for(LinkedList<E> list: old){
            for(E data: list){
                insert(data);
            }
        }
    }

    /**
     * To find an element if it is in the hash table
     * @param element the element we want to check
     * @return true if it is in the table. False otherwise
     */
    public boolean contains(E element){
        LinkedList<E> aimList = lists[(hash(element))];
        if(element instanceof Employee){
            for(E val: aimList){
                if (Objects.equals(((Employee) val).name, ((Employee) element).name)){
                    return true;
                }
            }
            return false;
        }
        if(element instanceof Branch){
            for(E val: aimList){
                if (Objects.equals(((Branch) val).name, ((Branch) element).name)){
                    return true;
                }
            }
            return false;
        }
        if(element instanceof City){
            for(E val: aimList){
                if (Objects.equals(((City) val).name, ((City) element).name)){
                    return true;
                }
            }
            return false;
        }
        return false;
    }


    /**
     * Finds the next prime after a number
     * @param num the number
     * @return the next prime
     */
    public int nextPrime(int num){
        if(num % 2 == 0){
            num ++;
        }
        while (!isPrime(num)){
            num ++;
        }
        return num;
    }

    /**
     * Finds if a number is prime
     * @param num the number to be checked
     * @return true if number is prime. False otherwise
     */
    public boolean isPrime(int num){
        if( num == 2 || num ==3 ){
            return true;
        }
        if( num == 1 || num % 2 == 0 ){
            return false;
        }
        for(int i = 3; i * i <= num; i++){
            if ( num % i == 0){
                return  false;
            }
        }
        return true;
    }
}
