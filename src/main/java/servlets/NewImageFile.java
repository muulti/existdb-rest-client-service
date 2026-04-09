package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import HTTPeXist.HTTPeXist;

public class NewImageFile extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private HTTPeXist eXist;

    public void init(ServletConfig config) {
        eXist = new HTTPeXist("http://localhost:8080");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String collection = request.getParameter("collection");
        String svgName = request.getParameter("svgName");
        String imagenSVG = request.getParameter("imagenSVG");
        System.out.println("-->NewImageFile: " + svgName + " en " + collection);

        int status = eXist.subirString(collection, imagenSVG, svgName);
        System.out.println("<--NewImageFile-status: " + status);

        // Go straight to the editor for the uploaded image
        request.setAttribute("collection", collection);
        request.setAttribute("svgName", svgName);
        request.setAttribute("imagenSVG", imagenSVG);
        request.setAttribute("imagenURI",
                "http://localhost:8080/exist/rest/db/" + collection + "/" + svgName + "/");

        RequestDispatcher rd = request.getRequestDispatcher("/jsp/imagenEdit.jsp");
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}