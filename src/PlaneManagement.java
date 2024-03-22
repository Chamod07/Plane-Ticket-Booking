import java.util.*;

public class PlaneManagement {
    // Scanner object for user input
    public static Scanner scanner = new Scanner(System.in);

    // 2D array to represent the seating plan (A-D rows, seat numbers)
    private static final int[][] seats = new int[4][];

    // Array to store ticket information
    private static final Ticket[] tickets = new Ticket[52];

    public static void main(String[] args) {
        initializeSeats();  // Initialize seat arrays for each row (A-D)

        // Display a welcome message
        drawStars();
        System.out.println("*\t\t\t\t  March 22, 2024 \t\t\t\t *");
        System.out.println("*\t\tWELCOME TO THE PLANE MANAGEMENT APP\t\t *");
        System.out.println("*\t ** Developed by Chamod Karunathilake **\t *");
        System.out.println("*  Manage airplane seats, buy tickets, and more! *");
        displayMenu();  // Display the menu for user interaction

        // Loop until user exits
        boolean exit = false;
        while (!exit) {
            drawStars();
            // Prompt user for menu selection
            System.out.print("Please select an option from the menu: ");
            String option = scanner.next();
            switch (option) {
                case "0":
                    System.out.println("\t\t\t***** EXIT APP *****");
                    System.out.println("Thank You for using the application\nHave a safe flight!");
                    exit = true;
                    break;
                case "1":
                    System.out.println("\t\t\t***** BUY SEAT *****");
                    buy_seat();
                    break;
                case "2":
                    System.out.println("\t\t\t***** CANCEL SEAT *****");
                    cancel_seat();
                    break;
                case "3":
                    System.out.println("\t\t\t***** FIRST AVAILABLE SEAT *****");
                    find_first_available();
                    break;
                case "4":
                    System.out.println("\t\t\t\t ***** SEATING PLAN *****");
                    show_seating_plan();
                    break;
                case "5":
                    System.out.println("\t\t\t***** TICKET INFORMATION *****");
                    print_tickets_info();
                    break;
                case "6":
                    System.out.println("\t\t\t***** SEARCH TICKET *****");
                    search_ticket();
                    break;
                default:
                    System.out.println("Invalid option. Select the correct option from the menu!");
            }
        }
    }

    private static void initializeSeats() {
        // Allocate columns for each row's seat array (14 for A & D, 12 for B & C)
        seats[0] = new int[14];  // Row A
        seats[1] = new int[12];  // Row B
        seats[2] = new int[12];  // Row C
        seats[3] = new int[14];  // Row D
    }

    private static void displayMenu() {
        // Display menu options initially
        drawStars();
        System.out.println("*  \t\t\t\t  MENU OPTIONS  \t\t\t\t *");
        drawStars();
        System.out.println("\t 1) Buy a seat");
        System.out.println("\t 2) Cancel a seat");
        System.out.println("\t 3) Find first available seat");
        System.out.println("\t 4) Show seating plan");
        System.out.println("\t 5) Print tickets information and total sales");
        System.out.println("\t 6) Search tickets");
        System.out.println("\t 0) Quit");
    }

    private static void drawStars() {
        // Display stars for decoration
        for (int i = 0; i < 50; i++)
            System.out.print("*");
        System.out.println();
    }

    private static int[] getValidSeat() {
        // Get valid seat selection from the user and return an array containing row letter, seat number, row index, and seat index
        char rowLetter = 0;
        int rowIndex, seatNumber = 0, seatIndex;
        do {
            // Get row letter in uppercase and validate input
            System.out.print("Enter the Passenger's Seat Row (A, B, C or D): ");
            String input = scanner.next();
            if (input.length() != 1) {
                System.out.println("Please enter only one letter.");
                continue;
            }
            rowLetter = input.toUpperCase().charAt(0);
            if (rowLetter < 'A' || rowLetter > 'D') {
                System.out.println("Invalid row letter. Enter a letter from A, B, C or D. ");
            }
        } while (rowLetter < 'A' || rowLetter > 'D');

        rowIndex = rowLetter - 'A';  // Calculate the input row's index

        boolean correctSeat = false;

        do {
            // Get seat number and validate it based on the row
            System.out.print("Enter the Passenger's Seat Number (1-14 for rows A and D, 1-12 for rows B and C): ");
            try {
                seatNumber = scanner.nextInt();
                // Check if seat number is within valid range for specific row
                if (seatNumber < 1 || (rowIndex == 0 || rowIndex == 3) && seatNumber > seats[0].length || (rowIndex == 1 || rowIndex == 2) && seatNumber > seats[1].length) {
                    System.out.println("Seat number is out of range.");
                } else {
                    correctSeat = true;  // Exit the loop if seat number is valid
                }
            } catch (InputMismatchException e) {
                System.out.println("Seat number should be an integer. Please try again.");
                scanner.nextLine();  // clear the scanner buffer after exception
            }
        } while (!correctSeat);

        seatIndex = seatNumber - 1;  // Calculate the input seat's index

        // Create an array to store user inputs (row letter, seat number, row index, seat index)
        int[] userInputs = new int[4];
        userInputs[0] = rowLetter;
        userInputs[1] = seatNumber;
        userInputs[2] = rowIndex;
        userInputs[3] = seatIndex;

        return userInputs;
    }

    private static void buy_seat() {
        System.out.println("1. Personal Details");
        // Get passenger details and create a Person object
        Person person = getPersonDetails();

        System.out.println("2. Ticket Details");
        // Get valid seat selection from user and store details in array
        int[] seatDetails = getValidSeat();
        char rowLetter = (char) seatDetails[0];
        int seatNumber = seatDetails[1];
        int rowIndex = seatDetails[2];
        int seatIndex = seatDetails[3];

        System.out.println("3. Confirmation");
        // Check if seat is already booked
        if (seats[rowIndex][seatIndex] == 1) {
            buyAnotherSeat();  // If the seat is already taken, asks the user if they want to continue
        } else {
            int price = setPrice(seatNumber);  // Calculate ticket price based on seat number
            // Create Ticket object with seat and passenger details
            Ticket ticket = new Ticket(rowLetter, seatNumber, price, person);

            // Find first empty slot in tickets array and assign the ticket to the empty slot
            for (int i = 0; i < tickets.length; i++) {
                if (tickets[i] == null) {
                    tickets[i] = ticket;
                    // Mark the seat as booked in the seating plan
                    seats[rowIndex][seatIndex] = 1;
                    System.out.println("Seat " + rowLetter + seatNumber + " successfully purchased!");
                    ticket.save(person);  // Save ticket details to a file
                    printTicketAfterSale(ticket);  // Asks user to print the ticket details once purchased
                    break;
                }
            }
        }
    }

    private static Person getPersonDetails() {
        String name;
        String surname;
        String email;

        // Get name, surname, email with validation
        do {
            System.out.print("Enter your name (at least 3 letters and no numbers): ");
            name = scanner.next();
        } while (name.length() < 3 || !name.matches("[a-zA-Z]+"));  // Use regex for name validation. Reference: https://www.w3schools.com/java/java_regex.asp
        do {
            System.out.print("Enter your surname (at least 3 letters and no numbers): ");
            surname = scanner.next();
        } while (surname.length() < 3 || !surname.matches("[a-zA-Z]+"));  // Use regex for surname validation. Reference: https://www.w3schools.com/java/java_regex.asp
        do {
            System.out.print("Enter your email (should contain '@' and '.'): ");
            email = scanner.next();
        } while (!email.contains("@") || !email.contains("."));

        return new Person(name, surname, email);
    }

    private static int setPrice(int seatNumber) {
        // Price based on seat number
        if (seatNumber <= 5) {
            return 200;
        }
        else if (seatNumber <= 9) {
            return 150;
        }
        else {
            return 180;
        }
    }

    private static void buyAnotherSeat() {
        String answer;
        do {
            System.out.print("This seat is already sold. Would you like to search for another seat (Y/N)? ");
            answer = scanner.next().toUpperCase();
        } while (!answer.equals("Y") && !answer.equals("N"));
        if (answer.equals("Y")) {
            buy_seat();  // If user enters Y, buy_seat method is called again
        }
    }

    private static void printTicketAfterSale(Ticket ticket) {
        String answer;
        do {
            System.out.print("Do you want to print the ticket (Y/N)? ");
            answer = scanner.next().toUpperCase();
        } while (!answer.equals("Y") && !answer.equals("N"));
        if (answer.equals("Y")) {
            ticket.printTicketDetails();  // If user enters Y, print the ticket details
        }
    }

    private static void cancel_seat() {
        // Get valid seat selection from user
        int[] seatDetails = getValidSeat();
        char rowLetter = (char) seatDetails[0];
        int seatNumber = seatDetails[1];
        int rowIndex = seatDetails[2];
        int seatIndex = seatDetails[3];

        // Check if seat is already available
        if (seats[rowIndex][seatIndex] == 0) {
            System.out.println("This seat is already available.");
        } else if (cancelSeatConfirmation(seatDetails)) {  // Confirm cancellation with user
                // Find the ticket and cancel it
                for (int i = 0; i < tickets.length; i++) {
                    if ((tickets[i] != null) && (tickets[i].getRow() == seatDetails[0]) && (tickets[i].getSeat() == seatDetails[1])) {
                        tickets[i].deleteTicketFile();
                        tickets[i] = null;
                        seats[rowIndex][seatIndex] = 0;
                        System.out.println("Seat " + rowLetter + seatNumber + " has been successfully canceled.");
                        return;
                    }
                }
                System.out.println("No matching ticket found."); // Inform if no ticket found
            }
    }

    private static boolean cancelSeatConfirmation(int[] seatDetails) {
        String answer;
        do {
            System.out.print("Are you sure you want to cancel seat " + (char) seatDetails[0] + seatDetails[1] + "? (Y/N) ");
            answer = scanner.next().toUpperCase(); // Get uppercase character for confirmation
        } while (!answer.equals("Y") && !answer.equals("N"));  // Loop until valid input (Y or N)

        return answer.equals("Y");
    }

    private static void find_first_available() {
        // Loop through seats to find the first available seat
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                if (seats[i][j] == 0) {
                    System.out.println("First available seat: Row " + (char) (i + 'A') + ", Seat " + (j + 1));
                    return;  // Exit function after finding first available seat
                }
            }
        }
        System.out.println("No seats available.");
    }

    private static void show_seating_plan() {
        System.out.print("\t");
        // Print seat numbers as headers (1-14)
        for (int i = 1; i < 15; i++) {
            System.out.printf("%4d", i);  // Format headers to have 4 spaces between each other
        }
        System.out.println();
        System.out.print("\t   ");
        for (int i = 0; i < 27; i++) {
            System.out.print("_ ");
        }
        System.out.println();

        // Loop through rows and seats for displaying the plan
        char rowLetter = 'A';
        for (int[] seatRow : seats) {
            System.out.print(rowLetter+"  |");
            for (int seatStatus : seatRow) {
                System.out.print(seatStatus == 0 ? "   O" : "   X");  // Use ternary operator to display O if available and X if taken
            }
            rowLetter++;
            System.out.println();
        }
    }

    private static void print_tickets_info() {
        int totalSales = 0;
        for (Ticket ticket : tickets) {
            if (ticket != null) {
                ticket.printTicketDetails();  // method from Ticket class to print ticket details
                totalSales += ticket.getPrice();
            }
        }
        System.out.println("Total sales: £" + totalSales);
    }

    private static void search_ticket() {
        System.out.println("Search ticket by: ");
        System.out.println("1. Seat details");
        System.out.println("2. Passenger name");
        System.out.print("Select a search method: ");

        char searchMethod = scanner.next().charAt(0);

        switch (searchMethod) {
            case '1':
                searchBySeat();
                break;
            case '2':
                searchByUser();
                break;
            default:
                System.out.println("Invalid search method. Please try again.");
                search_ticket();  // Call search_ticket recursively for invalid input
                break;
        }
    }

    private static void searchBySeat() {
        // Search ticket by seat details (row letter, seat number)
        boolean ticketFound = false;
        int[] seatDetails = getValidSeat();
        char rowLetter = (char) seatDetails[0];
        int seatNumber = seatDetails[1];

        for (Ticket ticket : tickets) {
            // Check if ticket row and seat matches user input row and seat
            if (ticket != null && ticket.getRow() == rowLetter && ticket.getSeat() == seatNumber) {
                ticket.printTicketDetails();  // Print the ticket details if the ticket is already booked
                ticketFound = true;
                break;
            }
        }
        // Display seat is available if no tickets found
        if (!ticketFound) {
            System.out.println("Seat " + rowLetter + seatNumber + " is available.");
        }
    }

    private static void searchByUser() {
        // Search ticket by user details (name, surname, email)
        boolean ticketFound = false;
        Person person = getPersonDetails();
        for (Ticket ticket : tickets) {
            if (ticket != null)
                // Individually check for person details using Getters from Person class
                if (Objects.equals(ticket.getPerson().getName(), person.getName()) && Objects.equals(ticket.getPerson().getSurname(), person.getSurname()) && Objects.equals(ticket.getPerson().getEmail(), person.getEmail())) {
                    ticket.printTicketDetails();
                    ticketFound = true;
                    break;
                }
        }
        // If no tickets found, display message found for passenger
        if (!ticketFound) {
            System.out.println("No tickets found for passenger.");
        }
    }
}
