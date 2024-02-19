public class NumberSorterExample {
     static class sortThread extends Thread{
        private final int number;
        public sortThread(int number){
            this.number = number;
        }
        //Added override annotation from Saleem's feedback
        @Override
        public void run(){
            try {
                Thread.sleep(number* 100L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(number);
        }
    }
    public static void main(String[] args) {
        int[] numbers = {10, 9, 5, 1, 2};
        sortThread[] sorter = new sortThread[numbers.length];

        for (int i = 0; i < numbers.length; i++) {
            //Placed in an array so its joinable
            sorter[i] = new sortThread(numbers[i]);
            sorter[i].start();
        }
    }


}
