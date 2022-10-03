package generation;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TestGenerator2 {
  public static void main(String[] args) {
    write("S2Test3.in", 20);
    write("S2Test4.in", 100);
  }

  public static void write(String filename, int datasize){
    try {
        FileWriter writer = new FileWriter(filename);
        for (int i = 0; i < datasize; i++) {
          String line;
          int int_random_cat;
          String[] cats = {"restaurant" , "hospital", "education"};

          Random rand = new Random(System.nanoTime());
      
          double xd = (rand.nextInt(100) - 50) + rand.nextDouble(); 
          double yd = (rand.nextInt(100) - 50) + rand.nextDouble();
        
          int_random_cat = rand.nextInt(3); 

          line = "A " + " id" + Integer.toString(i) + " " + cats[int_random_cat] + " " + Double.toString(xd) + " " + Double.toString(yd) + " \n";
          writer.write(line);
          line = "D " + " id" + Integer.toString(i) + " " + cats[int_random_cat] + " " + Double.toString(xd) + " " + Double.toString(yd) + " \n";
          writer.write(line);
      }
        for (int i = 0; i < 40; i++) {
            String line;
            int int_random_cat;
            int k = 15;
            String[] cats = {"restaurant" , "hospital", "education"};

            Random rand = new Random(System.nanoTime());
        
            double xd = (rand.nextInt(100) - 50) + rand.nextDouble(); 
            double yd = (rand.nextInt(100) - 50) + rand.nextDouble();
          
            int_random_cat = rand.nextInt(3); 

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