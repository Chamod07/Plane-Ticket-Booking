import java.io.*;

public class Ticket {
    private char row;
    private int seat;
    private int price;
    private Person person;

    public Ticket(char row, int seat, int price, Person person) {
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.person = person;
    }

    public char getRow() {
        return row;
    }

    public void setRow(char row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void printTicketDetails() {
        System.out.printf("Row: %c, Seat: %d, Price: £%d, Passenger: %s\n", getRow(), getSeat(), getPrice(), person.printPersonDetails());  // code taken from https://stackoverflow.com/questions/7278128/formatting-string-in-java-using-return-string-format
    }

    public void save(Person person) {
        String fileName = getRow() + "" + getSeat() + ".txt";
        String directoryPath = "Tickets/Row " + getRow();
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(directoryPath + "/" + fileName);
            writer.write("Row: " + getRow() + "\n");
            writer.write("Seat: " + getSeat() + "\n");
            writer.write("Price: £" + getPrice() + "\n");
            writer.write("Passenger Details: " + person.printPersonDetails());
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed creating the ticket information file.");
        }
    }

    public void deleteTicketFile() {
        String fileName = getRow() + "" + getSeat() + ".txt";
        String filePath = "Tickets/Row " + getRow() + "/" + fileName;
        File file = new File(filePath);
        File directory = file.getParentFile();
        if (file.delete()) {
            System.out.println("Ticket information file deleted successfully");
            if (directory.isDirectory() && directory.list().length == 0) { // Delete directory if it is empty. Code from: https://stackoverflow.com/questions/5930087/how-to-check-if-a-directory-is-empty-in-java
                directory.delete();
            }
        } else System.out.println("Error deleting ticket information file.");
    }

}
