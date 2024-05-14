/**
 * Umut Şendağ CMPE 250 HW 2 2021400072
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Main class
 */
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        try{
            //Taking file inputs and creating the output file
            File file = new File(args[1]);
            File initial = new File(args[0]);
            Scanner fileScanner = new Scanner(file);
            Scanner initialScanner = new Scanner(initial);
            FileWriter writer = new FileWriter(args[2]);
            Branch.writer = writer;

            //Creating the company
            Company company = new Company();

            //Reading the initial state of the company and creating cities, branches, and employees
            while (initialScanner.hasNextLine()){
                String[] data = initialScanner.nextLine().split(",");
                if(company.cities.get(data[0].strip()) == null) {
                    company.cities.insert(new City(data[0].strip()));
                }
                if(company.cities.get(data[0].strip()).branches.get(data[1].strip())== null) {
                    company.cities.get(data[0].strip()).branches.insert(new Branch(data[1].strip()));
                }
                company.insertToCompany(data);
            }
            initialScanner.close();

            //Main loop of the algorithm
            while (fileScanner.hasNextLine()){
                String data = fileScanner.nextLine();

                //If the doesn't end with a ":" or if it is not a blank line it is an operation
                if(!Objects.equals(data, "") && !data.endsWith(":") ){

                    //Splitting the data into usable variables
                    String operation;
                    String[] info;
                    operation = data.split(":")[0];
                    info = data.split(":")[1].split(",");

                    if(Objects.equals(operation, "LEAVE")){

                        //We try to find the branch and make our employee leave.
                        try {
                            Branch branch = company.cities.get(info[0].strip()).branches.get(info[1].strip());
                            branch.leave(branch.employees.get(info[2].strip()), branch.employees.get(info[2].strip()).job);
                        }

                        //If any error occurs during this process It means there is no such employee
                        catch (Exception e){
                            writer.write("There is no such employee.\n");
                        }

                    }
                    else if(Objects.equals(operation, "PERFORMANCE_UPDATE")){
                        int score = Integer.parseInt(info[3].strip());
                        Branch branch = company.cities.get(info[0].strip()).branches.get(info[1].strip());
                        Employee employee = branch.employees.get(info[2].strip());
                        try {

                            //We keep the old score as variable so if an employee gets out of a whitelist or a blacklist
                            //we instantly remove them from that list
                            int oldScore = employee.promotionPoint;
                            int newScore = oldScore + score / 200;
                            employee.promotionPoint = newScore;

                            //If an employee got his promotion point out of the critical zone we remove it from the relevant blacklist
                            if(oldScore <= -5 && newScore > -5){
                                if(employee.job == 0){
                                    branch.courierBlackList.remove(employee);
                                }
                                else if(employee.job == 1){
                                    branch.cashierBlackList.remove(employee);
                                }
                                else if(employee.job == 2){
                                    branch.cookBlacklist.remove(employee);
                                }
                            }

                            //Same thing mentioned before but for whitelists
                            else if(oldScore >= 10 && newScore < 10 && employee.job == 2){
                                branch.cookWhiteList.remove(employee);
                            }
                            else if(oldScore >= 3 && newScore < 3 && employee.job == 1){
                                branch.cashierWhiteList.remove(employee);
                            }
                            if(score > 0) {
                                branch.monthlyBranchBonus += score % 200;
                                branch.totalBranchBonus += score % 200;
                            }
                        }

                        //If any error occurs there is no such employee
                        catch (Exception e){
                            writer.write("There is no such employee.\n");
                            continue;
                        }

                        //If an employee got enough points for a promotion or lost enough points for a dismissal
                        //we add them to their relevant blacklists or whitelists
                        try {
                            if (employee.promotionPoint <= -5) {
                                if(employee.job == 0 && !branch.courierBlackList.contains(employee)){
                                    branch.courierBlackList.add(employee);
                                }
                                else if (employee.job == 1 && !branch.cashierBlackList.contains(employee)) {
                                    branch.cashierBlackList.add(employee);
                                } else if (employee.job == 2 && !branch.cookBlacklist.contains(employee)) {
                                    branch.cookBlacklist.add(branch.employees.get(info[2].strip()));
                                }
                            } else if (employee.promotionPoint >= 3 && employee.job == 1 && !branch.cashierWhiteList.contains(employee)) {
                                branch.cashierWhiteList.add(employee);
                            } else if (employee.promotionPoint >= 10 && employee.job == 2 && !branch.cookWhiteList.contains(employee)) {
                                branch.cookWhiteList.add(employee);
                            }
                        }

                        //We put this here also just in case
                        catch (Exception e){
                            writer.write("There is no such employee.\n");
                        }

                        //Checking if any promotions or dismissals happen after the performance update
                        branch.checkForPromotions();
                        branch.checkForDismissals();
                    }

                    else if(Objects.equals(operation, "ADD")){

                        //Inserting the employee directly. Further controls are inside the method.
                        company.insertToCompany(info);
                        Branch branch = company.cities.get(info[0].strip()).branches.get(info[1].strip());

                        //Checking if any promotions or dismissals happen after adding an employee
                        branch.checkForPromotions();
                        branch.checkForDismissals();
                    }
                    else if (Objects.equals(operation, "PRINT_MONTHLY_BONUSES")){

                        //Writing monthly bonuses to a file
                        int bonus = company.cities.get(info[0].strip()).branches.get(info[1].strip()).monthlyBranchBonus;
                        writer.write(String.format("Total bonuses for the %s branch this month are: %s\n",info[1].strip(),bonus));
                    }
                    else if(Objects.equals(operation, "PRINT_OVERALL_BONUSES")){

                        //Writing total bonuses to a file
                        int bonus = company.cities.get(info[0].strip()).branches.get(info[1].strip()).totalBranchBonus;
                        writer.write(String.format("Total bonuses for the %s branch are: %s\n",info[1].strip(),bonus));
                    }
                    else if(Objects.equals(operation, "PRINT_MANAGER")){
                        try{

                            //Writing the manager into the file
                            Employee manager = company.cities.get(info[0].strip()).branches.get(info[1].strip()).manager;
                            writer.write(String.format("Manager of the %s branch is %s.\n",info[1].strip(),manager.name.strip()));
                        }

                        //If any error occurs there is no such employee
                        catch (Exception e){
                            writer.write("There is no such employee.\n");
                        }
                    }
                }

                //If the line ends with a blank line it is the start of a new month, so we make all monthly bonuses for each branch 0
                //If it is a blank line it gets skipped automatically
                else if(data.endsWith(":")){
                    for(double i:company.cities.valList){
                        for(City city:company.cities.lists[(int) i]){
                           for(double j: city.branches.valList){
                               for(Branch branch:city.branches.lists[(int)j]){
                                   branch.monthlyBranchBonus = 0;
                               }
                           }
                        }
                    }
                }

            }
            fileScanner.close();
            writer.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Thy shall be flabbergasted by a pure beauty.");
            throw new FileNotFoundException();
        }
        catch (IOException e) {
            System.out.println("Thy shall be flabbergasted by a pure beauty.");
            throw new RuntimeException(e);
        }
    }
    }
