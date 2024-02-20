import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TryLockExample {
    public static void main(String[] args) throws InterruptedException {
        final Lock cookieJar = new ReentrantLock();
        final Lock chocolateBar = new ReentrantLock();

        // Kevin's Thread
        /*
        Acquires the cookieJar lock.
        Simulates eating cookies.
        Attempts to acquire the chocolateBar lock without waiting (using tryLock()).
        If successful, simulates eating the chocolate and then releases the chocolateBar lock.
        Finally, releases the cookieJar lock.
         */
        Thread kevin = new Thread(() -> {
            cookieJar.lock();
            try {
                System.out.println("Kevin is eating the cookies");
                try { Thread.sleep(100);} catch (Exception e) {}
                System.out.println("Kevin is waiting for Kaiden to stop hogging the fucking chocolate");
                if (chocolateBar.tryLock()) {
                    try {
                        System.out.println("Kevin is eating the chocolate");
                    } finally {
                        chocolateBar.unlock();
                    }
                }
            } finally {
                cookieJar.unlock();
            }
        });

        // Kaiden's Thread
        /*
        Acquires the chocolateBar lock.
        Simulates eating the chocolate bar.
        Attempts to acquire the cookieJar lock without waiting.
        If successful, simulates eating the cookies and then releases the cookieJar lock.
        Finally, releases the chocolateBar lock.
         */
        Thread kaiden = new Thread(() -> {
            chocolateBar.lock();
            try {
                System.out.println("Kaiden is eating the chocolate bar");
                try { Thread.sleep(100);} catch (Exception e) {}
                System.out.println("Kaiden is waiting for Kevin to stop eating the fucking cookies");
                if (cookieJar.tryLock()) {
                    try {
                        System.out.println("Kaiden is eating the cookies");
                    } finally {
                        cookieJar.unlock();
                    }
                }
            } finally {
                chocolateBar.unlock();
            }
        });

        kevin.start();
        kaiden.start();

        //Test using join() doesn't work cause it doesn't directly affect the locks
        kevin.join();
        kaiden.join();
    }
}
