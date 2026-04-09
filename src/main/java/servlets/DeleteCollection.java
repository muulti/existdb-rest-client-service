package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import HTTPeXist.HTTPeXist;

public class DeleteCollection extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private HTTPeXist eXist;

    public void init(ServletConfig config) {
        eXist = new HTTPeXist("http://localhost:8080");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String collection = request.getParameter("collection");
        System.out.println("-->DeleteCollection: " + collection);

        int status = eXist.delete(collection);
        System.out.println("<--DeleteCollection-status: " + status);

        if (status == 200) {
            request.setAttribute("informacion", "Coleccion borrada: " + collection);
        } else {
            request.setAttribute("informacion", "Error al borrar la coleccion: " + collection + " (status " + status + ")");
        }
        RequestDispatcher rd = request.getRequestDispatcher("/jsp/index.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}