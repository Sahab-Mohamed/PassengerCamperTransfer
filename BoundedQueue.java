package queues;
import java.util.ArrayList;

import PassengerCamperTransferPackage.Camper;

public class BoundedQueue<T> implements QueueInterface<T>{

	private static ArrayList<Camper> boundedQueue = new ArrayList<Camper>();
	private int size;
	private int capacity;
	
	
	public BoundedQueue(int capacity) {
		this.capacity = capacity;
		this.size = 0;

		boundedQueue = new ArrayList<Camper>(capacity);
		
	}
	@Override
	public void enqueue(T element) throws QueueOverflowException {
		
		if (isFull()) {
			throw new QueueOverflowException();
		}
		else {
			boundedQueue.add((Camper) element);
			++size;
		}
		
	}
	@Override
    public T dequeue() throws QueueUnderflowException {
        if (isEmpty()) {
            throw new QueueUnderflowException();
        } else {
            @SuppressWarnings("unchecked")
			T temp = (T) boundedQueue.remove(0);
            --size;
            return temp;
        }
    }
	public T peek() throws QueueUnderflowException{
		if (isEmpty()) {
            throw new QueueUnderflowException();
        } else {
            @SuppressWarnings("unchecked")
			T temp = (T) boundedQueue.get(0);
            
            return temp;
        }
		
		
	}
	  
	@Override
    public boolean isFull() {
        return size == capacity;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }
	public int getCapacity() {
		return capacity;
	}
	
	  

}
