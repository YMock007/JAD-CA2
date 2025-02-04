document.addEventListener("DOMContentLoaded", () => {
    // Handle star rating change
    document.querySelectorAll('.rating-stars input').forEach(star => {
        star.addEventListener('change', function () {
            const rating = parseInt(this.value, 10); // Get the selected star's value as an integer
            document.querySelectorAll('.rating-stars label').forEach(label => {
                const labelValue = parseInt(label.htmlFor.replace('star', ''), 10);
                label.style.color = labelValue <= rating ? 'gold' : 'gray'; 
            });
        });
    });

    // Pass booking ID to modal
    document.querySelectorAll('[data-bs-toggle="modal"]').forEach(button => {
        button.addEventListener('click', function () {
            const bookingId = this.getAttribute('data-booking-id');
            document.getElementById('modalBookingId').value = bookingId;

            // Set default rating to 5 stars
            document.querySelector('#star5').checked = true;
            document.querySelectorAll('.rating-stars label').forEach(label => {
                const labelValue = parseInt(label.htmlFor.replace('star', ''), 10);
                label.style.color = labelValue <= 5 ? 'gold' : 'gray';
            });
        });
    });
});
