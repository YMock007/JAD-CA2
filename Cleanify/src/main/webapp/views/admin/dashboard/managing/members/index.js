document.addEventListener("DOMContentLoaded", function () {
    // Form validation and submission
    function validateAndSubmit(form) {
        let valid = true;
        const inputs = form.querySelectorAll("input, select");
        inputs.forEach(input => {
            if (input.required && !input.value.trim()) {
                input.classList.add("is-invalid");
                valid = false;
            } else {
                input.classList.remove("is-invalid");
            }
        });

        if (valid) {
            form.submit();
        }
    }

    // Attach validate and submit logic to forms
    document.querySelectorAll("form").forEach(form => {
        form.addEventListener("submit", function (event) {
            event.preventDefault();
            validateAndSubmit(form);
        });
    });

    // Handle modal visibility
    document.querySelectorAll("[data-toggle='modal']").forEach(button => {
        button.addEventListener("click", function () {
            const modalId = button.getAttribute("data-target");
            const modalElement = document.querySelector(modalId);
            if (modalElement) {
                $(modalElement).modal("show");
            }
        });
    });
});
