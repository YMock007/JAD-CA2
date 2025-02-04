<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Services.Service" %>
<%@ page import="Services.ServiceList" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@24,400,0,0&icon_names=arrow_forward" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"/>
  <style>
    @charset "UTF-8";
    @import url('https://fonts.googleapis.com/css2?family=Inter:opsz,wght@14..32,100..900&display=swap');

    .carouselSection {
      padding: 0;
      margin: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      min-height: 50vh;
    }

    .card-wrapper {
      margin: 0 60px 35px;
      padding: 20px 10px;
      overflow: hidden;
    }

    .card-list {
      padding: 0;
    }

    .card-list .card-item {
      list-style: none;
    }

    .card-list .card-item .card-link {
      user-select: none;
      display: block;
      background: #fff;
      padding: 18px;
      border-radius: 12px;
      text-decoration: none;
      box-shadow: 0 10px 10px rgba(0, 0, 0, 0.5);
      transition: 0.2s ease;
      border: 2px solid transparent;
    }

    .card-list .card-item .card-link:hover {
      border-color: #17a2b8;
    }

    .card-list .card-item .card-image {
      width: 100%;
      aspect-ratio: 16 / 9;
      object-fit: cover;
      border-radius: 10px;
    }

    .card-list .card-link .badge {
      color: #17a2b8;
      padding: 8px 16px;
      font-size: 1.2rem;
      margin: 16px 0 18px;
      background: #DDE4FF;
      width: fit-content;
      border-radius: 50px;
    }

    .card-list .card-link .card-button {
      height: 35px;
      width: 35px;
      border-radius: 50%;
      margin: 30px 0 5px;
      border: 2px solid #17a2b8;
      background: none;
      cursor: pointer;
      color: #17a2b8;
      transform: rotate(-45deg);
    }

    .card-list .card-link:hover .card-button {
      color: white;
      background: #17a2b8;
    }

    .card-wrapper .swiper-pagination-bullet {
      height: 13px;
      width: 13px;
      opacity: 0.5;
      background: #17a2b8;
    }

    .card-wrapper .swiper-pagination-bullet-active {
      opacity: 1;
    }

    @media screen and (max-width: 768px) {
      .card-wrapper {
        margin: 0 10px 25px;
      }
    }
  </style>
</head>
<body>

<section class="carouselSection">
  <div class="container swiper">
    <div class="card-wrapper">
      <h3 id="header-title" class="text-center mb-4" style="font-weight: bold; color: #17a2b8">
        <%-- Check if activeServiceId is available from session --%>
        <%
          String activeServiceId = (String) session.getAttribute("activeServiceId"); 
          if (activeServiceId != null) {
        %>
          Other Related Services
        <% } else { %>
          Our Services
        <% } %>
      </h3>

      <ul class="card-list swiper-wrapper">
        <%-- Loop through the services and display each one --%>
        <%
          List<Service> sServices = ServiceList.getAllServices();
          if (sServices.isEmpty()) {
        %>
          <p>No services available to display.</p>
        <%
          } else {
            for (Service sService : sServices) {
              if (activeServiceId == null || !activeServiceId.equals(String.valueOf(sService.getId()))) {
        %>
          <li class="card-item swiper-slide" id="service-<%= sService.getId() %>">
            <div class="card-link">
              <form action="${pageContext.request.contextPath}/serviceServlet" method="GET">
                <input type="hidden" name="service_id" value="<%= sService.getId() %>" />
                <button type="submit" class="btn btn-sm">
                  <img src="<%= sService.getImageUrl() %>"
                       alt="<%= sService.getName() %>" class="card-image">
                  <p class="badge"><%= sService.getName() %></p>
                </button>
              </form>
            </div>
          </li>
        <%
              }
            }
          }
        %>
      </ul>

      <div class="swiper-pagination"></div>
      <div class="swiper-button-prev"></div>
      <div class="swiper-button-next"></div>
    </div>
  </div>
</section>

<!-- Swiper JS -->
<script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
<script>
  document.addEventListener('DOMContentLoaded', function () {
    new Swiper('.card-wrapper', {
      loop: true, 
      spaceBetween: 30, 
      pagination: {
        el: '.swiper-pagination',
        clickable: true, 
        dynamicBullets: true,
      },
      navigation: {
        nextEl: '.swiper-button-next',  
        prevEl: '.swiper-button-prev',  
      },
      breakpoints: {
        0: {
          slidesPerView: 1,  
        },
        768: {
          slidesPerView: 2,  
        },
        1024: {
          slidesPerView: 3,  
        }
      }
    });
  });
</script>

</body>
</html>
