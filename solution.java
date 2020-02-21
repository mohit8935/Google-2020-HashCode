import java.io.File; // Import the File class
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;

class Books{
    int score;
    int id;
    public Books(int score, int id){
        this.score = score;
        this.id = id;
    }
}
class Result{
  int libId;
  List<Integer> libBooks;
  public Result(int id, List<Integer> books){
    this.libId = id;
    this.libBooks = books;
  }
}

class Library{
    int id;
    PriorityQueue<Books> bookList;
    int process;
    int ship;
    float score = 0;
    public Library(int id, PriorityQueue<Books> books, int process, int ship,float score){
      this.id = id;
      this.bookList = books;
      this.process = process;
      this.ship = ship;
      this.score = score;
    }
}
class custom implements Comparator<Library>{
  public int compare(Library a, Library b){
      if(a.process == b.process){
        if(b.score/b.ship > a.score/a.ship) return 1;
        return -1;
      }
      else return a.process - b.process;
  }
}
class solution{

  public static void writeToFile(List<Result> res){
    try {
      File myObj = new File("d_output.txt");
      if (myObj.createNewFile()) {
        System.out.println("File created: " + myObj.getName());

      } else {
        System.out.println("File already exists.");
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    try {  
      FileWriter myWriter = new FileWriter("d_output.txt");
      myWriter.write(Integer.toString(res.size()) + System.getProperty( "line.separator" ));
      for(int i = 0; i < res.size(); i++){
        int libId = res.get(i).libId;
        int total = res.get(i).libBooks.size();
        myWriter.write(Integer.toString(libId) + " " + Integer.toString(total) + System.getProperty( "line.separator" ));
        StringBuilder builder = new StringBuilder();
        for(int j : res.get(i).libBooks){
          builder.append(Integer.toString(j) + " ");
        }
        builder.append(System.getProperty( "line.separator" ));
        myWriter.write(builder.toString());
      }
      myWriter.close();
      System.out.println("Successfully wrote to the file.");
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    } 

  }
    public static void main(String[] args) {
         Map<Integer,Books> books = new HashMap<>();
         Set<Integer> processedBooks = new HashSet<>();
            try {
              File myObj = new File("d_tough_choices.txt");
              Scanner myReader = new Scanner(myObj);
              String[] line = myReader.nextLine().split(" ");
                int numberofBooks = Integer.valueOf(line[0]);
                int librarires = Integer.valueOf(line[1]);
                int days = Integer.valueOf(line[2]);
                line = myReader.nextLine().split(" ");
                for(int i = 0; i < line.length; i++) {
                    books.put(i,new Books(Integer.valueOf(line[i]),i));
                }
                int libId = 0;
                PriorityQueue<Library> pq = new PriorityQueue<>(new custom());
                int lineC = 0;
              while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(" ");
                if(data[0].length() == 0) continue;
                System.out.println(lineC);
                lineC++;
                int libBooks = Integer.valueOf(data[0]);
                int process = Integer.valueOf(data[1]);
                int ship = Integer.valueOf(data[2]);
                if(process >= days){
                  myReader.nextLine();
                  continue;
                }
                data = myReader.nextLine().split(" ");
                lineC++;
                float score = 0;
                PriorityQueue<Books> temp = new PriorityQueue<>((Books a, Books b) -> (b.score - a.score));
                
                for(int i = 0; i < libBooks; i++){
                    int bookId = Integer.valueOf(data[i]);
                    Books b = books.get(bookId);
                    temp.offer(b);
                    score += b.score;
                }
                Library library = new Library(libId, temp,process, ship,score);
                pq.offer(library);
                libId++;
              }
              myReader.close();
              List<Result> res = new ArrayList<>();
              
              while(days > 0 && !pq.isEmpty()){
                  Library lib = pq.remove();

                  int process = lib.process;
                  int rem = days - process;
                  
                 
                  if(rem <= 0) break;
                  int shipBooks = (int) Math.ceil(rem*lib.ship);
           
                  List<Integer> temp = new ArrayList<>();
                  while(shipBooks >= 0 && !lib.bookList.isEmpty()){
                    int libBookId = lib.bookList.remove().id;
                    if(processedBooks.contains(libBookId)) continue;
                    temp.add(libBookId);
                    processedBooks.add(libBookId);
                    shipBooks--;
                  }
                  if(temp.size() > 0) {
                    days -= process;
                    res.add(new Result(lib.id,temp));
                  }
                  
              }
              System.out.println(res.size());
              writeToFile(res);
            } catch (FileNotFoundException e) {
              System.out.println("An error occurred.");
              e.printStackTrace();
            }
          }
}
