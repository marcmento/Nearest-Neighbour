package generation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGenerator {
  public static void main(String[] args) {
    write("Dataset1a.txt", 10);
    write("Dataset1b.txt", 10);
    write("Dataset1c.txt", 10);
    write("Dataset2a.txt", 1000);
    write("Dataset2b.txt", 1000);
    write("Dataset2c.txt", 1000);
    write("Dataset3a.txt", 10000);
    write("Dataset3b.txt", 10000);
    write("Dataset3c.txt", 10000);
  }

  public static void write(String filename, int datasize){
    try {
        FileWriter writer = new FileWriter(filename);
        for (int i = 0; i < datasize; i++) {
            String line;
            int int_random_cat;
            int upperbound;
            String[] cats = {"restaurant" , "hospital", "education"};

            Random rand = new Random(System.nanoTime());
        
            double xd = (rand.nextInt(100) - 50) + rand.nextDouble(); 
            double yd = (rand.nextInt(100) - 50) + rand.nextDouble();

            upperbound = 3;
            int_random_cat = rand.nextInt(upperbound); 

            line = "id" + Integer.toString(i) + " " + cats[int_random_cat] + " " + Double.toString(xd) + " " + Double.toString(yd) + " \n";
            writer.write(line);
        }
        writer.close();
    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
  }
}