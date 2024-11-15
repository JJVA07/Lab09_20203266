package com.example.lab9_base.Controller;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;
import com.example.lab9_base.Dao.DaoArbitros;
import com.example.lab9_base.Dao.DaoPartidos;
import com.example.lab9_base.Dao.DaoSelecciones;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "PartidoServlet", urlPatterns = {"/PartidoServlet", ""})
public class PartidoServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        RequestDispatcher view;
        String vista;
        switch (action) {

            case "guardar":
                // Recuperar parámetros del formulario
                String jornadaStr = request.getParameter("jornada");
                String fecha = request.getParameter("fecha");
                String localStr = request.getParameter("local");
                String visitanteStr = request.getParameter("visitante");
                String arbitroStr = request.getParameter("arbitro");

                // Crear las listas de selecciones y árbitros
                DaoSelecciones daoSelecciones = new DaoSelecciones();
                DaoArbitros daoArbitros = new DaoArbitros();
                ArrayList<Seleccion> selecciones = daoSelecciones.listarSelecciones();
                ArrayList<Arbitro> arbitros = daoArbitros.listarArbitros();

                // Validación de campos vacíos
                if (jornadaStr == null || fecha == null || localStr == null || visitanteStr == null || arbitroStr == null ||
                        jornadaStr.isEmpty() || fecha.isEmpty() || localStr.isEmpty() || visitanteStr.isEmpty() || arbitroStr.isEmpty()) {

                    // Configurar mensaje de error y listas de selección sin mantener valores ingresados
                    request.setAttribute("error", "Todos los campos son obligatorios.");
                    request.setAttribute("selecciones", selecciones);
                    request.setAttribute("arbitros", arbitros);
                    view = request.getRequestDispatcher("/partidos/form.jsp");
                    view.forward(request, response);
                    return;
                }

                int jornada = Integer.parseInt(jornadaStr);
                int local = Integer.parseInt(localStr);
                int visitante = Integer.parseInt(visitanteStr);
                int arbitro = Integer.parseInt(arbitroStr);

                // Validación de que el equipo local y visitante no sean iguales
                if (local == visitante) {
                    request.setAttribute("error", "La selección visitante no puede ser igual a la selección local.");
                    request.setAttribute("selecciones", selecciones);
                    request.setAttribute("arbitros", arbitros);
                    view = request.getRequestDispatcher("/partidos/form.jsp");
                    view.forward(request, response);
                    return;
                }

                // Validación de que el partido no se repita con las mismas selecciones en el mismo rol (local y visitante)
                DaoPartidos dao = new DaoPartidos();
                ArrayList<Partido> partidos = dao.listaDePartidos();
                boolean partidoExistente = partidos.stream().anyMatch(
                        p -> p.getSeleccionLocal().getIdSeleccion() == local &&
                                p.getSeleccionVisitante().getIdSeleccion() == visitante
                );

                if (partidoExistente) {
                    request.setAttribute("error", "Ya existe un partido con las mismas selecciones en este rol.");
                    request.setAttribute("selecciones", selecciones);
                    request.setAttribute("arbitros", arbitros);
                    view = request.getRequestDispatcher("/partidos/form.jsp");
                    view.forward(request, response);
                    return;
                }

                // Si todo es válido, crear el partido
                Partido partido = new Partido();
                partido.setNumeroJornada(jornada);
                partido.setFecha(fecha);

                // Crear objeto Seleccion para el equipo local
                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setIdSeleccion(local);
                partido.setSeleccionLocal(seleccionLocal);

                // Crear objeto Seleccion para el equipo visitante
                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setIdSeleccion(visitante);
                partido.setSeleccionVisitante(seleccionVisitante);

                // Crear objeto Arbitro para el partido
                Arbitro arbitroObj = new Arbitro();
                arbitroObj.setIdArbitro(arbitro);
                partido.setArbitro(arbitroObj);

                // Guardar el partido en la base de datos
                dao.crearPartido(partido);

                // Redirigir a la lista de partidos
                response.sendRedirect(request.getContextPath() + "/PartidoServlet?action=lista");
                break;

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");
        RequestDispatcher view;
        switch (action) {
            case "lista":
                DaoPartidos dao = new DaoPartidos();
                ArrayList<Partido> partidos = dao.listaDePartidos();

                System.out.println("Número de partidos: " + partidos.size());
                request.setAttribute("partidos", partidos);

                view = request.getRequestDispatcher("index.jsp");
                view.forward(request, response);
                break;
            case "crear":
                // Obtener listas de selecciones y árbitros
                DaoSelecciones daoSelecciones = new DaoSelecciones();
                DaoArbitros daoArbitros = new DaoArbitros();

                ArrayList<Seleccion> selecciones = daoSelecciones.listarSelecciones();
                ArrayList<Arbitro> arbitros = daoArbitros.listarArbitros();

                // Pasar las listas como atributos al JSP
                request.setAttribute("selecciones", selecciones);
                request.setAttribute("arbitros", arbitros);

                // Redirigir al formulario de creación de partidos
                view = request.getRequestDispatcher("/partidos/form.jsp");
                view.forward(request, response);
                break;

        }

    }
}
