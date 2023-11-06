
import java.io.File;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Kiril_SP
 */
public class Proyecto1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        AccesoDOM accesoLibro = new AccesoDOM();
        File fichero = new File("./src/books.xml");
        
        accesoLibro.abrirXMLaDOM(fichero);
        
        
    }
    
}
