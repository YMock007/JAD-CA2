<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cleanify - Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://kit.fontawesome.com/015a0a8305.js" crossorigin="anonymous"></script>
    <style>
        body {
    		font-family: Arial, sans-serif;
            background: linear-gradient(#eceffe, #ced6fb);
}


        /* Intro Section */
        #intro-section {
            min-height: 82vh;
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 2rem;
        }

        .intro-text-container {
            width: 50%;
        }

        .intro-title {
            font-size: 4rem;
            font-weight: 700;
            color: #00bcd4;
            text-transform: uppercase;
            letter-spacing: 2px;
            text-shadow: 0 4px 6px rgba(0, 0, 0, 1);
        }

        .intro-description {
            font-size: 1.3rem;
            line-height: 1.6;
            color: #fff;
            text-shadow: 2px 2px 3px #000;
        }

        .service-cards-container {
            display: flex;
            justify-content: space-around;
            flex-wrap: wrap;
            padding: 2rem;
        }
        
        .service-cards-container h2{
        	color: #474747
        }

        .service-card {
            width: 30%;
            background-color: #f8f9fa;
            border-radius: 10px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2); 
            padding: 2rem;
            text-align: center;
            margin: 1rem 0;
            transition: all 0.3s ease-in-out;
        }

        .service-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 12px 30px rgba(0, 0, 0, 0.3);
        }

        .service-card h3 {
            font-size: 1.8rem;
            color: #00bcd4;
            margin-bottom: 1rem;
        }

        .service-card p {
            font-size: 1.1rem;
            color: #333;
        }

        /* Testimonials Section */
        .testimonials-section {
            color: #fff;
            padding: 4rem 2rem;
            text-align: center;
        }

        .testimonials-section h2 {
            font-size: 2.5rem;
            margin-bottom: 2rem;
            color: #474747;
        }

        .testimonial-container {
            display: grid;
            grid-template-columns: repeat(3, 1fr); 
            gap: 2rem;
        }

        .testimonial {
            background-color: #fff;
            color: #333;
            border-radius: 8px;
            padding: 2rem;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease-in-out;
        }

        .testimonial:hover {
            transform: translateY(-5px); 
            box-shadow: 0 12px 30px rgba(0, 0, 0, 0.2); 
        }

        .testimonial p {
            font-size: 1.2rem;
            line-height: 1.5;
        }

        .testimonial h4 {
            font-size: 1.4rem;
            font-weight: 600;
            margin-top: 1rem;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            #intro-section {
                flex-direction: column;
                text-align: center;
            }

            .intro-text-container {
                width: 100%;
                margin-bottom: 2rem;
            }

            .service-card {
                width: 100%;
            }

            .testimonial-container {
                grid-template-columns: 1fr 1fr; 
            }
        }

        @media (max-width: 576px) {
            .testimonial-container {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>

        <%@ include file="/views/Util/components/header/header.jsp" %>

    <!-- Intro Section -->
    <div id="intro-section">
        <div class="intro-text-container">
            <h2 class="intro-title"><span>Welcome to</span> Cleanify!</h2>
            <p class="intro-description">
                At Cleanify, we provide <span style="color: #00bcd4; font-weight: bold;">expert cleaning services</span> tailored to your needs.
                From cozy homes to busy offices, we ensure every space shines with pristine cleanliness.
            </p>
        </div>
        <div id="imgContainer">
            <img src="<%= request.getContextPath() %>/resources/serviceImg/serviceMain.jpg" alt="Cleanify Services" />
        </div>
    </div>

    <!-- How We Care and How We Clean Sections -->
	<section class="services-section">
	    <h2 class="section-header text-center">Our Commitment to Excellence</h2>
	    <div class="service-cards-container">

	        <div class="service-card">
	            <h3>How We Care</h3>
	            <p>We prioritize customer satisfaction by listening to your needs and offering services that ensure peace of mind. Whether it’s for your home or office, we care for every detail.</p>
	        </div>
	
	        <div class="service-card">
	            <h3>How We Clean</h3>
	            <p>Our team uses eco-friendly products and state-of-the-art cleaning techniques to ensure your space is spotless and safe. We pay attention to every corner and surface, leaving your environment fresh and sanitized.</p>
	        </div>

	        <div class="service-card">
	            <h3>Why Choose Us</h3>
	            <p>With years of experience in the industry, Cleanify guarantees exceptional quality and reliability. We understand the value of your time, and we strive to provide services that are quick, effective, and affordable.</p>
	        </div>
	    </div>
	</section>



    <!-- Testimonials Section -->
    <div class="testimonials-section">
        <h2 >What Our Clients Say</h2>
        <div class="testimonial-container">
            <div class="testimonial">
                <p>"Cleanify transformed my office space! The team was punctual, thorough, and left everything spotless. Highly recommend their services!"</p>
                <h4>- John D.</h4>
            </div>
            <div class="testimonial">
                <p>"I love how Cleanify uses eco-friendly products. It's great to know that my home is clean and safe for my family!"</p>
                <h4>- Sarah L.</h4>
            </div>
            <div class="testimonial">
                <p>"I hired Cleanify to clean my apartment, and I was beyond impressed! They were professional, friendly, and left my home sparkling clean!"</p>
                <h4>- Emily R.</h4>
            </div>
            <div class="testimonial">
                <p>"We’ve used Cleanify for our office cleaning services for months now, and they consistently deliver excellent results. Great service, great value!"</p>
                <h4>- Mark S.</h4>
            </div>
            <div class="testimonial">
                <p>"I booked Cleanify for a one-time deep clean of my home. The team did a fantastic job! I will definitely be using them again." </p>
                <h4>- Rachel W.</h4>
            </div>
            <div class="testimonial">
                <p>"Cleanify is my go-to for cleaning services. Their attention to detail is unmatched, and I love that they use sustainable cleaning products." </p>
                <h4>- Michael T.</h4>
            </div>
        </div>
    </div>

    <%@ include file="/views/Util/components/swiper/serviceSwiper.jsp" %>
    <%@ include file="/views/Util/notification.jsp" %>
	<%@ include file="/views/Util/components/footer/footer.jsp" %>
</body>
</html>
