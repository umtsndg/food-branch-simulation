import java.io.IOException;
import java.util.Objects;

/**
 * The company class
 */
public class Company {
    HashTable<City> cities; // A hash table to store cities

    /**
     * Constructor
     */
    public Company() {
        cities = new HashTable<>();
    }

    /**
     * Inserts an employee to a company
     * @param data the data of the employee
     * @throws IOException
     */
    public void insertToCompany(String[] data ) throws IOException {
        if(Objects.equals(data[3].strip(), "COURIER")){
            cities.get(data[0].strip()).branches.get(data[1].strip()).insertToBranch(data[2].strip(),"COURIER");
        }
        else if(Objects.equals(data[3].strip(), "CASHIER")){
            cities.get(data[0].strip()).branches.get(data[1].strip()).insertToBranch(data[2].strip(),"CASHIER");
        }
        else if(Objects.equals(data[3].strip(), "COOK")){
            cities.get(data[0].strip()).branches.get(data[1].strip()).insertToBranch(data[2].strip(),"COOK");
        }
        else if(Objects.equals(data[3].strip(), "MANAGER")){
            cities.get(data[0].strip()).branches.get(data[1].strip()).insertToBranch(data[2].strip(),"MANAGER");
        }
    }
}

