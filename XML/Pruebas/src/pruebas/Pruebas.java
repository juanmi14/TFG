/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebas;

/**
 *
 * @author Juanmi
 */
public class Pruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int x = 6;
        System.out.println(factorialIterativo(x));
        System.out.println(factorialRecursivo(x));
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
}
