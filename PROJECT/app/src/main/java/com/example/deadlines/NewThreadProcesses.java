package com.example.deadlines;

public class NewThreadProcesses {
    private static Thread thread = new Thread();
    public static void launch(Runnable runnable){
        //исполнение Runnable в другом потоке
        thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.setPriority(10);
        thread.start();
        while (thread.isAlive()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}

