import java.util.*;

public class PlaneManagement {
    public static final Scanner scanner = new Scanner(System.in);

    private static int[][] Seats = new int[4][];

    private static Ticket[] tickets = new Ticket[52];

    public static void main(String[] args) {
        Seats[0] = new int[14];  // initialize Row A
        Seats[1] = new int[12];  // initialize Row B
        Seats[2] = new int[12];  // initialize Row C
        Seats[3] = new int[14];  // initialize Row D

        int ticketCount = 0;

        display_menu();

        boolean loopControl = false;
        while (!loopControl) {
            draw_stars();
            System.out.print("Please select an option from the menu: ");
            char Option = scanner.next().charAt(0);
            switch (Option) {
                case '0':
                    System.out.println("\t\t\t*\t EXIT APP\t*");
                    System.out.println("Thank You for using the application\nHave a safe flight!");
                    loopControl = true;
                    break;
                case '1':
                    System.out.println("\t\t\t***** BUY SEAT *****");
                    ticketCount = buy_seat(ticketCount);
                    break;
                case '2':
                    System.out.println("\t\t\t***** CANCEL SEAT *****");
                    ticketCount = cancel_seat(ticketCount);
                    break;
                case '3':
                    System.out.println("\t\t\t***** FIRST AVAILABLE SEAT *****");
                    find_first_available();
                    break;
                case '4':
                    System.out.println("\t\t\t***** SEATING PLAN *****");
                    show_seating_plan();
                    break;
                case '5':
                    System.out.println("\t\t\t***** TICKET INFORMATION *****");
                    print_tickets_info(ticketCount);
                    break;
                case '6':
                    search_ticket();
                    break;
                default:
                    System.out.println("Invalid option. Select the correct option from the menu!");
            }
        }
    }

    private static void display_menu() {
        System.out.println("Welcome To The Plane Management Application");
        draw_stars();
        System.out.println("*  \t\t\t\t  MENU OPTIONS  \t\t\t\t *");
        draw_stars();
        System.out.println("\t 1) Buy a seat");
        System.out.println("\t 2) Cancel a seat");
        System.out.println("\t 3) Find first available seat");
        System.out.println("\t 4) Show seating plan");
        System.out.println("\t 5) Print tickets information and total sales");
        System.out.println("\t 6) Search tickets");
        System.out.println("\t 0) Quit");
    }

    private static void draw_stars() {
        for (int i = 0; i < 50; i++)
            System.out.print("*");
        System.out.println();
    }

    private static int[] get_valid_seat() {
        char RowLetter;
        do {
            System.out.print("Enter the Row Letter (A, B, C, D): ");
            RowLetter = scanner.next().charAt(0);
            RowLetter = Character.toUpperCase(RowLetter);
            if (RowLetter < 'A' || RowLetter > 'D') {
                System.out.println("Invalid row letter. Enter a letter between A and D. ");
            }
        } while (RowLetter < 'A' || RowLetter > 'D');

        int SeatNumber = 0;
        int RowIndex = RowLetter - 'A';
        boolean correctSeat = false;
        do {
            try {
                System.out.print("Enter the Seat Number (1-14 for A & D, 1-12 for B & C): ");
                SeatNumber = scanner.nextInt();
                if (SeatNumber < 1 || (RowIndex == 0 || RowIndex == 3) && SeatNumber > Seats[0].length || (RowIndex == 1 || RowIndex == 2) && SeatNumber > Seats[1].length) {
                    System.out.println("Invalid seat number.");
                } else correctSeat = true;
            } catch (InputMismatchException e) {
                System.out.println("Seat number should be an integer.");
                scanner.nextLine();  // to clear the buffer
            }
        } while (!correctSeat);

        int SeatIndex = SeatNumber - 1;
        int[] userInputs = new int[4];
        userInputs[0] = RowLetter;  // assign user input row letter to an array index 0
        userInputs[1] = SeatNumber;    // assign user input seat number to an array index 1
        userInputs[2] = RowIndex;  // assign row index to an array index 2
        userInputs[3] = SeatIndex;  // assign seat index to the array index 3
        return userInputs;
    }

    private static int buy_seat(int ticketCount) {
        Person person = getPersonDetails();  // create a new person object

        int[] seatDetails = get_valid_seat();
        if (Seats[seatDetails[2]][seatDetails[3]] == 1) {
            System.out.println("Seat already sold.");
        } else {
            int price = setPrice(seatDetails[1]);
            Ticket ticket = new Ticket((char) seatDetails[0], seatDetails[1], price, person);
            tickets[ticketCount] = ticket;
            ticketCount++;
            Seats[seatDetails[2]][seatDetails[3]] = 1;
            System.out.println("Seat " + (char) seatDetails[0] + seatDetails [1] + " successfully purchased!");
        }
        return ticketCount;
    }

    private static Person getPersonDetails() {
        System.out.print("Enter your name: ");
        String name = scanner.next();
        System.out.print("Enter your surname: ");
        String surname = scanner.next();
        System.out.print("Enter your email: ");
        String email = scanner.next();
        return new Person(name, surname, email);
    }

    private static int setPrice(int seatNumber) {
        if (seatNumber <= 5) return 200;
        else if (seatNumber <= 9) return 150;
        else return 180;
    }

    private static int cancel_seat(int ticketCount) {
        int[] seatDetails = get_valid_seat();
        if (Seats[seatDetails[2]][seatDetails[3]] == 0) {
            System.out.println("Seat already available.");
        } else {
            for (int i = 0; i < ticketCount; i++){
                if (tickets[i].getRow() == seatDetails[0] && tickets[i].getSeat() == seatDetails[1]) {
                    for (int j = i; j < tickets.length - 1; j++) {
                        tickets[j] = tickets[j+1];
                    }
                    Seats[seatDetails[2]][seatDetails[3]] = 0;
                    ticketCount--;
                    System.out.println("Seat " + (char) seatDetails[0] + seatDetails[1] + " successfully made available!");
                    break;
                }
            }
        }
        return ticketCount;
    }

    private static void find_first_available() {
        for (int i = 0; i < Seats.length; i++) {
            for (int j = 0; j < Seats[i].length; j++) {
                if (Seats[i][j] == 0) {
                    System.out.println("First available seat: Row " + (char) (i + 'A') + ", Seat " + (j + 1));
                    return;  // Exit function after finding first available seat
                }
            }
        }
        System.out.println("No seats available.");
    }

    private static void show_seating_plan() {
        for (int[] seat : Seats) {
            for (int i : seat) {
                if (i == 0) {
                    System.out.print("O ");
                } else
                    System.out.print("X ");
            }
            System.out.println();
        }
    }

    private static void print_tickets_info(int ticketCount) {
        int totalSales = 0;
        if(ticketCount != 0) {
            for (int i = 0; i < ticketCount; i++) {
                if (tickets[i] != null) {
                    tickets[i].printTicketDetails();
                    totalSales += tickets[i].getPrice();
                }
            }
            System.out.println("Total sales: £" + totalSales);
        } else System.out.println("No tickets have been sold yet.");
    }

    private static void search_ticket() {

    }
}
