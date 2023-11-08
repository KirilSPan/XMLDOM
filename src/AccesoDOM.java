
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public ArrayList recorrerDOM() {

        String[] datos = null;
        ArrayList iDs = new ArrayList();

        try {
            //codigo escrito en Practica1
            //añade el nuevo método

            datos = new String[7];//lo usamos para la información de cada libro
            @SuppressWarnings("UnusedAssignment")
            Node nodo = null;
            Node root = doc.getFirstChild();
            NodeList nodelist = root.getChildNodes(); //(1)Ver dibujo del árbol
            System.out.println(doc.getFirstChild().getNodeName() + " of Books");

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

                    System.out.println("id: " + datos[0] + " -- Autor: " + datos[1] + " -- Titulo: " + datos[2] + " -- Genero: " + datos[3] + " -- Precio: " + datos[4] + " -- Ano publicacion: " + datos[5] + " -- Descripcion: " + datos[6]);
                    iDs.add(datos[0]);

                }
            }
        } catch (DOMException e) {
            System.out.println(e.toString());

        }

        return iDs;
    }

    public int insertarLibroEnDOM(String author, String title, String genre, double price, String publish_date, String description) {
        try {
            System.out.println("Add book to main DOM:" + author + ";" + title + ";" + genre);

            ArrayList<String> retrievedData = recorrerDOM(); // Assuming the method is called within the same class or accessible

            String datePattern = "yyyy-MM-dd"; // Date pattern to parse the publish_date
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            Date formattedDate = dateFormat.parse(publish_date);

            String incrementedID = null;

            // Get the last ID from the retrieved ArrayList
            if (!retrievedData.isEmpty()) {
                String lastIDString = retrievedData.get(retrievedData.size() - 1);

                // Increment the last ID
                int lastID;
                try {
                    lastID = Integer.parseInt(lastIDString.replaceAll("[^0-9]", ""));
                    lastID++; // Increment the ID by 1
                    incrementedID = String.valueOf(lastID);
                    System.out.println("Incremented ID: " + incrementedID);
                } catch (NumberFormatException e) {
                    System.out.println("Last ID is not a valid integer.");
                }
            } else {
                System.out.println("No IDs found in the ArrayList.");
            }

            Node nauthor = doc.createElement("author");
            Node nauthor_text = doc.createTextNode(author);
            nauthor.appendChild(nauthor_text);

            Node ntitle = doc.createElement("title");
            Node ntitle_text = doc.createTextNode(title);
            ntitle.appendChild(ntitle_text);

            Node ngenre = doc.createElement("genre");
            Node ngenre_text = doc.createTextNode(genre);
            ngenre.appendChild(ngenre_text);

            Node nprice = doc.createElement("price");
            Node nprice_text = doc.createTextNode(Double.toString(price));
            nprice.appendChild(nprice_text);

            Node npublish_date = doc.createElement("publish_date");
            Node npublish_date_text = doc.createTextNode(dateFormat.format(formattedDate));
            npublish_date.appendChild(npublish_date_text);

            Node ndescription = doc.createElement("description");
            Node ndescription_text = doc.createTextNode(description);
            ndescription.appendChild(ndescription_text);

            Node newBook = doc.createElement("book");
            ((Element) newBook).setAttribute("id", "bk" + incrementedID);
            newBook.appendChild(nauthor);
            newBook.appendChild(ntitle);
            newBook.appendChild(ngenre);
            newBook.appendChild(nprice);
            newBook.appendChild(npublish_date);
            newBook.appendChild(ndescription);

            newBook.appendChild(doc.createTextNode("\n"));

            Node raiz = doc.getFirstChild();
            raiz.appendChild(newBook);
            System.out.println("Libro insertado en DOM.");
            return 0;
        } catch (DOMException | ParseException e) {
            System.out.println(e);
            return -1;
        }
    }

   
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
    void guardarDOMcomoArchivo(String newFileXML) {
        try {
            Source src = new DOMSource(doc); // Definimos el origen
            StreamResult rst = new StreamResult(new File(newFileXML)); // Definimos el resultado
            // Declaramos el Transformer que tiene el método .transform() que necesitamos.
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            // Opción para indentar el archivo
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(src, (javax.xml.transform.Result) rst);
            System.out.println("File from the DOM succefully created\n");
        } catch (IllegalArgumentException | TransformerException e) {
            e.printStackTrace();
        }
    }

}
