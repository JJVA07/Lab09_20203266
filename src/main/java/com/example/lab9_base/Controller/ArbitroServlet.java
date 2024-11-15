package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Dao.DaoArbitros;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "ArbitroServlet", urlPatterns = {"/ArbitroServlet"})
public class ArbitroServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");

        DaoArbitros daoArbitros = new DaoArbitros();


        ArrayList<String> paises = new ArrayList<>();
        paises.add("Peru");
        paises.add("Chile");
        paises.add("Argentina");
        paises.add("Paraguay");
        paises.add("Uruguay");
        paises.add("Colombia");

        switch (action) {

            case "buscar":
                String tipo = request.getParameter("tipo");
                String buscar = request.getParameter("buscar");

                ArrayList<Arbitro> arbitros;
                if ("nombre".equals(tipo) && buscar != null && !buscar.trim().isEmpty()) {
                    // Filtrar por nombre
                    arbitros = daoArbitros.busquedaNombre(buscar);
                } else if ("pais".equals(tipo) && buscar != null && !buscar.trim().isEmpty()) {
                    // Filtrar por país
                    arbitros = daoArbitros.busquedaPais(buscar);
                } else {
                    // Si no se especificó tipo o buscar, listar todos
                    arbitros = daoArbitros.listarArbitros();
                }

                request.setAttribute("arbitros", arbitros);
                request.setAttribute("opciones", new ArrayList<>(Arrays.asList("nombre", "pais")));
                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;

            case "guardar":
                String nombre = request.getParameter("nombre");
                String pais = request.getParameter("pais");

                // Verificar que los campos no estén vacíos
                if (nombre == null || nombre.trim().isEmpty() || pais == null || pais.trim().isEmpty()) {
                    request.setAttribute("error", "Todos los campos son obligatorios.");
                    request.setAttribute("paises", paises);
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                    return;
                }

                // Verificar si el nombre del árbitro ya existe
                if (daoArbitros.existeArbitro(nombre)) {
                    request.setAttribute("error", "El nombre del árbitro ya existe. Por favor, elige otro nombre.");
                    request.setAttribute("paises", paises);
                    view = request.getRequestDispatcher("/arbitros/form.jsp");
                    view.forward(request, response);
                    return;
                }

                // Crear el árbitro si las validaciones son exitosas
                Arbitro arbitro = new Arbitro();
                arbitro.setNombre(nombre);
                arbitro.setPais(pais);

                daoArbitros.crearArbitro(arbitro);

                // Redirigir a la lista de árbitros tras registro exitoso
                response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");
                break;

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        ArrayList<String> paises = new ArrayList<>();
        paises.add("Peru");
        paises.add("Chile");
        paises.add("Argentina");
        paises.add("Paraguay");
        paises.add("Uruguay");
        paises.add("Colombia");
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("nombre");
        opciones.add("pais");


        // Agregamos las listas de países y opciones al request para que puedan ser utilizadas en el JSP
        request.setAttribute("paises", paises);
        request.setAttribute("opciones", opciones);

        DaoArbitros daoArbitros = new DaoArbitros();

        switch (action) {
            case "lista":
                // Obtiene la lista de árbitros y la pasa al JSP
                ArrayList<Arbitro> arbitros = daoArbitros.listarArbitros();
                request.setAttribute("arbitros", arbitros);

                view = request.getRequestDispatcher("/arbitros/list.jsp");
                view.forward(request, response);
                break;
            case "crear":

                request.setAttribute("paises", paises);
                view = request.getRequestDispatcher("/arbitros/form.jsp");
                view.forward(request, response);
                break;
            case "borrar":
                // Obtener el id del árbitro a borrar
                int id = Integer.parseInt(request.getParameter("id"));

                // Llamar al método de borrado en DaoArbitros
                daoArbitros.borrarArbitro(id);

                // Redirigir de nuevo a la lista de árbitros después de borrar
                response.sendRedirect(request.getContextPath() + "/ArbitroServlet?action=lista");
                break;
        }
    }
}
