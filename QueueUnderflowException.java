package queues;

public class QueueUnderflowException extends Exception {
	public QueueUnderflowException() {
	    super("The train is empty !");
	}

}
