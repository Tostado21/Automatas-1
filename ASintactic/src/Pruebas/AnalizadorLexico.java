package Pruebas;

/*
Esta clase representa al automata donde cada metodo "Estado?"
es un estado que lee el siguiente simbolo y dependiendo de su valor
manda a llamar a otro Estado para repetir el proceso.

Cada vez que se pasa a un estado se inserta el simbolo en el token y cuando se 
llega al final de un token se imprime y la variableque lo guarda se limpia.
*/
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class AnalizadorLexico {
    String txt;
    int inicio = 0; //Puntero del inicio del nuevo token
    int ultimo = 0; //Puntero del final
    String token = "", temp="";

    public AnalizadorLexico(){
        txt = leer(); //Archivo de la clase LectorArchivo    
    }//fin del constructor
    
    
    public String leer(){
        JOptionPane.showMessageDialog(null, "Indica el codigo a leer");
        String texto = "";
        JFileChooser buscador = new JFileChooser();
        buscador.showOpenDialog(buscador);
        try{
            //Obtiene la ruta absoluta del archivo elegido
            String patch = buscador.getSelectedFile().getAbsolutePath();
            //Abrir el arhivo
            FileInputStream archivo = new FileInputStream(patch);
            //Creando el objeto de entrada
            DataInputStream entrada = new DataInputStream(archivo);
            //Crear un buffer de lectura
            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            String lineas;
            while((lineas = buffer.readLine()) != null){
                //System.out.println(lineas);
                texto += lineas+"\n";
            }
        }catch(Exception e){
            System.out.println("Ocurrio un error "+e.getMessage());
        }
        return texto;
    }//Fin del metodo
    
    //Regresa la categoria del siguiente token
    public String getToken(){
        token = ""; //Limpia el token de entrada
        String aux = Estado0(); //Guarda la categoria
        inicio = ultimo+1;  //Mueve los indices para la siguiente palabra
        ultimo = inicio;
        return aux; //Regresa la categoria
    }//Metodo
    
    
    boolean esOtroCaracter(char aux){ //Revisa si el siguiente caracter es un nuevo token
        char[] lista = {'{', '}',';', '(', ')', '+', '-', '=', '<', '>'};
        for(int i=0; i<lista.length; i++){
            if(aux == lista[i]){
                return true;
            }
        }
        return false;
    }//Fin del metodo
    
    
    boolean esEspacio(char aux){    //Revisa si el siguiente caracter es un espacio
        char[] lista = {' ', '\n', '\t'};
        for(int i=0; i<lista.length; i++){
            if(aux == lista[i]){
                return true;
            }
        }
        return false;
    }//Fin del metodo
    
    boolean esPalabraReservada(String t) {
         String [] pReservada={"if"};
         for (int i = 0; i < pReservada.length; i++) {
             if (t.equals(pReservada[i])) {
                 temp=pReservada[i];
            return true;     
             }
        }
        return false;

    }
    //Si se llega a un estado de ciclo de no aceptacion (el infiernito)
    //Se recorre todo el token ya que no sera aceptado.
    public String infiernoFinal(){
        if(ultimo < txt.length()){
            char simbolo = txt.charAt(ultimo);
            
            if(esOtroCaracter(simbolo)){
                ultimo--;
                return("ERROR");
            }else if(esEspacio(simbolo)){
                ultimo--;
                return("ERROR");
            }else{
                token += simbolo;
                ultimo++;
                return(infiernoFinal());
            }
        }else{
            return("ERROR");
        }
    }//Fin del metodo
    
    
    //Estados del automata por los que se desplazara con la entrada de 
    //cada caracter del token
    public String Estado0(){
        if(ultimo < txt.length()){
            char simbolo = txt.charAt(inicio);
            if (simbolo == '=') {
                token += simbolo;
                ultimo++;
                return(Estado3());
            } //igual
            else if (simbolo == '>') {
                token += simbolo;
                ultimo++;
                return(Estado3());  //mayor e igual  

            } else if (simbolo == '<') {
                token += simbolo;
                ultimo++;
                return(Estado3()); //menor e igual   
            }else if(simbolo == '!'){ //Revisa el caracter siguiente
                token += simbolo;   //Recorre los indices
                ultimo ++;
                return(Estado4());  //Llama al siguiente estado
            }else if(Character.isLetter(simbolo)){
                token += simbolo;
                ultimo ++;
                return(Estado1());
            }else if(Character.isDigit(simbolo)){
                token += simbolo;
                ultimo ++;
                return(Estado2());
                }else if(simbolo == '{'){
                token += simbolo;
                return(token);
            }else if(simbolo == '}'){
                token += simbolo;
                return(token);
            }else if(simbolo == '+'){
                token += simbolo;
                return(token);
                
            }else if(simbolo == '-'){
                token += simbolo;
                return(token);
            }else if(simbolo == '('){
                token += simbolo;
                return(token);
            }else if(simbolo == ')'){
                token += simbolo;
                return(token);
            }else if(simbolo == ';'){
                token += simbolo;
                return(token);
            }else{
                if(esEspacio(simbolo)){     //Revisa si es el fin del token
                    token = "";
                    inicio++;
                    ultimo = inicio;
                    return(Estado0());
                }else{                      //Sino no es aceptada
                    token += simbolo;
                    ultimo ++;
                    return(infiernoFinal());
                }
            }
        }else{
            return("$");    //Si llego al fin del texto regresa el $
        }
    }//Fin del estado 0
    
    public String Estado1(){ //ID
        if(ultimo < txt.length()){
            char simbolo = txt.charAt(ultimo);
            if(Character.isLetter(simbolo)){
                token += simbolo;
                ultimo ++;
                return(Estado1());
            }else if(Character.isDigit(simbolo)){
                token += simbolo;
                ultimo ++;
                return(Estado1());
            }else if(simbolo == '_'){
                token += simbolo;
                ultimo ++;
                return(Estado1());
            }else{
                if(esOtroCaracter(simbolo)){
                    ultimo--;
                    if (esPalabraReservada(token)) {
                      return(token);  
                    }else{
                    return("id");    
                    }
                    
                }else if(esEspacio(simbolo)){
                    ultimo--;
                    if (esPalabraReservada(token)) {
                      return(token);
                    }else{
                    return("id");    
                    }
                }else{
                    token += simbolo;
                    ultimo ++;
                    return(infiernoFinal());
                }
            }
        }else{
            return("id");
            
        }
    }//Fin del metodo
    
    public String Estado2(){//IntLiteral
        if(ultimo < txt.length()){
            char simbolo = txt.charAt(ultimo);
        
            if(Character.isDigit(simbolo)){
                token += simbolo;
                ultimo ++;
                return(Estado2());
            }else{
                if(esOtroCaracter(simbolo)){
                    ultimo--;
                    return("intLiteral");
                }else if(esEspacio(simbolo)){
                    ultimo--;
                    return("intLiteral");
                }else{
                    token += simbolo;
                    ultimo ++;
                    return(infiernoFinal());
                }
            }
        }else{
            return("intLiteral");
        }
    }//Fin del metodo
    
    public String Estado3(){//! va a ser el 4
        if(ultimo < txt.length()){
            char simbolo = txt.charAt(ultimo);
            if(simbolo == '='){
                token += simbolo;
                return(token);
            }else{                                  //OTHER
                ultimo--;
                return(token);    //ERROR indica que la palabra no es aceptada
            }
        }else{
            return("ERROR");
        }
    }//Fin del metodo
    
    public String Estado4(){//! va a ser el 4
        if(ultimo < txt.length()){
            char simbolo = txt.charAt(ultimo);
            if(simbolo == '='){
                token += simbolo;
                return(token);
            }else{                                  //OTHER
                ultimo--;
                return("ERROR");    //ERROR indica que la palabra no es aceptada
            }
        }else{
            return("ERROR");
        }
    }//Fin del metodo
    

}//Class
