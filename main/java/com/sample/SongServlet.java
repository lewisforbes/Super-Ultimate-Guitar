package com.sample;

import program.Main;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(
        name = "songservlet",
        urlPatterns = "/song"
)
public class SongServlet extends HttpServlet {

    private static String songToPrint = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        songToPrint = null;
        String query = req.getParameter("query");
        songToPrint = Main.newSong(query);
        if (songToPrint == null) {
            RequestDispatcher view = req.getRequestDispatcher("invalid.html");
            view.forward(req,resp);
            return;
        }

        req.setAttribute("transposedBy", 0);
        req.setAttribute("size", 15);
        req.setAttribute("song", songToPrint);
        RequestDispatcher view = req.getRequestDispatcher("song.jsp");
        view.forward(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (songToPrint == null) {
            goHome(req,resp);
            return;
        }

        String query = req.getQueryString();
        if (!(query.contains("transpose=") && query.contains("size="))) {
            goHome(req,resp);
            return;
        }

        String transposedSong;
        int transposedBy;
        int size;
        try {
            size = Integer.parseInt(query.substring(query.lastIndexOf("=")+1));
            query = query.substring(0, query.indexOf("&"));
            transposedBy = Integer.parseInt(query.substring(query.indexOf("=")+1));
            transposedSong = Main.transposeSong(transposedBy);
        } catch (Exception e) {
            goHome(req,resp);
            return;
        }

        req.setAttribute("song", transposedSong);
        req.setAttribute("transposedBy", transposedBy);
        req.setAttribute("size", size);
        RequestDispatcher view = req.getRequestDispatcher("song.jsp");
        view.forward(req, resp);
    }

    private static void goHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("gohome.jsp");
        view.forward(req,resp);
    }

}