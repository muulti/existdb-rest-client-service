package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import HTTPeXist.HTTPeXist;

public class CreateCollection extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private HTTPeXist eXist;

    public void init(ServletConfig config) {
        eXist = new HTTPeXist("http://localhost:8080");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String collection = request.getParameter("collection");
        System.out.println("-->CreateCollection: " + collection);

        int status = eXist.create(collection);
        System.out.println("<--CreateCollection-status: " + status);

        if (status == 200 || status == 201) {
            request.setAttribute("informacion", "Coleccion creada: " + collection);
        } else {
            request.setAttribute("informacion", "Error al crear la coleccion: " + collection + " (status " + status + ")");
        }
        RequestDispatcher rd = request.getRequestDispatcher("/jsp/index.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}