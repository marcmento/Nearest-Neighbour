package generation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TestGenerator {
  public static void main(String[] args) {
    write("S1Test1.in", 20);
    write("S1Test2.in", 100);
  }

  public static void write(String filename, int datasize){
    try {
        FileWriter writer = new FileWriter(filename);
        for (int i = 0; i < datasize; i++) {
            String line;
            int int_random_cat;
            int k;
            String[] cats = {"restaurant" , "hospital", "education"};

            Random rand = new Random(System.nanoTime());
        
            double xd = (rand.nextInt(100) - 50) + rand.nextDouble(); 
            double yd = (rand.nextInt(100) - 50) + rand.nextDouble();
          
            int_random_cat = rand.nextInt(3); 
            k =50;

            line = "S " + cats[int_random_cat] + " " + Double.toString(xd) + " " + Double.toString(yd) + " " + k + " \n";
            writer.write(line);
        }
        writer.close();
    } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
    }
  }
}