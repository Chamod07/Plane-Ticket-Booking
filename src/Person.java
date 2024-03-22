public class Person {
    private String name;
    private String surname;
    private String email;

    // Constructor to initialize Person object
    public Person(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    // Getters and Setters for Person object
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Method to return person details
    public String printPersonDetails() {
        return String.format("%s %s (%s)", getName(), getSurname(), getEmail());  // code taken from https://stackoverflow.com/questions/7278128/formatting-string-in-java-using-return-string-format
    }
}
