package queues;

public class QueueOverflowException extends Throwable  {
	
	public QueueOverflowException() {
		System.out.println("The train is full !");
	}
}
