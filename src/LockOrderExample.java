public class LockOrderExample {
    public static void main(String[] args) throws InterruptedException {
        final Object cookieJar = new Object();
        final Object chocolateBar = new Object();

        // Original Kevin's Thread
        Thread kevin = new Thread(() -> {
            synchronized (cookieJar) {
                System.out.println("Kevin is eating the cookies");
                try { Thread.sleep(100);} catch (Exception e) {}
                System.out.println("Kevin is waiting for Kaiden to stop hogging the fucking chocolate");
                synchronized (chocolateBar) {
                    System.out.println("Kevin is eating the chocolate");
                }
            }
        });

        // Ordered Kaiden's Thread to fix the deadlock (Logic change)
        Thread kaiden = new Thread(() -> {
            synchronized (cookieJar) {
                System.out.println("Kaiden is eating the cookies");
                try { Thread.sleep(100);} catch (Exception e) {}
                System.out.println("Kaiden is waiting for Kevin to stop hogging the fucking chocolate");
                synchronized (chocolateBar) {
                    System.out.println("Kaiden is eating the chocolate bar");
                }
            }
        });

        kevin.start();
        kaiden.start();

        //Test using join() doesn't work cause it doesn't directly affect the locks
        kevin.join();
        kaiden.join();
    }
}
