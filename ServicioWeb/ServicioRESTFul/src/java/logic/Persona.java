package logic;

import java.util.List;

/**
 *
 * @author Juanmi
 */
public class Persona{
    private String nombre;
    private int edad;
    private double altura;
    private List<String> aficiones;

    public Persona() {
    }

    public Persona(String nombre, int edad, double altura, List<String> aficiones) {
        this.nombre = nombre;
        this.edad = edad;
        this.altura = altura;
        this.aficiones = aficiones;
    }
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public List<String> getAficiones() {
        return aficiones;
    }

    public void setAficiones(List<String> aficiones) {
        this.aficiones = aficiones;
    }
    
}
