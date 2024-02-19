import java.util.*;

public class CinemaSimulator {
    private final Map<String, String> seats;

    public CinemaSimulator(int rows, int columns) {
        seats = new HashMap<>();
        for (char row = 'A'; row < 'A' + rows; row++) {
            for (int column = 1; column <= columns; column++) {
                this.seats.put(row + "" + column, null); // Null=No booking
            }
        }
    }
    public synchronized String bookSeat(String seat) {
        try {
            // Artificial delay
            Thread.sleep(new Random().nextInt(300));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Booking interrupted";
        }

        // Check if the seat is available
        if (seats.containsKey(seat) && seats.get(seat) == null) {
            seats.put(seat, Thread.currentThread().getName());
            return "Ticket issued for seat " + seat;
        } else {
            return "Seat " + seat + " is not available";
        }
    }
    public synchronized int getAvailableSeatsCount() {
        return (int) seats.values().stream().filter(Objects::isNull).count();
    }
    public synchronized String getBookingDetails() {
        StringBuilder details = new StringBuilder();
        for (Map.Entry<String, String> seat : seats.entrySet()) {
            if (seat.getValue() == null) {
                details.append(seat.getKey()).append(": Available\n");
            } else {
                details.append(seat.getKey()).append(": Booked by ").append(seat.getValue()).append("\n");
            }
        }
        return details.toString();
    }
    public static void main(String[] args) throws InterruptedException {
        CinemaSimulator barbie = new CinemaSimulator(13, 15);
        CinemaSimulator oppenheimer = new CinemaSimulator(13, 15);
        List<Thread> threads = new ArrayList<>();
        Random random = new Random();

        System.out.println("Barbie - Available Seats: " + barbie.getAvailableSeatsCount());
        System.out.println("Oppenheimer - Available Seats: " + oppenheimer.getAvailableSeatsCount());

        //Test case for booking the same seat
        /*
        //Booking the same seat (in this case A1)
        for (int i = 0; i<2; i++) {
            final Runnable taskBarbie = () -> {
                //String containing seat number
                String seat = "A1";
                String result = barbie.bookSeat(seat);
                System.out.println(Thread.currentThread().getName() + " at Barbie: " + result);
            };
            Thread thread = new Thread(taskBarbie);
            //thread.setName("Customer-" + i);
            thread.start();
            threads.add(thread);
        }
         */

        //Randomly assigning seats
        for (int i = 0; i < 30; i++) {
            final String seatBarbie = (char) ('A' + random.nextInt(13)) + "" + (1 + random.nextInt(15));
            final String seatOppenheimer = (char) ('A' + random.nextInt(13)) + "" + (1 + random.nextInt(15));

            Runnable taskBarbie = () -> {
                String result = barbie.bookSeat(seatBarbie);
                System.out.println(Thread.currentThread().getName() + " at Barbie: " + result);
            };
            Runnable taskOppenheimer = () -> {
                String result = oppenheimer.bookSeat(seatOppenheimer);
                System.out.println(Thread.currentThread().getName() + " at Oppenheimer: " + result);
            };
            Thread threadBarbie = new Thread(taskBarbie);
            Thread threadOppenheimer = new Thread(taskOppenheimer);
            threadBarbie.start();
            threads.add(threadBarbie);
            threadOppenheimer.start();
            threads.add(threadOppenheimer);
        }


        //Join threads so I can check the number of seats still available
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Barbie - Available Seats: " + barbie.getAvailableSeatsCount());
        System.out.println("Oppenheimer - Available Seats:" + oppenheimer.getAvailableSeatsCount());
        System.out.println();
        System.out.println("Oppenheimer - Booking Details:\n" + oppenheimer.getBookingDetails());

    }
}
