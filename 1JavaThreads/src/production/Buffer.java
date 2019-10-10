package production;

public class Buffer {

    private String message;
    private boolean empty; // true if producer produces message and consumer should wait
    private int messageNumber;
    private final int MESSAGES_NUMBER;

    public Buffer(int messagesNumber) {
        this.MESSAGES_NUMBER = messagesNumber;
        messageNumber = 1;
        empty = true;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public synchronized void put(String message, String threadName) {
        while(!empty) {
            try {
                if(this.messageNumber > this.MESSAGES_NUMBER) return;  // terminate if all messages has been put
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        empty = false;
        this.message = message;
        System.out.println("Thread " + threadName + " has put: " + message + " to buffer");
        this.messageNumber++;
        notifyAll();

    }

    public synchronized String take(String threadName) {
        while(empty) {
            try {
                if(this.messageNumber > this.MESSAGES_NUMBER) return message;  // terminate if all messages has been taken
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        empty = true;
        System.out.println("Thread " + threadName + " has taken: " + this.message + " from buffer");
        notifyAll();
        return message;
    }

}
