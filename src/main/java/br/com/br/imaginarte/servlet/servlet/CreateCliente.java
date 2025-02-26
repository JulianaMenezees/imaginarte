package br.com.br.imaginarte.servlet.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/Cadastro_Admin")
public class CreateCliente extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nome_admin = request.getParameter("nome_admin");

        System.out.println(nome_admin);

        request.getRequestDispatcher("index.html").forward(request, response);

    }


}
