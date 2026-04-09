package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import HTTPeXist.HTTPeXist;

public class NewImage extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private HTTPeXist eXist;

    public void init(ServletConfig config) {
        eXist = new HTTPeXist("http://localhost:8080");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String collection = request.getParameter("collection");
        String svgName = request.getParameter("svgName");
        System.out.println("-->NewImage: " + svgName + " en " + collection);

        // Minimal blank SVG
        String blankSvg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"200\" height=\"200\">\n"
                + "  <rect width=\"200\" height=\"200\" fill=\"white\"/>\n"
                + "</svg>";

        int status = eXist.subirString(collection, blankSvg, svgName);
        System.out.println("<--NewImage-status: " + status);

        // Go straight to the editor for the new image
        request.setAttribute("collection", collection);
        request.setAttribute("svgName", svgName);
        request.setAttribute("imagenSVG", blankSvg);
        request.setAttribute("imagenURI",
                "http://localhost:8080/exist/rest/db/" + collection + "/" + svgName + "/");

        RequestDispatcher rd = request.getRequestDispatcher("/jsp/imagenEdit.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}