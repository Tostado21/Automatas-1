package Pruebas;
/*
Esta clase lee un archivo de texto plano linea por linea.
El primer token de cada linea es tomado como un 'no terminal' y se agrega
a un arrayList sin repetir.
El resto de la linea lo mete en otro arrayList llamado derivacion.

Despues de leer todo el texto checa en todos los tokens del arrayList derivacion
en busca de ver cuales tokens no aparecen en los 'no Terminales' y los agrega a
otro arrayList llamado 'terminales' sin repeticion.
*/
import java.io.*;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Gramatica {
    ArrayList<String> noTerminales = new ArrayList<String>();
    ArrayList<String> terminales = new ArrayList<String>();
    ArrayList<String> derivaciones = new ArrayList<String>();
    String relleno = "";    //Guarda todo el texto
    
    int[][] Matriz = {   //Matriz predictiva
       //                        {  }  $  id  =   ;  if  (  )  il +   -   >   <  >=  <=  ==  !=
 /*Program*/                    {1, 0, 0,  0, 0,  0,  0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0},
 /*Sentencias*/                 {0, 3, 0,  2, 0,  0,  2, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0},
 /*Sentencia*/                  {0, 0, 0,  4, 0,  0,  5, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0},
 /*Expresion*/                  {0, 0, 0,  6, 0,  0,  0, 0, 0, 7, 0,  0,  0,  0,  0,  0,  0,  0},
 /*resto_expresion*/            {0, 0, 0,  0, 0, 10,  0, 0, 0, 0, 8,  9,  0,  0,  0,  0,  0,  0},
 /*expresion_logica*/           {0, 0, 0, 11, 0,  0,  0, 0, 0, 0, 0,  0,  0,  0,  0,  0,  0,  0},
 /*expresion_relacional*/       {0, 0, 0,  0, 0,  0,  0, 0, 0, 0, 0,  0, 12, 13, 14, 15, 16, 17}
    };
    
    
    
    public void leerLineas(){
        JOptionPane.showMessageDialog(null, "Indica el archivo de la gramatica");
        JFileChooser buscador = new JFileChooser();
        buscador.showOpenDialog(buscador
        );
        try{
            String patch = buscador.getSelectedFile().getAbsolutePath();    //leer texto
            FileInputStream archivo = new FileInputStream(patch);
            DataInputStream entrada = new DataInputStream(archivo);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            int contador = 1;   //Para indicar el numero de derivacion 
            String linea;
            
            while((linea = buffer.readLine()) != null){ //Linea por linea
                int inicio = 0; //Puntero del inicio del nuevo token
                int ultimo = 0; //Puntero del 
                String noTerminal = ""; //Tokens para los arrayList
                String derivacion = "";
                relleno += contador+" - "+linea+"\n";   //linea de gramatica completa
                contador++;
                
                while(linea.charAt(ultimo) != ' '){ //Toma el primer token como 'no termina' 
                    noTerminal += linea.charAt(ultimo);
                    ultimo++;
                }
                if(noTerminales.contains(noTerminal) == false){ //Revisa si no esta
                    noTerminales.add(noTerminal);   //Lo agrega
                }
                inicio = ultimo+1;  //pasa el espacio entre el no terminal y el resto de la linea
                ultimo = inicio;
                
                while(ultimo < linea.length()){     //Toma el resto de la linea como 'derivacion'
                    derivacion += linea.charAt(ultimo);
                    ultimo++;
                }
                derivaciones.add(derivacion);   //lo agrega
            }//Fin del while que recorre lineas
            
            buscarTerminales(); //llama al metodo
            
        }catch(Exception e){
            System.out.println("Ocurrio un error "+e.getMessage());
        }
    }//Fin del metodo
    
    
    //Este metodo revisa en las derivaciones los tokens que no aparecen 
    //en los no terminales y los agrega a los terminales
    public void buscarTerminales(){
        for(int i=0; i<derivaciones.size(); i++){   //Recorre el arrayList
            String derivacion = derivaciones.get(i);    //Toma una linea
            String token = "";
                
            for(int x=0; x<derivacion.length(); x++){   //Recorre los caracteres de la linea
                char caracter = derivacion.charAt(x);
                if(caracter != ' '){    //Los tokens estan separados por espacios 
                    token += caracter;
                }else{
                    if(noTerminales.contains(token) == false){  //Revisa si el token esta en no terminales
                        if(terminales.contains(token) == false){    //Revisa si ya esta repetido
                            terminales.add(token);  //Lo agrega
                        }
                    }
                    token = ""; //vacia el token para buscar uno nuevo en la linea
                }
                if(x == derivacion.length()-1){     //hace lo mismo si es el ultimo token de la linea
                    if(noTerminales.contains(token) == false){
                        if(terminales.contains(token) == false){
                            terminales.add(token);
                        }
                    }
                }
            }//Fin del for que recorre la linea
        }//Fin del for que recorre las derivaciones
    }//Fin del metodo
    
    
    public void imprimir(){
        System.out.println("\n |***************| Terminales |*****************|");
        for(int i=0; i<terminales.size(); i++){
            System.out.println(i+" - "+terminales.get(i));
        }
        System.out.println("\n |***************| NO Terminales |*****************|");
        for(int i=0; i<noTerminales.size(); i++){
            System.out.println(i+" - "+noTerminales.get(i));
        }
        System.out.println("\n |***************| Derivaciones |*****************|");
        for(int i=0; i<derivaciones.size(); i++){
            System.out.println(i+" - "+derivaciones.get(i));
        }
        System.out.println("\n |***************| Gramatica completa |*****************|");
        System.out.println(relleno);
    }//Fin del metodo
    
    
}//Class
