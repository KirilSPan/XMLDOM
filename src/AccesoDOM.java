import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Kiril_SP
 */
public class AccesoDOM {

    Document doc;
    //DocumentBuilderFactory factory;

    public int abrirXMLaDOM(File f) {

        try {
            System.out.println("Abriendo archivo XML file y generando DOM ....");
            //creamos nuevo objeto DocumentBuilder al que apunta la variable factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //ignorar comentarios y espacios blancos
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            //DocumentBuilder tiene el método parse que es el que genera DOM en memoria
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(f);
            // ahora doc apunta al arbol DOM y podemos recorrerlo
            System.out.println("DOM creado con éxito.");
            

            return 0;

        } catch (IOException | ParserConfigurationException | SAXException e) {

            return -1;
        }
        
        

    }

    public void recorrerDOM() {
    try {
        Node nodo = null;
        Node root = doc.getFirstChild();
        NodeList nodelist = root.getChildNodes();
        System.out.println(doc.getFirstChild().getNodeName());

        
        for (int i = 0; i < nodelist.getLength(); i++) {
            nodo = nodelist.item(i);
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                String[] datos = new String[7]; // Actualizamos el tamaño del array

                
                datos[0] = nodo.getAttributes().getNamedItem("id").getNodeValue();
                datos[1] = nodo.getAttributes().getNamedItem("autor").getNodeValue();
                datos[2] = nodo.getAttributes().getNamedItem("Titulo").getNodeValue();
                datos[3] = nodo.getAttributes().getNamedItem("genero").getNodeValue();
                datos[4] = nodo.getAttributes().getNamedItem("precio").getNodeValue();
                datos[5] = nodo.getAttributes().getNamedItem("Año publicacion").getNodeValue();
                datos[6] = nodo.getAttributes().getNamedItem("descripcion").getNodeValue();

                NodeList nl2 = nodo.getChildNodes();

                for (int j = 0; j < nl2.getLength(); j++) {
                    Node ntemp = nl2.item(j);
                    if (ntemp.getNodeType() == Node.ELEMENT_NODE) {
                        if (ntemp.getNodeName().equals("autor")) {
                            datos[5] = ntemp.getTextContent();
                        } else if (ntemp.getNodeName().equals("titulo")) {
                            datos[6] = ntemp.getTextContent();
                        }
                    }
                }

                System.out.println("id: " + datos[0] + " -- Autor: " + datos[5] + " -- Título: " + datos[6] + " -- Género: " + datos[1] + " -- Precio: " + datos[2] + " -- Año publicación: " + datos[3] + " -- Descripción: " + datos[4]);
            }
        }
    } catch (DOMException e) {
        System.out.println(e.toString());
    }
}


    public int insertarLibroEnDOM(String titulo, String autor, String fecha) {
        try {
            System.out.println("Añadir libro al árbol DOM:" + titulo + ";" + autor + ";" + fecha);
            //crea los nodos=>los añade al padre desde las hojas a la raíz
            //CREATE TITULO con el texto en medio
            Node ntitulo = doc.createElement("Titulo");//crea etiquetas <Titulo>...</Titulo>
            Node ntitulo_text = doc.createTextNode(titulo);//crea el nodo texto para el Titulo
            ntitulo.appendChild(ntitulo_text);//añade el titulo a las etiquetas=><Titulo>titulo</Titulo>
            //Node nautor=doc.createElement("Autor").appendChild(doc.createTextNode(autor));//one line doesn't work
            //CREA AUTOR
            Node nautor = doc.createElement("Autor");
            Node nautor_text = doc.createTextNode(autor);
            nautor.appendChild(nautor_text);
            //CREA LIBRO, con atributo y nodos Título y Autor 
            Node nLibro = doc.createElement("Libro");
            ((Element) nLibro).setAttribute("publicado", fecha);
            nLibro.appendChild(ntitulo);
            nLibro.appendChild(nautor);
            //APPEND LIBRO TO THE ROOT

            nLibro.appendChild(doc.createTextNode("\n"));//para insertar saltos de línea

            Node raiz = doc.getFirstChild();//tb. doc.getChildNodes().item(0)
            raiz.appendChild(nLibro);
            System.out.println("Libro insertado en DOM.");
            return 0;
        } catch (DOMException e) {
            System.out.println(e);
            return -1;
        }
        
        
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public int deleteNode(String tit) {
        System.out.println("Buscando el Libro " + tit + " para borrarlo");
        try {
            //Node root=doc.getFirstChild();
            //Node raiz = doc.getDocumentElement();
            NodeList nl1 = doc.getElementsByTagName("Titulo");
            Node n1;
            for (int i = 0; i < nl1.getLength(); i++) {
                n1 = nl1.item(i);
                if (n1.getNodeType() == Node.ELEMENT_NODE) {//redundante por getElementsByTagName, no lo es si buscamos getChildNodes()
                    if (n1.getChildNodes().item(0).getNodeValue().equalsIgnoreCase(tit)) {
                        System.out.println("Borrando el nodo <Libro> con título " + tit);
                        //n1.getParentNode().removeChild(n1); //borra <Titulo> tit </Titulo>, pero deja Libro y Autor
                        n1.getParentNode().getParentNode().removeChild(n1.getParentNode());
                    }

                }
            }
            System.out.println("Nodo borrado");
            //Guardar el arbol DOM en un nuevo archivo para mantener nuestro archivo original
            //guardarDOMcomoArchivo("LibrosBorrados.xml");
            
            

            return 0;
        } catch (DOMException e) {
            System.out.println(e);
            e.printStackTrace();
            return -1;
        }
    }

    // Crea un nuevo archivo xml del DOM en memoria
    @SuppressWarnings("CallToPrintStackTrace")
    void guardarDOMcomoArchivo(String nuevoArchivo) {
        try {
            Source src = new DOMSource(doc); // Definimos el origen
            StreamResult rst = new StreamResult(new File(nuevoArchivo)); // Definimos el resultado
            // Declaramos el Transformer que tiene el método .transform() que necesitamos.
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            // Opción para indentar el archivo
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(src, (javax.xml.transform.Result) rst);
            System.out.println("Archivo creado del DOM con éxito\n");
        } catch (IllegalArgumentException | TransformerException e) {
            e.printStackTrace();
        }
    }

}
