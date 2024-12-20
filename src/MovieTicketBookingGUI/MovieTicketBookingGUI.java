package MovieTicketBookingGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// --- Observer Pattern ---
interface TicketObserver {
    void update(String ticketDetails);
}

class TicketBookingSystemObservable {
    private List<TicketObserver> observers = new ArrayList<>();
    private String ticketDetails;

    public void addObserver(TicketObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TicketObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (TicketObserver observer : observers) {
            observer.update(ticketDetails);
        }
    }

    public void bookTicket(String ticketDetails) {
        this.ticketDetails = ticketDetails;
        notifyObservers();
    }
}

// --- Factory Pattern for Movie Types ---
abstract class Movie {
    String genre;
}

class ActionMovie extends Movie {
    public ActionMovie() {
        this.genre = "Action";
    }
}

class ComedyMovie extends Movie {
    public ComedyMovie() {
        this.genre = "Comedy";
    }
}

class DramaMovie extends Movie {
    public DramaMovie() {
        this.genre = "Drama";
    }
}

class MagicMovie extends Movie {
    public MagicMovie() {
        this.genre = "Magic";
    }
}

class MovieFactory {
    public static Movie createMovie(String genre) {
        switch (genre) {
            case "Action":
                return new ActionMovie();
            case "Comedy":
                return new ComedyMovie();
            case "Drama":
                return new DramaMovie();
            case "Magic":
                return new MagicMovie();
            default:
                return null;
        }
    }
}

// --- Factory Pattern for Theater Types ---
abstract class TheaterType {
    String name;

    public String getName() {
        return name;
    }
}

class CinemaHall extends TheaterType {
    public CinemaHall() {
        this.name = "Cinema Hall";
    }
}

class CinemaDriver extends TheaterType {
    public CinemaDriver() {
        this.name = "Cinema Driver";
    }
}

class IMAX extends TheaterType {
    public IMAX() {
        this.name = "IMAX";
    }
}

class TheaterFactory {
    public static TheaterType createTheater(String type) {
        switch (type) {
            case "Cinema Hall":
                return new CinemaHall();
            case "IMAX":
                return new IMAX();
            case "Cinema Driver":
                return new CinemaDriver();
            default:
                return null;
        }
    }
}

// --- Singleton for Ticket Booking System ---
class TicketBookingSystem {
    private static TicketBookingSystem instance;

    private TicketBookingSystem() {}

    public static TicketBookingSystem getInstance() {
        if (instance == null) {
            instance = new TicketBookingSystem();
        }
        return instance;
    }

    public void bookTicket(String movieName, String theaterType, String genre, int seats) {
        JOptionPane.showMessageDialog(null, "Booking confirmed for " + movieName + " at " + theaterType + ".\nGenre: " + genre + ", Seats: " + seats);
    }
}

// --- Prototype Pattern ---
class TicketPrototype implements Cloneable {
    private String movieName;
    private String theaterType;
    private String genre;
    private int seats;
    private double price;

    public TicketPrototype(String movieName, String theaterType, String genre, int seats, double price) {
        this.movieName = movieName;
        this.theaterType = theaterType;
        this.genre = genre;
        this.seats = seats;
        this.price = price;
    }

    public String getDetails() {
        return "Movie: " + movieName + "\nTheater: " + theaterType + "\nGenre: " + genre + "\nSeats: " + seats + "\nPrice: $" + price;
    }

    public TicketPrototype clone() {
        try {
            return (TicketPrototype) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// --- Decorator Pattern ---
interface Ticket {
    String getDetails();
    double getPrice();
}

class BasicTicket implements Ticket {
    private String movieName;
    private String theaterType;
    private String genre;
    private int seats;
    private double basePrice;

    public BasicTicket(String movieName, String theaterType, String genre, int seats) {
        this.movieName = movieName;
        this.theaterType = theaterType;
        this.genre = genre;
        this.seats = seats;
        this.basePrice = seats * 10.0; // Example base price calculation
    }

    @Override
    public String getDetails() {
        return "Movie: " + movieName + "\nTheater: " + theaterType + "\nGenre: " + genre + "\nSeats: " + seats;
    }

    @Override
    public double getPrice() {
        return basePrice;
    }
}

abstract class TicketDecorator implements Ticket {
    protected Ticket decoratedTicket;

    public TicketDecorator(Ticket decoratedTicket) {
        this.decoratedTicket = decoratedTicket;
    }

    @Override
    public String getDetails() {
        return decoratedTicket.getDetails();
    }

    @Override
    public double getPrice() {
        return decoratedTicket.getPrice();
    }
}

class WindowSeatDecorator extends TicketDecorator {
    public WindowSeatDecorator(Ticket decoratedTicket) {
        super(decoratedTicket);
    }

    @Override
    public String getDetails() {
        return super.getDetails() + "\nFeature: Window Seat";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + 5.0; // Additional cost for a window seat
    }
}

class MealIncludedDecorator extends TicketDecorator {
    public MealIncludedDecorator(Ticket decoratedTicket) {
        super(decoratedTicket);
    }

    @Override
    public String getDetails() {
        return super.getDetails() + "\nFeature: Meal Included";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + 15.0; // Additional cost for meal inclusion
    }
}

// --- Builder Pattern for Ticket Creation ---
class TicketBuilder {
    private String movieName;
    private String theaterType;
    private String genre;
    private int seats;
    private boolean windowSeat;
    private boolean mealIncluded;

    public TicketBuilder(String movieName, String theaterType, String genre, int seats) {
        this.movieName = movieName;
        this.theaterType = theaterType;
        this.genre = genre;
        this.seats = seats;
    }

    public TicketBuilder setWindowSeat(boolean windowSeat) {
        this.windowSeat = windowSeat;
        return this;
    }

    public TicketBuilder setMealIncluded(boolean mealIncluded) {
        this.mealIncluded = mealIncluded;
        return this;
    }

    public Ticket build() {
        Ticket ticket = new BasicTicket(movieName, theaterType, genre, seats);
        if (windowSeat) {
            ticket = new WindowSeatDecorator(ticket);
        }
        if (mealIncluded) {
            ticket = new MealIncludedDecorator(ticket);
        }
        return ticket;
    }
}

// --- GUI Application ---
public class MovieTicketBookingGUI implements TicketObserver {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Movie Ticket Booking System");
        frame.setSize(600, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(20, 10));

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        JLabel lblMovieName = new JLabel("Movie Name:");
        JTextField txtMovieName = new JTextField();

        JLabel lblTheaterType = new JLabel("Theater Type:");
        JComboBox<String> cbTheaterType = new JComboBox<>(new String[]{"Cinema Hall", "IMAX", "Cinema Driver"});

        JLabel lblGenre = new JLabel("Genre:");
        JComboBox<String> cbGenre = new JComboBox<>(new String[]{"Action", "Comedy", "Drama", "Magic"});

        JLabel lblSeats = new JLabel("Number of Seats:");
        JTextField txtSeats = new JTextField();

        JCheckBox chkWindowSeat = new JCheckBox("Window Seat");
        JCheckBox chkMealIncluded = new JCheckBox("Meal Included");

        inputPanel.add(lblMovieName);
        inputPanel.add(txtMovieName);
        inputPanel.add(lblTheaterType);
        inputPanel.add(cbTheaterType);
        inputPanel.add(lblGenre);
        inputPanel.add(cbGenre);
        inputPanel.add(lblSeats);
        inputPanel.add(txtSeats);
        inputPanel.add(chkWindowSeat);
        inputPanel.add(chkMealIncluded);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnBook = new JButton("Book Ticket");
        JButton btnBuild = new JButton("Build Ticket");
        JButton btnClone = new JButton("Clone Ticket");
        JButton btnClear = new JButton("Clear");

        buttonPanel.add(btnBook);
        buttonPanel.add(btnBuild);
        buttonPanel.add(btnClone);
        buttonPanel.add(btnClear);

        JPanel outputPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextArea ticketDetails = new JTextArea();
        ticketDetails.setEditable(false);
        ticketDetails.setBorder(BorderFactory.createTitledBorder("Ticket Details"));

        JTextArea clonedTicketDetails = new JTextArea();
        clonedTicketDetails.setEditable(false);
        clonedTicketDetails.setBorder(BorderFactory.createTitledBorder("Cloned Ticket Details"));

        outputPanel.add(new JScrollPane(ticketDetails));
        outputPanel.add(new JScrollPane(clonedTicketDetails));

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(outputPanel, BorderLayout.SOUTH);

        TicketPrototype[] currentTicket = new TicketPrototype[1];
        TicketBookingSystemObservable observable = new TicketBookingSystemObservable();
        observable.addObserver(new MovieTicketBookingGUI());

        btnBook.addActionListener(e -> {
            try {
                String movieName = txtMovieName.getText();
                String theaterType = cbTheaterType.getSelectedItem().toString();
                String genre = cbGenre.getSelectedItem().toString();
                int seats = Integer.parseInt(txtSeats.getText());

                // Using the TicketBuilder to create a ticket
                TicketBuilder ticketBuilder = new TicketBuilder(movieName, theaterType, genre, seats);
                if (chkWindowSeat.isSelected()) {
                    ticketBuilder.setWindowSeat(true);
                }
                if (chkMealIncluded.isSelected()) {
                    ticketBuilder.setMealIncluded(true);
                }

                Ticket ticket = ticketBuilder.build();  // Build the ticket

                TheaterType theater = TheaterFactory.createTheater(theaterType);
                TicketBookingSystem.getInstance().bookTicket(movieName, theater.getName(), genre, seats);

                ticketDetails.setText(ticket.getDetails() + "\nTotal Price: $" + ticket.getPrice());

                currentTicket[0] = new TicketPrototype(movieName, theater.getName(), genre, seats, ticket.getPrice());
                observable.bookTicket(currentTicket[0].getDetails());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for seats.");
            }
        });

        btnBuild.addActionListener(e -> {
            if (currentTicket[0] != null) {
                ticketDetails.setText(currentTicket[0].getDetails());
            } else {
                JOptionPane.showMessageDialog(frame, "No ticket booked yet!");
            }
        });

        btnClone.addActionListener(e -> {
            if (currentTicket[0] != null) {
                TicketPrototype clonedTicket = currentTicket[0].clone();
                clonedTicketDetails.setText(clonedTicket.getDetails());
            } else {
                JOptionPane.showMessageDialog(frame, "No ticket to clone!");
            }
        });

        btnClear.addActionListener(e -> {
            txtMovieName.setText("");
            txtSeats.setText("");
            cbTheaterType.setSelectedIndex(0);
            cbGenre.setSelectedIndex(0);
            chkWindowSeat.setSelected(false);
            chkMealIncluded.setSelected(false);
            ticketDetails.setText("");
            clonedTicketDetails.setText("");
        });

        frame.setVisible(true);
    }

    @Override
    public void update(String ticketDetails) {
        JOptionPane.showMessageDialog(null, "Ticket details updated:\n" + ticketDetails);
    }
}
