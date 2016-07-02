/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static java.lang.System.in;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQExpression;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQSequence;
import oracle.xml.xquery.OXQDataSource;

/**
 *
 * @author Juanmi
 */
public class Pruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws XQException, FileNotFoundException {
        
//        OXQDataSource ds = new OXQDataSource();
//        XQConnection xqc = ds.getConnection();
//        XQExpression xqe;
//        XQSequence xqs;
//        String query = "declare variable $doc external;"
//                + " data($doc//Family[@defined='true']/@name)";
////                + "for $b in $doc//Family "
////                + "return data($b[@defined='true']/@name)";
//        xqe = xqc.createExpression();
//        InputStream is = new FileInputStream(new File("ejemploF1.xml"));
//        xqe.bindDocument(new QName("doc"), is, null, null);
//        xqs = xqe.executeQuery(query);
//        
//        while(xqs.next()){
////            System.out.println(xqs.getItemAsString(null));
//            System.out.println(xqs.getAtomicValue());
//        }
//        
////        System.out.println(xqs.getSequenceAsString(null));
//        
//        xqc.close();


//          Gson gson = new Gson();
//          List<String> fm = new ArrayList();
//          fm.add("Enterobacteriaceae");
//          fm.add("Bacteria2");
//          fm.add("Bacteria3");
//          
//          String jFm = gson.toJson(fm);
//          System.out.println(jFm);

            Pattern p = Pattern.compile("-?\\d+");
            Matcher m = p.matcher("There are more than -2 and less than 12 numbers here");
            while (m.find()) {
              System.out.println(m.group());
            }

//        Gson gson = new GsonBuilder().create();
//        ArrayList<String> jobs = new ArrayList();
//        jobs.add("via tacciolli");
//        jobs.add("eficen");
//        jobs.add("uni");
//        ArrayList<Dir> directions = new ArrayList();
//        directions.add(new Dir("Calle1", 1));
//        directions.add(new Dir("Calle2", 2));
//        String p = gson.toJson(new Person("Juan", jobs, directions));
//        System.out.println(p);
    }
    
    private static int factorialRecursivo(int x){
        if(x == 0)
            return 1;
        else
            return x * factorialRecursivo(x-1);
    }
    
    private static int factorialIterativo(int x){
        int factor = 1;
        if (x > 0) {
            for (int i = 1; i <= x; i++) {
                factor *= i;
            }
        }
        return factor;
    }
    
    
    
    private static class Person{
        String name;
        List<String> jobs;
        List<Dir> directions;
        
        public Person(){}
        
        public Person(String name, List<String> jobs, List<Dir> directions){
            this.name = name;
            this.jobs = jobs;
            this.directions = directions;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getJobs() {
            return jobs;
        }

        public void setJobs(List<String> jobs) {
            this.jobs = jobs;
        }

        public List<Dir> getDirections() {
            return directions;
        }

        public void setDirections(List<Dir> directions) {
            this.directions = directions;
        }
        
    }
    
    private static class Dir{
        String street;
        int cp;
        
        public Dir(){}
        public Dir(String street, int cp){
            this.street = street;
            this.cp = cp;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public int getCp() {
            return cp;
        }

        public void setCp(int cp) {
            this.cp = cp;
        }
        
        
    }
}



//        OXQDataSource ds = new OXQDataSource();
//        XQConnection con = ds.getConnection();
//        String query = "<hello-world>{1 + 1}</hello-world>";
//        XQPreparedExpression expr = con.prepareExpression(query); 
//        XQSequence result = expr.executeQuery();
//        System.out.println(result.getSequenceAsString(null));
//
//        result.close();
//        expr.close();
//        con.close();