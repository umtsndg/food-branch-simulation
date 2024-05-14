/**
 * The employee class
 */
public class Employee {
    public String name; // Name of the person

    public int job; // Job of the person. 0 if courier, 1 if cashier, 2 if cook, 3 if manager


    public int promotionPoint; // Promotion points of the employee

    /**
     * Constructor
     * @param name name of the employee
     * @param job job of the employee
     */
    Employee(String name, int job ){
        this.job = job;
        this.name = name;
        this.promotionPoint = 0;
    }




}
