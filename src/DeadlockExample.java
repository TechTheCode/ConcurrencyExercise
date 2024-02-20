public class DeadlockExample {
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

        // Original Kaiden's Thread
        Thread kaiden = new Thread(() -> {
            synchronized (chocolateBar) {
                System.out.println("Kaiden is eating the chocolate bar");
                try { Thread.sleep(100);} catch (Exception e) {}
                System.out.println("Kaiden is waiting for Kevin to stop eating the fucking cookies");
                synchronized (cookieJar) {
                    System.out.println("Kaiden is eating the cookies");
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
