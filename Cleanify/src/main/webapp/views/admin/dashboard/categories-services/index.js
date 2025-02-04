/**
 * Form validation and submission handler
 * @param {HTMLButtonElement} button - The button triggering the form submission
 */
function validateAndSubmit(button) {
    const form = button.form;
    const inputs = form.querySelectorAll("input, textarea, select");

    // Validate form inputs
    for (const input of inputs) {
        // Ignore file input fields
        if (input.type === "file") continue;

        // Trim spaces and validate
        if (input.value.trim() === "") {
            input.setCustomValidity(input.title || "This field cannot be empty or contain only spaces.");
        } else {
            input.setCustomValidity("");
        }

        if (!input.checkValidity()) {
            input.reportValidity();
            return false;
        }
    }

    // If all inputs are valid, disable the button and change the text
    const originalText = button.innerText; // Save the original button text
    button.disabled = true;
    if (button.classList.contains("btn-success")) {
        button.innerText = "Submitting...";
    } else if (button.classList.contains("btn-primary")) {
        button.innerText = "Saving...";
    } else if (button.classList.contains("btn-danger")) {
        button.innerText = "Deleting...";
    }

    // Submit the form asynchronously
    const formData = new FormData(form);

    fetch(form.action, {
        method: form.method,
        body: formData,
    })
        .then((response) => {
            if (response.ok) {
                // Form submission successful
                button.innerText = "Success!";
                setTimeout(() => {
                    button.innerText = originalText;
                    button.disabled = false;
                    return true;
                }, 2000);
            } else {
                // Form submission failed
                response.text().then((text) => alert(`Failed: ${text}`));
                button.innerText = originalText;
                button.disabled = false;
            }
        })
        .catch((error) => {
            // Handle fetch errors
            alert(`Error: ${error.message}`);
            button.innerText = originalText;
            button.disabled = false;
        });

    return false; // Prevent the default form submission
}


/**
 * Prevent card collapse when clicking specific buttons
 */
function preventCardCollapse() {
    document.querySelectorAll('.edit-icon, .delete-icon').forEach(button => {
        button.addEventListener('click', function (event) {
            event.preventDefault(); // Prevent default action (if any)
            event.stopPropagation(); // Stop the click from toggling the collapse
            // Allow modal to open normally
            const targetModal = button.getAttribute('data-target');
            if (targetModal) {
                $(targetModal).modal('show'); // Manually trigger the modal
            }
        });
    });
}

/**
 * Sorting functionality for categories
 */
function setupSorting(categoriesContainer) {
    function sortCategories(criteria, reverse = false) {
        const cards = Array.from(categoriesContainer.getElementsByClassName("category-card"));

        cards.sort((a, b) => {
            const nameA = a.getAttribute("data-name").toLowerCase();
            const nameB = b.getAttribute("data-name").toLowerCase();
            const servicesA = parseInt(a.getAttribute("data-services"), 10);
            const servicesB = parseInt(b.getAttribute("data-services"), 10);

            let comparison = 0;
            if (criteria === "name") {
                comparison = nameA.localeCompare(nameB);
            } else if (criteria === "services") {
                comparison = servicesA - servicesB;
            }

            return reverse ? -comparison : comparison;
        });

        // Append sorted cards back to the container
        cards.forEach(card => categoriesContainer.appendChild(card));
    }

    // Add event listeners for sort buttons
    document.getElementById("sortAlphabeticalAsc").addEventListener("click", () => sortCategories("name", false));
    document.getElementById("sortAlphabeticalDesc").addEventListener("click", () => sortCategories("name", true));
    document.getElementById("sortServicesAsc").addEventListener("click", () => sortCategories("services", false));
    document.getElementById("sortServicesDesc").addEventListener("click", () => sortCategories("services", true));
}




/**
 * Search functionality for categories and services
 */
function setupSearch(categoriesContainer) {
    const searchBar = document.getElementById("searchBar");
    const searchCategory = document.getElementById("searchCategory");
    const searchService = document.getElementById("searchService");

    function filterContent() {
        const query = searchBar.value.toLowerCase().trim();
        const searchInCategory = searchCategory.checked;
        const searchInService = searchService.checked;

        const cards = categoriesContainer.getElementsByClassName("category-card");

        Array.from(cards).forEach(card => {
            const categoryName = card.getAttribute("data-name").toLowerCase();
            const services = card.querySelectorAll("tbody tr");
            const collapseTarget = card.querySelector(".card-header").getAttribute("data-target");
            const collapseElement = document.querySelector(collapseTarget);

            let isVisible = false;
            let shouldExpand = false;

            // Default state: Collapse all cards
            collapseElement.classList.remove("show");

            // Check category match
            if (searchInCategory && categoryName.includes(query)) {
                isVisible = true; // Show card
                shouldExpand = false; // Keep the toggle collapsed for categories
            }

            // Check services match
            if (searchInService) {
                let serviceMatch = false;

                Array.from(services).forEach(service => {
                    const serviceName = service.querySelector("td:nth-child(3)").innerText.toLowerCase();
                    if (serviceName.includes(query)) {
                        serviceMatch = true;
                    }
                });

                if (serviceMatch) {
                    isVisible = true; // Show card
                    shouldExpand = true; // Expand the toggle for services
                }
            }

            // Toggle visibility and expand/collapse behavior
            card.style.display = isVisible ? "" : "none";

            if (shouldExpand) {
                collapseElement.classList.add("show");
            }
        });
    }

    searchBar.addEventListener("input", filterContent);
    searchCategory.addEventListener("change", filterContent);
    searchService.addEventListener("change", filterContent);
}




// Initialize all functionalities on DOM content loaded
document.addEventListener("DOMContentLoaded", function () {
    const categoriesContainer = document.getElementById("categoriesContainer");

    // Prevent card collapse for specific buttons
    preventCardCollapse();

    // Setup sorting functionality
    setupSorting(categoriesContainer);


    // Setup search functionality
    setupSearch(categoriesContainer);
});
