package Pruebas;
/*
Utilizando el algoritmo del LLDriver el analizador sintactico tiene una pila
de tokens que espera para mantener un orden de escritura, para comprobar que el orden
se esta cumpliendo toma palabra por palabra por palabra para comprobar que 
se esta ingresando las palabras esperadas.
*/
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class AnalizadorSintactico {
    Gramatica gramatica = new Gramatica();
    AnalizadorLexico lexico = new AnalizadorLexico();
    Pila pila = new Pila();                         //Objeto de pila
    ArrayList<String> noTerminales;                 //Listas de palabras y producciones
    ArrayList<String> terminales;
    ArrayList<String> derivaciones;                 //Derivacion = Produccion
    int[][] matriz;                                 //Recupera la matriz
    boolean aceptado = true;
    
    public AnalizadorSintactico(){
        gramatica.leerLineas();     //Lee la gramatica y recupera las listas
        gramatica.imprimir();       //Muestra las listas 
        noTerminales = gramatica.noTerminales;
        terminales = gramatica.terminales;
        derivaciones = gramatica.derivaciones;
        matriz = gramatica.Matriz;
    }//Fin del constructor
    
        //Metodo principal
    public void analizarSintaxis(){
        String tokenPila="", tokenEntrada;     //Palabras a trabajar en el ciclo
        int x, y;                           //Indices de las listas donde esta cada palabra
        pila.push("program");            //Mete el principal no terminal en la pila
        tokenEntrada = lexico.getToken();   //Toma el siguiente caracter de entrada del analizador lexico.
        while(!pila.estaVacia()){           //Mientras la pila tenga elementos.
            
            tokenPila = pila.tomarElemento();   //Toma la parte mas alta de la pila.
            if(esNoTerminal(tokenPila)){        //Si el valor de la pila es no terminal hay que derivarlo.
                x = noTerminales.indexOf(tokenPila);    //Toma los indices donde estan las palabras en las listas.
                y =   terminales.indexOf(tokenEntrada);
                System.out.println("Pila: "+tokenPila+" ("+x+")");  //Muestra las palabras actuales en el ciclo
                System.out.println("Entrada: "+tokenEntrada+" ("+y+")");
                
                if(matriz[x][y] > 0){           //Si hay produccion en la matriz con los indices
                    System.out.println("Produccion: "+matriz[x][y]+"\n");
                    String produccion = derivaciones.get(matriz[x][y]-1)+" ";
                    pila.pop();                 //Quita el elemento de la pila
                    cicloPush(produccion);      //Agrega su produccion a la pila
                }else{
                    System.out.println("Error de sintaxis:");   //Si no hay produccion hay un error
                    aceptado = false;
                    break;
                }
            }else{
                if(tokenPila.equals("")){   //Si en la pila hay vacio solo hay que descartarlo con un pop 
                    pila.pop();             //Descarta el vacio
                }else if(tokenPila.equals(tokenEntrada)){   //Si coinciden los tokens
                    System.out.println("Pila: "+tokenPila+"");
                    System.out.println("Entrada: "+tokenEntrada+"\n");
                    pila.pop();                             //hace pop
                    tokenEntrada = lexico.getToken();       //Toma la siguiente palabra de entrada
                }else{
                    if(tokenEntrada.equals("ERROR")){       //Imprime que es una palabra no aceptada
                        System.out.println("Error: Palabra no aceptada");
                        System.out.println("Palabra: "+lexico.token);
                    }else{
                        System.out.println("Error de sintaxis:");   //Error por sintaxis
                        System.out.println("Esperaba: "+tokenPila);
                        System.out.println("Recibio: "+tokenEntrada);
                    }
                    aceptado = false;
                    break;
                }
            }
        }//Fin del while de pila
        if(aceptado){
            System.out.println("\n ***| Analisis sintactico correcto |***");
        }else{
            System.out.println("\n ***| Error revisa tu codigo |***");
        }
    }//Fin del metodo
    
    
    //Este metodo revisa si un token es no terminal
    public boolean esNoTerminal(String cad){
        for(int i=0; i<noTerminales.size(); i++){
            if(cad.equals(noTerminales.get(i))){
                return true;
            }
        }
        return false;
    }//Fin del metodo
    
    
    //Toma el string de las producciones y lo separa en tokens
    //Los cuales agrega a la pila de derecha a izquierda
    public void cicloPush(String produccion){
        ArrayList<String> agregar = new ArrayList<String>();    //Guarda los tokens
        String token = "";          //Los tokens estan separados por un espacio
        for(int i=0; i<produccion.length(); i++){   //Recorre los caracteres
            char x = produccion.charAt(i);
            if(x != ' '){   //Los tokens se separan por espacios
                token += x;
            }else{
                agregar.add(token); //Agrega el token a la lista por meter en pila.
                token = "";
            }
        }
        for(int i=agregar.size()-1; i>=0; i--){ //los agrega por la drecha a la pila
            pila.push(agregar.get(i));
        }
    }//Fin del metodo
    
}//Class
