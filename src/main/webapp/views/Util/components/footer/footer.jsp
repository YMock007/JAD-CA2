<%@ page import="java.time.LocalDate" %> 

<footer class="footer">
    <div class="text-center">
        <p>&copy; <%= LocalDate.now().getYear() %> Cleanify. All rights reserved.</p>
    </div>
</footer>

<style>
    .content {
        flex-grow: 1; 
    }

    .footer {;
        bottom: 0;
        left: 0;
        width: 100%;
        background: rgb(20,141,159);
        color: white;
        text-shadow: 2px 2px 3px #000;
        padding: 1.5rem 0;
        text-align: center;
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 999;
    }

    .footer::before {
        content: "";
        position: absolute;
        top: -10px;
        left: 0;
        width: 100%;
        height: 15px;
        background: linear-gradient(to top, rgba(20, 141, 159, 0.8), rgba(255, 255, 255, 0));
        filter: blur(5px);
        pointer-events: none;
    }

    .text-center {
        margin-top: 10px;
    }
</style>
