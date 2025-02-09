<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CleanBot Solutions</title>
    <link rel="stylesheet" href="style.css">
    <style>
	    	/* Global Styles (Color-Enhanced) */
		body {
	    font-family: 'Arial', sans-serif;
	    line-height: 1.6;
	    color: #444; /* Darker text color */
	    margin: 0;
	    padding: 0;
	    background-color: #f4f8fb; /* Light blue-gray background */
		}
		
		.container {
		    max-width: 1200px;
		    margin: 0 auto;
		    padding: 0 20px;
		}
		
		h2 {
		    font-size: 2.5em;
		    text-align: center;
		    margin-bottom: 50px;
		    color: #3a7bc8; /* Primary color */
		}
		
		/* Header Styles (Colored) */
		header {
		    background-color: #fff;
		    box-shadow: 0 2px 5px rgba(0,0,0,0.1);
		    position: fixed;
		    width: 100%;
		    z-index: 1000;
		    padding: 20px 0;
		}
		
		.container {
		    display: flex;
		    justify-content: space-between;
		    align-items: center;
		}
		
		.logo {
		    font-size: 28px;
		    font-weight: bold;
		    color: #3a7bc8; /* Primary color */
		}
		
		nav ul {
		    display: flex;
		    list-style-type: none;
		    margin: 0;
		    padding: 0;
		}
		
		nav ul li {
		    margin-left: 30px;
		}
		
		nav ul li a {
		    text-decoration: none;
		    color: #555; /* Darker link color */
		    font-weight: 500;
		    transition: color 0.3s ease;
		}
		
		nav ul li a:hover {
		    color: #e44d26; /* Secondary color */
		}
		
		.btn {
		    background-color: #e44d26; /* Secondary color */
		    color: #fff;
		    padding: 12px 25px;
		    border-radius: 5px;
		    transition: background-color 0.3s ease;
		}
		
		.btn:hover {
		    background-color: #d43d16; /* Darker secondary color */
		}
		
		/* Hero Section */
        .hero {
            background: linear-gradient(135deg, #2563eb, #1d4ed8);
            height: 85vh;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
        }

        .hero-overlay {
            background: rgba(255, 255, 255, 0.15);
            padding: 50px;
            border-radius: 12px;
            box-shadow: 0 5px 10px rgba(0, 0, 0, 0.15);
        }

        .hero h1 {
            font-size: 50px;
            font-weight: bold;
            color: white;
        }

        .hero span {
            color: #facc15;
        }

        .hero p {
            font-size: 18px;
            color: white;
            margin-bottom: 20px;
        }

        .hero .cta-button {
            background: #facc15;
            color: black;
            font-size: 18px;
            box-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
        }

        .hero .cta-button:hover {
            background: #fcd34d;
        }
        .cta-button {
            background: #f97316;
            color: white;
            padding: 10px 20px;
            font-size: 16px;
            font-weight: bold;
            border-radius: 6px;
            text-decoration: none;
            transition: background 0.3s ease;
        }
		
		/* Services Section (Colored Cards) */
		.services {
		    padding: 100px 0;
		    background-color: #e9f1f7; /* Light background color */
		}
		
		.service-list {
		    display: grid;
		    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
		    gap: 30px;
		}
		
		.service-card {
		    background-color: #fff;
		    border-radius: 10px;
		    padding: 30px;
		    text-align: center;
		    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
		    transition: transform 0.3s ease;
		    border-bottom: 5px solid #3a7bc8; /* Primary color border */
		}
		
		.service-card:hover {
		    transform: translateY(-5px);
		}
		
		/* Why Us Section (Color Accents) */
		.why-us {
		    padding: 100px 0;
		}
		
		.benefits {
		    display: grid;
		    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
		    gap: 40px;
		}
		
		.benefit-card {
		    background-color: #fff;
		    border-radius: 10px;
		    padding: 30px;
		    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
		}
		
		.benefit-card h3 {
		    margin-bottom: 15px;
		    color: #e44d26; /* Secondary color */
		}
		
		/* FAQ Section (Consistent Styling) */
		.faq {
		    padding: 100px 0;
		    background-color: #e9f1f7; /* Light background color */
		}
		
		.faq-item {
		    background-color: #fff;
		    border-radius: 10px;
		    padding: 30px;
		    margin-bottom: 25px;
		    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
		}
		
		.faq-question {
		    font-size: 1.2em;
		    font-weight: bold;
		    margin-bottom: 10px;
		    color: #3a7bc8; /* Primary color */
		}
		
		.faq-answer {
		    line-height: 1.7;
		}
		
		/* Contact Section (Improved Spacing) */
		.contact {
		    padding: 100px 0;
		    text-align: center;
		    background-color: #fff;
		}
		
		.contact p {
		    font-size: 1.2em;
		    margin-bottom: 20px;
		}
		
		/* Footer Styles (Refined) */
		footer {
		    background-color: #333;
		    color: #fff;
		    padding: 60px 0;
		}
		
		.footer-content {
		    display: flex;
		    justify-content: space-around;
		    align-items: center;
		    flex-wrap: wrap;
		}
		
		.footer-logo {
		    font-size: 28px;
		    font-weight: bold;
		}
		
		.footer-links a, .footer-social a {
		    color: #fff;
		    text-decoration: none;
		    margin-right: 25px;
		    transition: color 0.3s ease;
		}
		
		.footer-links a:hover, .footer-social a:hover {
		    color: #f9d71c; /* Accent color */
		}
		
		.footer-bottom {
		    text-align: center;
		    margin-top: 30px;
		    padding-top: 30px;
		    border-top: 1px solid rgba(255,255,255,0.1);
		}
	    	
	</style>
</head>
<body>

    <header>
        <div class="container">
            <div class="logo">CleanBot Solutions</div>
            <nav>
                <ul>
                    <li><a href="#services">Services</a></li>
                    <li><a href="#why-us">Why Work With Us?</a></li>
                    <li><a href="#faq">FAQ</a></li>
                    <li><a href="#contact">Contact</a></li>
                    <li><a href="${pageContext.request.contextPath}/views/registration/logIn.jsp" class="btn">Join as Worker</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <section class="hero">
        <div class="hero-overlay">
            <h1>Join <span>CleanBot Solutions</span> & Start Earning</h1>
            <p>Flexible work. Instant pay. Verified jobs.</p>
            <a href="${pageContext.request.contextPath}/views/registration/logIn.jsp" class="cta-button">Get Started</a>
        </div>
    </section>

    <section id="services" class="services">
        <h2>Our Services</h2>
        <div class="service-list">
            <div class="service-card">🧹 Cleaning</div>
            <div class="service-card">🔧 Repair</div>
            <div class="service-card">🏠 Maintenance</div>
            <div class="service-card">🐜 Pest Control</div>
            <div class="service-card">⚙️ Installation</div>
        </div>
    </section>

    <section id="why-us" class="why-us">
        <h2>Why Work With Us?</h2>
        <div class="benefits">
            <div class="benefit-card">
                <h3>📅 Flexible Schedule</h3>
                <p>Work when you want. Choose your jobs.</p>
            </div>
            <div class="benefit-card">
                <h3>💰 Get Paid Instantly</h3>
                <p>Receive payments immediately after completing a job.</p>
            </div>
            <div class="benefit-card">
                <h3>✅ Safe & Verified Jobs</h3>
                <p>We only list verified and trustworthy jobs.</p>
            </div>
            <div class="benefit-card">
                <h3>📈 Boost Your Income</h3>
                <p>Complete more jobs and get higher pay.</p>
            </div>
        </div>
    </section>

    <section id="faq" class="faq">
        <h2>Frequently Asked Questions</h2>
        <div class="faq-item">
            <h3 class="faq-question">How do I apply for jobs?</h3>
            <p class="faq-answer">Sign up, verify your profile, and start browsing jobs. Click 'Apply' to begin.</p>
        </div>
    </section>

    <section id="contact" class="contact">
        <h2>Contact Us</h2>
        <p>Email: support@cleanbot.com | Phone: +65 1234 5678</p>
    </section>

    <footer>
        <div class="footer-content">
            <div class="footer-logo">CleanBot Solutions</div>
            <div class="footer-links">
                <a href="#services">Services</a>
                <a href="#why-us">Why Us?</a>
                <a href="#faq">FAQ</a>
                <a href="#contact">Contact</a>
            </div>
            <div class="footer-social">
                <a href="#">🔵 Facebook</a>
                <a href="#">🐦 Twitter</a>
                <a href="#">📸 Instagram</a>
            </div>
        </div>
        <p class="footer-bottom">© 2025 CleanBot Solutions. All rights reserved.</p>
    </footer>

</body>
</html>
