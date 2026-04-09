package servlets;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import HTTPeXist.HTTPeXist;

public class ListResources extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HTTPeXist eXist;

	
	public void init(ServletConfig config) {
		System.out.println("---> Entrando en init()de listResource");
		eXist = new HTTPeXist("http://localHost:8080");
		System.out.println("---> Saliendo de init()de LoginServlet");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String collection = request.getParameter("collection");
		String data = eXist.list(collection);
		System.out.println("LIST_RESOURCE: " + data);
		System.out.println("***************************************");
		System.out.println("Listando la colleccion:");
		System.out.println("Coleccion " + collection);


		if (data.equals("")) {
			// Collection no existe
			request.setAttribute("informacion", "Coleccion No Existe: " + collection);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/index.jsp");
			rd.forward(request, response);
			return;
		}

		Document doc = convertStringToXMLDocument(data);
		NodeList valorNode = doc.getElementsByTagName("exist:resource");
		System.out.println("Numero de elementos " + valorNode.getLength());
		System.out.println("recursos encontrados: " + valorNode.getLength());

		if (valorNode.getLength() == 0) {
			// Collection exists but is empty
			request.setAttribute("informacion", "La coleccion '" + collection + "' existe pero esta vacia");
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/index.jsp");
			rd.forward(request, response);
			return;
		}

		// Collection exists and has resources — load them all
		Map<String, String> listaSVG = new HashMap<String, String>();
		for (int i = 0; i < valorNode.getLength(); i++) {
			String nombre = valorNode.item(i).getAttributes().getNamedItem("name").getNodeValue();
			String imagen = eXist.read(collection, nombre);
			System.out.println("nombre: " + nombre);
			listaSVG.put(nombre, imagen);
		}

		request.setAttribute("collection", collection);
		request.setAttribute("listaSVG", listaSVG);
		System.out.println("size: " + listaSVG.size());
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/imagenList.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request, response);
	}

	private static Document convertStringToXMLDocument(String xmlString) {
		// Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try {
			// Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();

			// Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
