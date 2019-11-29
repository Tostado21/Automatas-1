package Pruebas;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JFileChooser;

public class Pila {
    ArrayList<String> elementos = new ArrayList<String>();
    
    public void push(String token){
        elementos.add(token);
    }
    
    public void pop(){
        int ultimo = elementos.size()-1;
        elementos.remove(ultimo);
    }
    
    public boolean estaVacia(){
        return elementos.isEmpty();
    }
    
    public String tomarElemento(){
        int ultimo = elementos.size()-1;
        return elementos.get(ultimo);
    }
    
}//Class

