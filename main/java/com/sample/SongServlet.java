package com.sample;

import program.Main;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.channels.FileLockInterruptionException;


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

        String songUrl = getURL(songToPrint);
        req.setAttribute("url", songUrl);
        req.setAttribute("transposedBy", 0);
        req.setAttribute("size", 20);
        req.setAttribute("song", removeLine(songToPrint, "Available at: " + songUrl));
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

        String songURL = getURL(transposedSong);
        req.setAttribute("url", songURL);
        req.setAttribute("song", removeLine(transposedSong, "Available at: " + songURL));
        req.setAttribute("transposedBy", transposedBy);
        req.setAttribute("size", size);
        RequestDispatcher view = req.getRequestDispatcher("song.jsp");
        view.forward(req, resp);
    }

    private static void goHome(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("gohome.jsp");
        view.forward(req,resp);
    }

    private static String getURL(String song) {
        try {
            String preTarget = "Available at: ";
            String url = song.substring(song.indexOf(preTarget) + preTarget.length());
            url = url.substring(0, url.indexOf("\n"));
            return url;
        } catch (Exception ignored) { }
        throw new IllegalArgumentException("Song to print has not been formatted correctly or set");
    }

    private static String removeLine(String text, String line) {
        try {
            return text.replace(line + "\n", "");
        } catch (Exception ignored) { }
        throw new IllegalArgumentException(line + "\nis not in\n" + text);
    }

}