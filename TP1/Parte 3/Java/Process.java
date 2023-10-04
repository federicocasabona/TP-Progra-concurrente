import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class ProducerConsumerExample
{
    public static void main(String[] args)
    {
      if (args.length != 1)
      {
          System.out.println("Ejemplo de uso: java ProducerConsumerExample <numOfRandomNumbers>");
          return;
      }

        int numOfRandomNumbers = Integer.parseInt(args[0]);
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(numOfRandomNumbers); // Create a blocking queue with a capacity of 10

        Thread producerThread = createProducerThread(queue,numOfRandomNumbers);
        Thread consumerThread = createConsumerThread(queue,numOfRandomNumbers);

        // Start both threads
        producerThread.start();
        consumerThread.start();
    }

    public static int generateRandomNumber()
    {
        return (int) (Math.random() * 100);
    }

    public static Thread createProducerThread(ArrayBlockingQueue<Integer> queue, int numOfRandomNumbers)
    {
        return new Thread(() ->
        {
            try
            {
                for (int i = 0; i < numOfRandomNumbers; i++)
                {
                    int randomNum = generateRandomNumber();
                    System.out.println("Producing: " + randomNum);
                    queue.put(randomNum);
                }
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        });
    }

    public static Thread createConsumerThread(ArrayBlockingQueue<Integer> queue, int numOfRandomNumbers)
    {
        return new Thread(() ->
        {
            Map<Integer, Integer> consumedNumbers = new HashMap<>(); // Map to store frequencies
            int sum = 0;
            try
            {
                for (int i = 0; i < numOfRandomNumbers; i++)
                {
                    int num = queue.take();
                    sum += num;
                    consumedNumbers.put(num, consumedNumbers.getOrDefault(num, 0) + 1);
                }
                double average = (double) sum / numOfRandomNumbers;
                int maximum = Collections.max(consumedNumbers.keySet());
                int minimum = Collections.min(consumedNumbers.keySet());
                List<Integer> mostFrequentValues = findMostFrequentValues(consumedNumbers);

                System.out.println("Promedio: " + average);
                System.out.println("Maximo: " + maximum);
                System.out.println("Minimo: " + minimum);
                System.out.println("Suma: " + sum);
                System.out.println("El mas frecuente: " + mostFrequentValues);

            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        });
    }

    public static List<Integer> findMostFrequentValues(Map<Integer, Integer> frequencyMap)
    {
        List<Integer> mostFrequentValues = new ArrayList<>();
        int maxFrequency = 0;

        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet())
        {
            int frequency = entry.getValue();

            if (frequency > maxFrequency)
            {
                mostFrequentValues.clear();
                mostFrequentValues.add(entry.getKey());
                maxFrequency = frequency;
            }
            else if (frequency == maxFrequency)
            {
                mostFrequentValues.add(entry.getKey());
            }
        }

        return mostFrequentValues;
    }
}
