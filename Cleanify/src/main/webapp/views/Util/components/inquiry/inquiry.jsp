<script src="https://kit.fontawesome.com/015a0a8305.js" crossorigin="anonymous"></script>
<style>
#inquiry {
    font-family: "Inter", sans-serif;
    font-optical-sizing: auto;
    font-weight: 400;
    font-style: normal;
    padding: 30px;
}

#inquiryChildContainer{
	width: 1280px;
	margin: 0 auto;
	display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 40px;
}

#inquiryForm {
    width: 100%;
    display: flex;
    flex-direction: column;
}

#inquiryForm input,
#inquiryForm textarea {
    width: 100%;
    padding: 12px;
    margin-top: 5px;
    border: 1px solid #ccc;
    border-radius: 5px;
    font-size: 16px;
}

#inquiryForm label {
    font-weight: bold;
    margin-top: 15px;
}

#inquiryForm button {
    margin-top: 20px;
    padding: 12px;
    background: rgb(60,231,235);
	background: linear-gradient(163deg, rgba(60,231,235,0.980071252133666) 25%, rgba(23,125,108,1) 86%);
    border-radius: 50%;
    color: white;
    border: none;
    border-radius: 5px;
    font-size: 16px;
    cursor: pointer;
    transition: background 0.3s;
}	

#inquiryForm button:hover {
    background: rgb(74,199,202);
	background: linear-gradient(234deg, rgba(74,199,202,0.4870740532541141) 9%, rgba(138,255,236,0.8372141092765231) 86%);
}

#companyDetails {
    background: rgb(74,199,202);
	background: linear-gradient(234deg, rgba(74,199,202,0.8624241933101365) 32%, rgba(158,219,209,0.8372141092765231) 87%);
    padding: 30px;
    border-radius: 30px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items : flex-start;
    text-align: left;
}

#companyDetailsHeader h2 {
    font-size: 24px;
    margin-bottom: 10px;
}

#companyDetailsHeader p {
    font-size: 16px;
    color: #666;
}

.companyAddress, .companyPhone, .companyEmail {
    display: flex;
    align-items: flex-start;
    margin-top: 15px;
}

.icon {
    width: 40px;
    height: 40px;
    background: rgb(74,199,202);
	background: linear-gradient(234deg, rgba(74,199,202,0.4870740532541141) 32%, rgba(158,219,209,0.8372141092765231) 92%);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: white;
    font-size: 18px;
    margin-right: 15px;
}


@media (max-width: 768px) {
    #inquiry {
        grid-template-columns: 1fr;
        gap: 20px;
    }

    #personInquiry, #companyDetails {
        padding: 20px;
    }

    #inquiryForm input, #inquiryForm textarea {
        font-size: 14px;
    }

    #inquiryForm button {
        font-size: 14px;
    }

    .icon {
        width: 35px;
        height: 35px;
        font-size: 16px;
    }
}



</style>

<section id="inquiry">
    <div id="inquiryChildContainer">
    <div id="personInquiry">
	    <div class="inquiryHeader">
	        <h2>Get In Touch</h2>
	    </div>
	    <form action="${pageContext.request.contextPath}/Inquiry" method="post" id="inquiryForm">

	    <!-- Name Field -->
	    <div class="form-group">
	        <label for="name">Name</label>
	        <input type="text" id="name" name="name" placeholder="Enter your full name" required>
	    </div>
	
	    <!-- Email Field -->
	    <div class="form-group">
	        <label for="email">Email:</label>
	        <input type="email" id="email" name="email" placeholder="Enter your email address" required>
	    </div>
	
	    <!-- Problem Title -->
	    <div class="form-group">
	        <label for="title">Problem Title:</label>
	        <input type="text" id="title" name="title" placeholder="Briefly summarize the issue" required>
	    </div>
	
	    <!-- Description -->
	    <div class="form-group">
	        <label for="description">Description:</label>
	        <textarea id="description" name="description" rows="5" placeholder="Describe the problem in detail" required></textarea>
	    </div>
	
	    <!-- Submit Button -->
	    <button type="submit">Submit Inquiry</button>
	
	</form>

    </div>
    <div id="companyDetails">
    	<div id="companyDetailsHeader"> 
    		<h2>Contact Us</h2>
    		<p>We are open for any suggestions or just to have a chat</p>
    	</div>
		<!-- Company Address -->
		<div class="companyAddress">
		    <div class="icon">
		        <i class="fas fa-map-marker-alt"></i>
		    </div>
		    <div>
		        <p><em></em><b>Address: </b></em>
		        	Singapore Polytechnic
					500 Dover Road
					Singapore 139651
				</p>
		    </div>
		</div>
		
		<!-- Company Phone -->
		<div class="companyPhone">
		    <div class="icon">
		        <i class="fas fa-phone-alt"></i>
		    </div>
		    <div>
		        <p><em></em><b>Phone: </b></em>
		        	+65 1345678
				</p>
		    </div>
		</div>
		
		<!-- Company Email -->
		<div class="companyEmail">
		    <div class="icon">
		        <i class="fas fa-envelope"></i>
		    </div>
		    <div>
		        <p><em></em><b>Email: </b></em>
		        	cleanifymakeyoushine@gmail.com
				</p>
		    </div>
		</div>

    </div></div>
</section>
