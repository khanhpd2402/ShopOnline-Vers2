/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dao.CouponDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import model.Coupon;

/**
 *
 * @author khanh
 */
@WebServlet(name = "AddCouponServlet", urlPatterns = {"/addcoupon"})
public class AddCouponServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddCouponServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddCouponServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("AddCoupon.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        String value_raw = request.getParameter("value").trim().replace(",", "");
        String expirationDate_raw = request.getParameter("expirationDate");
        String productStatus = request.getParameter("productStatus");

        CouponDAO cdb = new CouponDAO();
        Coupon coupon = cdb.getCouponByCode(code);
        if (expirationDate_raw != null && !expirationDate_raw.isEmpty()) {
            LocalDate expirationDateLocalDate = LocalDate.parse(expirationDate_raw, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Date expirationDate = java.sql.Date.valueOf(expirationDateLocalDate);
            try {
                double value = Double.parseDouble(value_raw);
                if (coupon == null) {
                    cdb.insertUserContact(code, value, productStatus.equals("1"), expirationDate);
                    request.setAttribute("messSuccess", "Add coupon success!");
                }else{
                    request.setAttribute("messExistCode", "Coupon Code already exist!");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("messError", "Invalid format value!");
            }
            request.getRequestDispatcher("AddCoupon.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
