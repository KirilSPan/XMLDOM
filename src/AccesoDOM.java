
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
            //codigo escrito en Practica1
            //añade el nuevo método

            String[] datos = new String[7];//lo usamos para la información de cada libro
            @SuppressWarnings("UnusedAssignment")
            Node nodo = null;
            Node root = doc.getFirstChild();
            NodeList nodelist = root.getChildNodes(); //(1)Ver dibujo del árbol
            System.out.println(doc.getFirstChild().getNodeName()+ " of Books");

            //recorrer el árbol DOM. El 1er nivel de nodos hijos del raíz
            for (int i = 0; i < nodelist.getLength(); i++) {
                nodo = nodelist.item(i);//node toma el valor de los hijos de raíz
                if (nodo.getNodeType() == Node.ELEMENT_NODE) {//miramos nodos de tipo Element

                    @SuppressWarnings("UnusedAssignment")
                    Node ntemp = null;
                    int contador = 1;
                    //sacamos el valor del atributo publicado
                    datos[0] = nodo.getAttributes().item(0).getNodeValue();
                    //sacamos los valores de los hijos de nodo, Titulo y Autor
                    NodeList nl2 = nodo.getChildNodes();//obtenemos lista de hijos (2)

                    for (int j = 0; j < nl2.getLength(); j++) {//iteramos en esa lista 
                        ntemp = nl2.item(j);
                        if (ntemp.getNodeType() == Node.ELEMENT_NODE) {
                            if (ntemp.getNodeType() == Node.ELEMENT_NODE) {
//para conseguir el texto de titulo y autor, se //puedo hacer con getNodeValue(), también con  //getTextContent() si es ELEMENT
                                datos[contador] = ntemp.getTextContent(); // también datos[contador]=ntemp.getChildNodes().item(0).getNodeValue();

                                contador++;
                            }
                        }
                        //el array de String datos[] tiene los valores que necesitamos

                    }
                    
                    System.out.println("id: " + datos[0] + " -- Autor: " + datos[1] + " -- Título: " + datos[2] + " -- Género: " + datos[3] + " -- Precio: " + datos[4] + " -- Año publicación: " + datos[5] + " -- Descripción: " + datos[6]);

                }
            }
        } catch (DOMException e) {
            System.out.println(e.toString());

        }
    }

    public int insertarLibroEnDOM(String author, String title, String genre) {
        try {
            System.out.println("Add book to main DOM:" + title + ";" + title + ";" + genre);
            //crea los nodos=>los añade al padre desde las hojas a la raíz
            //CREATE TITULO con el texto en medio
            Node ntitulo = doc.createElement("Title");//crea etiquetas <Titulo>...</Titulo>
            Node ntitulo_text = doc.createTextNode(title);//crea el nodo texto para el Titulo
            ntitulo.appendChild(ntitulo_text);//añade el titulo a las etiquetas=><Titulo>titulo</Titulo>
            //Node nautor=doc.createElement("Autor").appendChild(doc.createTextNode(autor));//one line doesn't work
            //CREA AUTOR
            Node nautor = doc.createElement("Author");
            Node nautor_text = doc.createTextNode(title);
            nautor.appendChild(nautor_text);
            //CREA LIBRO, con atributo y nodos Título y Autor 
            Node nLibro = doc.createElement("Libro");
            ((Element) nLibro).setAttribute("publicado", genre);
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
            NodeList nl1 = doc.getElementsByTagName("title");
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
