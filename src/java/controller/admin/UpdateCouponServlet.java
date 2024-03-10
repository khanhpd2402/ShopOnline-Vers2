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
@WebServlet(name = "UpdateCouponServlet", urlPatterns = {"/updatecoupon"})
public class UpdateCouponServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateCouponServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateCouponServlet at " + request.getContextPath() + "</h1>");
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
        CouponDAO cdb = new CouponDAO();
        String id = request.getParameter("id");
        if (id != null && id.length() > 0) {
            //get Coupon by id
            Coupon coupon = cdb.getCouponById(Integer.parseInt(id));
            request.setAttribute("dataCoupon", coupon);
            request.getRequestDispatcher("UpdateCoupon.jsp").forward(request, response);
        } else {
            response.sendRedirect("couponmanage");
        }
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
        // Lấy thông tin từ form
        String id_raw = request.getParameter("id");
        String code = request.getParameter("code");
        String value_raw = request.getParameter("value").trim().replace(",", "");
        String expirationDate_raw = request.getParameter("expirationDate");
        String productStatus = request.getParameter("productStatus");
// Tạo đối tượng CouponDAO để thực hiện các thao tác liên quan đến coupon trong cơ sở dữ liệu
        CouponDAO cdb = new CouponDAO();
        Coupon coupon = cdb.getCouponByCode(code);
        // Kiểm tra ngày hết hạn có được nhập hay không
        if (expirationDate_raw != null && !expirationDate_raw.isEmpty()) {
            LocalDate expirationDateLocalDate = LocalDate.parse(expirationDate_raw, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Date expirationDate = java.sql.Date.valueOf(expirationDateLocalDate);
            try {
                double value = Double.parseDouble(value_raw);
                // Kiểm tra xem mã coupon đã tồn tại chưa (trừ trường hợp nếu mã đó thuộc coupon cần cập nhật)
                if (coupon != null && coupon.getId() != Integer.parseInt(id_raw)) {
                    request.setAttribute("messExistCode", "Coupon Code already exist!");
                    Coupon cou = cdb.getCouponById(Integer.parseInt(id_raw));
                    request.setAttribute("dataCoupon", cou);
                    request.getRequestDispatcher("UpdateCoupon.jsp").forward(request, response);
                } else {
                    // Cập nhật thông tin của coupon trong cơ sở dữ liệu
                    Coupon c = new Coupon(Integer.parseInt(id_raw), code, value, productStatus.equals("1"), expirationDate);
                    cdb.updateCoupon(c);
                    response.sendRedirect("couponmanage");
                }
            } catch (NumberFormatException e) {
                // Xử lý ngoại lệ nếu giá trị nhập vào không hợp lệ
                Coupon cou = cdb.getCouponById(Integer.parseInt(id_raw));
                request.setAttribute("dataCoupon", cou);
                request.setAttribute("messError", "Invalid format value!");
                request.getRequestDispatcher("UpdateCoupon.jsp").forward(request, response);

            }
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
