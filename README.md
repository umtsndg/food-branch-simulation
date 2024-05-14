This code simulates a food branch.

It takes input from two files where the first one being inital state and the second one being changes and does operations according to changes.

Operations the code can do:
1)Add an employee to a branch with command:

ADD: city_name, branch_name, employee_name employee_surname position

Example: ADD: Diyarbakir, Dicle, Dayehatun Yildirim, COOK

2)Update and employees performance ratings with command:

PERFORMANCE_UPDATE: city_name, branch_name, employee_name employee_surname, points

Example: PERFORMANCE_UPDATE: Diyarbakir, Dicle, Acer Aksoy, 1803

3)Make an employee leave with command:

LEAVE: city_name, branch_name, employee_name employee_surname

Example: LEAVE: Diyarbakir, Dicle, Bergen Uzun

4)Can print the manager of a branch with the command:

PRINT_MANAGER: city_name, branch_name

Example: PRINT_MANAGER: Diyarbakir, Dicle

5)Can print monthly bonuses of a branch or can print the total bonuses of a branch. (Bonuses are given to employees with respect to their performances)

PRINT_MONTHLY_BONUSES: city_name, branch_name

PRINT_OVERALL_BONUSES: city_name, branch_name

Examples:

PRINT_MONTHLY_BONUSES: city_name, branch_name

PRINT_OVERALL_BONUSES: city_name, branch_name

6)Employees are promoted or fired automatically according to their promotion points after each performance update and add operation. Sample input and output files are inside the repo.





