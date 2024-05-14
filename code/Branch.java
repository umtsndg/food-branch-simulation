import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.Objects;

/**
 * The branch class
 */
public class Branch {
    public static Writer writer; // The writer from the main
    public String name; // Name of the district which the branch is in
    public int monthlyBranchBonus; // Branch's this month given bonus
    public int totalBranchBonus; // Branch's total given bonus
    public int cashierCount; // The number of cashiers in the branch
    public int cookCount; // The number of cooks in the branch
    public int courierCount; // The number of couriers in the branch
    public HashTable<Employee> employees; // A hash table to store all employees
    public LinkedList<Employee> cashierWhiteList = new LinkedList<>(); // A linked list to store cashiers with more than or equal to 3 promotion points
    public LinkedList<Employee> cashierBlackList = new LinkedList<>(); // A linked list to store cashiers with less than or equal to -5 promotion points

    public LinkedList<Employee> cookWhiteList = new LinkedList<>(); // A linked list to store cooks with more than or equal to 10 promotion points
    public LinkedList<Employee> cookBlacklist = new LinkedList<>(); // A linked list to store cooks with less than or equal to -5 promotion points
    public LinkedList<Employee> courierBlackList = new LinkedList<>(); // A linked list to store couriers with less than or equal to -5 promotion points
    public Employee manager; // The manager of the branch

    /**
     * Constructor
     * @param name name of the district which the branch is in
     */
    public Branch(String name) {
        this.name = name;
        monthlyBranchBonus = 0;
        totalBranchBonus = 0;
        employees = new HashTable<>();
    }

    /**
     * Inserts an employee to a branch. If there is already someone with the same name it doesn't add it.
     * @param nm name of the employee
     * @param job job of the employee
     * @throws IOException
     */
    public void insertToBranch(String nm, String job) throws IOException {
        if (Objects.equals(job, "COURIER") && employees.insert(new Employee(nm, 0))) {
            courierCount++;
        } else if (Objects.equals(job, "CASHIER") && employees.insert(new Employee(nm, 1))) {
            cashierCount++;
        } else if (Objects.equals(job, "COOK") && employees.insert(new Employee(nm, 2))) {
            cookCount++;
        } else if (employees.insert(new Employee(nm, 3))) {
            manager = employees.get(nm);
        } else {
            Branch.writer.write("Existing employee cannot be added again.\n");
        }
    }

    /**
     * Removes an employee from the branch. If it can't be removed because of certain conditions gives the employee a bonus.
     * Doesn't give the bonus if the employee's promotion point is less than 5
     * @param employee the employee which wants to leave
     * @param job the job of the emoployee
     * @throws IOException
     */

    public void leave(Employee employee, int job) throws IOException {
        int size = -1;
        LinkedList<Employee> list = null;
        if (job == 0) {
            size = courierCount;
            list = courierBlackList;
        } else if (job == 1) {
            size = cashierCount;
            list = cashierBlackList;
        } else if (job == 2) {
            size = cookCount;
            list = cookBlacklist;
        }

        if(employee.promotionPoint <= -5){
            return;
        }
        else if ((size == 1 && !LinkedListContains(list,employee)) || (employee.job == 3 && cookWhiteList.isEmpty()) ) {
            monthlyBranchBonus += 200;
            totalBranchBonus += 200;
        }
        else {
            if (job != 3) {
                if (size == cashierCount) {
                    cashierWhiteList.remove(employee);
                    cashierBlackList.remove(employee);
                }
                if (size == courierCount) {
                    courierBlackList.remove(employee);
                }
                if (size == cookCount) {
                    cookBlacklist.remove(employee);
                    cookWhiteList.remove(employee);
                }
                employees.remove(employee);
                writer.write(String.format("%s is leaving from branch: %s.\n", employee.name, name));
                if (job == 0) {
                    courierCount--;
                } else if (job == 1) {
                    cashierCount--;
                } else if (job == 2) {
                    cookCount--;
                }

            } else {
                if (!cookWhiteList.isEmpty() && cookCount > 1) {
                    writer.write(String.format("%s is leaving from branch: %s.\n", manager.name, name));
                    employees.remove(manager);
                    manager = cookWhiteList.remove();
                    manager.promotionPoint -= 10;
                    manager.job++;
                    writer.write(String.format("%s is promoted from Cook to Manager.\n", manager.name));
                }
            }
        }

    }

    /**
     * Checks if a promotion should happen in a branch
     * @throws IOException
     */
    public void checkForPromotions() throws IOException {
        if (!cookWhiteList.isEmpty() && cookCount > 1 && manager.promotionPoint <= -5) {
            writer.write(String.format("%s is dismissed from branch: %s.\n", manager.name, name));
            employees.remove(manager);
            manager = cookWhiteList.remove();
            manager.promotionPoint -= 10;
            manager.job++;
            writer.write(String.format("%s is promoted from Cook to Manager.\n", manager.name));
        }
        while (!cashierWhiteList.isEmpty() && cashierCount > 1) {
            Employee employee = cashierWhiteList.remove();
            employee.promotionPoint -= 3;
            employees.get(employee.name).job += 1;
            cashierCount -= 1;
            cookCount += 1;
            writer.write(String.format("%s is promoted from Cashier to Cook.\n", employee.name));
            if(employee.promotionPoint >= 10){
                cookWhiteList.add(employee);
            }
        }
    }

    /**
     * Checks if a dismissal should occur in a branch
     * @throws IOException
     */
    public void checkForDismissals() throws IOException {
        while (!courierBlackList.isEmpty()) {
            if (courierCount > 1) {
                Employee employee = courierBlackList.remove();
                writer.write(String.format("%s is dismissed from branch: %s.\n", employee.name, name));
                courierCount--;
                employees.remove(employee);
            }

        }
        while (!cookBlacklist.isEmpty() && cookCount > 1) {
            Employee employee = cookBlacklist.peek();
            writer.write(String.format("%s is dismissed from branch: %s.\n", employee.name, name));
            employees.remove(employee);
            cookBlacklist.remove();
            cookCount--;
        }
        while (!cashierBlackList.isEmpty() && cashierCount > 1) {
            Employee employee = cashierBlackList.peek();
            writer.write(String.format("%s is dismissed from branch: %s.\n", employee.name, name));
            employees.remove(employee);
            cashierBlackList.remove();
            cashierCount--;
        }
    }

    /**
     * A method to find if an employee is in a linked list by looking at the name of the employee
     * @param list the linked list
     * @param toBeChecked the employee
     * @return true if linked list contains the employee. false otherwise
     */
    public boolean LinkedListContains(LinkedList<Employee> list, Employee toBeChecked){
        for(Employee employee : list){
            if (Objects.equals(toBeChecked.name, employee.name)){
                return true;
            }
        }
        return false;
    }
}