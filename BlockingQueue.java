import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<T>{
    private Queue<T> queue = new LinkedList<>();
    private int capacity;

    private Lock lock = new ReentrantLock();

    private Condition notNull  = lock.newCondition();

    private Condition isEmpty = lock.newCondition();


    private BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    private void put( T t) throws  InterruptedException{
        lock.lock();
        try{
            while (queue.size() == capacity){
                notNull.await();
            }
            queue.add(t);
            isEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    private T take() throws  InterruptedException{
        lock.lock();

        try{
            while(queue.isEmpty()){
                isEmpty.await();
            }
            T item  = queue.remove();
            notNull.signal();
            return item;
        }finally {
            lock.unlock();
        }
    }


}
