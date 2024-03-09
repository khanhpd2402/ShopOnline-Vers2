<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value = "vi_VN"/>
<%@ page import="java.time.LocalDate" %>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Lumino - Dashboard</title>
        <link href="cssadmin/bootstrap.min.css" rel="stylesheet">
        <link href="cssadmin/font-awesome.min.css" rel="stylesheet">
        <link href="cssadmin/datepicker3.css" rel="stylesheet">
        <link href="cssadmin/styles.css" rel="stylesheet">
        <!--Custom Font-->
        <link href="https://fonts.googleapis.com/css?family=Montserrat:300,300i,400,400i,500,500i,600,600i,700,700i" rel="stylesheet">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" integrity="sha384-DyZ88mC6Up2uqS4h/KRgHuoeGwBcD4Ng9SiP4dIRy0EXTlnuz47vAwmeGwVChigm" crossorigin="anonymous"/>

        <style>
            table {
                border-collapse: collapse;
                width: 100%;
            }

            th, td {
                border: 1px solid #ddd;
                padding: 8px;
                text-align: left;
            }

            .hidden-column {
                display: none;
            }
            form {
                max-width: 400px;
                background-color: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                margin-bottom: 20px;
                margin-right: 10px;
            }
            /* Modal styles */
            .modal {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.7);
            }

            .modal-content {
                background-color: #fefefe;
                margin: 15% auto;
                padding: 20px;
                border: 1px solid #888;
                width: 30%;
            }
            .close {
                color: #0c5460;
                float: right;
                font-size: 28px;
                font-weight: bold;
                width: 3px;
            }

            .close:hover,
            .close:focus {
                color: black;
                text-decoration: none;
                cursor: pointer;
            }
        </style>
    </head>

    <body>
        <%@include file="component/nav.jsp" %>
        <%@include file="component/sidebar.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
            <div class="row">
                <ol class="breadcrumb">
                    <li><a href="productmanage">
                            <em class="fa fa-home"></em>
                        </a></li>
                    <li class="active">Home</li>
                </ol>

            </div><!--/.row-->

            <div class="row">
                <div class="col-lg-6 row" style="display: inline-block;">
                    <form action="couponmanage" method="post" > 
                        <div class="form-group col-lg-12 row " >
                            <input type="text" class="form-control"  name="search" placeholder="Search" style="height: 38px; width: 300px;" >                                     
                            <input type="submit"  value="Search" style="height: 38px; width: 100px;">  
                        </div>
                    </form>
                </div>

                <div class="col-lg-2" style="display: inline-block;">
                    <h2>Coupons</h2>
                </div>
            </div>
            <!--/.main-->
            <div class="row">
                <div class="tbl-header col-lg-12">
                    <label for="columnSelector">Select columns to display:</label>
                    <select id="columnSelector" multiple>
                        <option value="id" selected>ID</option>
                        <option value="code" selected>Code</option>
                        <option value="value" selected>Value</option>
                        <option value="type" selected>Type</option>
                        <option value="expirationDate" selected>Expiration Date</option>
                        <option value="Action" selected>Action</option>
                    </select>
                    <%
                      LocalDate currentDate = LocalDate.now();
                    %>
                    <table id="CouponTable">
                        <thead>
                            <tr>
                            <tr>
                                <th id="id">ID</th>
                                <th id="code">Code</th>
                                <th id="value">Value</th>
                                <th id="type">Type</th>
                                <th id="expirationDate">Expiration Date</th>
                                <th id="Action">Action</th>
                            </tr>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- danh sach tat ca coupon -->
                            <c:forEach items="${requestScope.dataAll}" var="da">
                                <tr>
                                    <td id="id" data-column="col1">${da.id}</td>
                                    <td id="code" data-column="col2">${da.code}</td>
                                    <td id="value" data-column="col3">${da.value}</td>
                                    <td id="type" data-column="col4">${da.type ? 'Giảm giá theo %' : 'Giảm giá theo VND'}</td>
                                    <td id="expirationDate" data-column="col5">${da.expirationDate}</td>
                                    <td id="Action" data-column="col17">
                                        <a href="updatecoupon?id=${da.id}"><li class=" fa fa-pencil-square-o"></li></a>
                                        &oline;&oline;
                                        <button type="button" id="openDeleteModalBtn" onclick="openDeleteModal('${da.id}')"><i class="fas fa-trash"></i></button>
                                    </td>

                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <c:if test="${requestScope.notfound != null}">
                        <div class="centered-message">
                            <p><i class="fas fa-exclamation-circle"></i>${requestScope.notfound}</p>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
        <!--modal cảnh báo khi người dùng muốn xóa-->
        <div id="modalDelete" class="modal">
            <div class="modal-content">
                <span id="close" class="close">&times;</span>
                <form name="fd" action="couponmanage" method="post">
                    <input type="text" name="id" id="idDelette" hidden="" >
                    <p>Are you sure want to delete this coupon?</p>
                    <div class="button-container2">
                        <button type="submit" id="deleteButton" style="background-color: #ffc107; "><i class="fas fa-check"></i> Xác Nhận</button>
                    </div>
                </form>
            </div>
        </div>

        <script src="jsadmin/jquery-1.11.1.min.js"></script>
        <script src="jsadmin/bootstrap.min.js"></script>
        <script src="jsadmin/chart.min.js"></script>
        <script src="jsadmin/chart-data.js"></script>
        <script src="jsadmin/easypiechart.js"></script>
        <script src="jsadmin/easypiechart-data.js"></script>
        <script src="jsadmin/bootstrap-datepicker.js"></script>
        <script src="jsadmin/custom.js"></script>
        <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
        <script>
                                            $(document).ready(function () {
                                                // Handle column selection change
                                                $('#columnSelector').change(function () {
                                                    var selectedColumns = $(this).val();

                                                    // Show/hide columns based on selection
                                                    $('#CouponTable th, #CouponTable td').each(function () {
                                                        var columnId = $(this).attr('id');
                                                        if (selectedColumns.includes(columnId)) {
                                                            $(this).removeClass('hidden-column');
                                                        } else {
                                                            $(this).addClass('hidden-column');
                                                        }
                                                    });
                                                });
                                            });
        </script>
        <script>
            function openDeleteModal(id) {
                var modal = document.getElementById("modalDelete");
// Get the <span> element that closes the modal
                var span = document.getElementById("close");
                // When the user clicks the button, open the modal 
                modal.style.display = "block";
                // Gán giá trị userContactID cho thuộc tính value
                document.getElementById("idDelette").value = id;
                // When the user clicks on <span> (x), close the modal
                span.onclick = function () {
                    closeModal();
                };

// When the user clicks anywhere outside of the modal, close it
                window.onclick = function (event) {
                    if (event.target == modal) {
                        closeModal();
                    }
                };
                // Function to close the modal
                function closeModal() {
                    modal.style.display = "none";
                }
            }
        </script>
    </body>
</html>
