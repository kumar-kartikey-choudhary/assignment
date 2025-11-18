// --- Configuration ---
// This is the base URL of our Spring Boot backend.
const API_BASE_URL = 'http://localhost:9090/api';

// --- Event Listeners ---

/**
 * Main entry point. This function runs when the page is fully loaded.
 */
document.addEventListener('DOMContentLoaded', () => {
    // Loading dynamic content
    loadProjects();
    loadClients();

    // form submission handlers
    const contactForm = document.getElementById('contact-form');
    contactForm.addEventListener('submit', handleContactSubmit);

    const newsletterForm = document.getElementById('newsletter-form');
    newsletterForm.addEventListener('submit', handleNewsletterSubmit);
});


// --- Dynamic Content Loading ---

/**
 * Fetches projects from the backend and displays them in the #project-grid.
 */
async function loadProjects() {
    const grid = document.getElementById('project-grid');
    
    try {
        const response = await fetch(`${API_BASE_URL}/projects`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const projects = await response.json();

        // Clear the skeleton/loading card
        grid.innerHTML = ''; 

        if (projects.length === 0) {
            grid.innerHTML = '<p class="text-gray-600 col-span-full text-center">No projects available at the moment.</p>';
            return;
        }

        // Create and append a card for each project
        projects.forEach(project => {
            const card = document.createElement('div');
            card.className = 'bg-white rounded-lg shadow-lg overflow-hidden';
            
            // Use a placeholder if the image URL is broken
            const placeholderImg = `https://placehold.co/600x400/E2E8F0/94A3B8?text=${encodeURIComponent(project.name)}`;

            card.innerHTML = `
                <img src="${project.imageUrl}" 
                     alt="${project.name}" 
                     class="w-full h-56 object-cover"
                     onerror="this.src='${placeholderImg}'">
                <div class="p-6">
                    <h3 class="text-2xl font-bold text-gray-800 mb-3">${project.name}</h3>
                    <p class="text-gray-600 mb-6">${project.description}</p>
                    <button class="bg-blue-600 text-white px-5 py-2 rounded-md font-medium hover:bg-blue-700 transition duration-300">
                        Read More
                    </button>
                </div>
            `;
            grid.appendChild(card);
        });

    } catch (error) {
        console.error('Failed to load projects:', error);
        grid.innerHTML = '<p class="text-red-500 col-span-full text-center">Error: Could not load projects.</p>';
    }
}

/**
 * Fetches clients from the backend and displays them in the #client-grid.
 */
async function loadClients() {
    const grid = document.getElementById('client-grid');

    try {
        const response = await fetch(`${API_BASE_URL}/clients`);
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const clients = await response.json();

        // Clear the skeleton/loading card
        grid.innerHTML = ''; 

        if (clients.length === 0) {
            grid.innerHTML = '<p class="text-gray-600 col-span-full text-center">No client testimonials available.</p>';
            return;
        }

        // Create and append a card for each client
        clients.forEach(client => {
            const card = document.createElement('div');
            card.className = 'bg-white p-6 rounded-lg shadow-lg text-center';
            
            const placeholderImg = `https://placehold.co/100x100/E2E8F0/94A3B8?text=${encodeURIComponent(client.name[0])}`;

            card.innerHTML = `
                <img src="${client.imageUrl}" 
                     alt="${client.name}" 
                     class="w-20 h-20 rounded-full mx-auto mb-4 object-cover"
                     onerror="this.src='${placeholderImg}'">
                <p class="text-gray-600 text-sm italic mb-4">"${client.description}"</p>
                <h4 class="text-lg font-bold text-blue-600">${client.name}</h4>
                <p class="text-gray-500 text-sm">${client.designation}</p>
            `;
            grid.appendChild(card);
        });

    } catch (error) {
        console.error('Failed to load clients:', error);
        grid.innerHTML = '<p class="text-red-500 col-span-full text-center">Error: Could not load client testimonials.</p>';
    }
}


// --- Form Submission Handlers ---

/**
 * Handles the submission of the "Get a Free Consultation" form.
 */
async function handleContactSubmit(event) {
    event.preventDefault(); // Stop the form from reloading the page
    
    const form = event.target;
    const messageEl = document.getElementById('contact-message');
    
    // 1. Get data from the form
    const formData = new FormData(form);
    const data = Object.fromEntries(formData.entries());

    try {
        // 2. Send data to the backend
        const response = await fetch(`${API_BASE_URL}/contact`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        });

        if (!response.ok) {
            // Handle HTTP errors (e.g., 400, 500)
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }

        // 3. Handle success
        showMessage(messageEl, 'Thank you! We have received your quote request.', false);
        form.reset(); // Clear the form fields

    } catch (error) {
        // 4. Handle failure
        console.error('Contact form submission error:', error);
        showMessage(messageEl, `Error: ${error.message}`, true);
    }
}

/**
 * Handles the submission of the "Subscribe Us" newsletter form.
 */
async function handleNewsletterSubmit(event) {
    event.preventDefault();
    
    const form = event.target;
    const messageEl = document.getElementById('newsletter-message');
    const email = document.getElementById('newsletter-email').value;

    try {
        const response = await fetch(`${API_BASE_URL}/newsLetter`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email: email }), // Send as a JSON object
        });

        if (!response.ok) {
            // Get the specific error message from the backend (e.g., "already subscribed")
            const errorText = await response.text();
            throw new Error(errorText || `HTTP error! status: ${response.status}`);
        }

        // Handle success
        showMessage(messageEl, 'Thank you for subscribing!', false);
        form.reset();

    } catch (error) {
        // Handle failure (e.g., duplicate email)
        console.error('Newsletter submission error:', error);
        showMessage(messageEl, `Error: ${error.message}`, true);
    }
}


// --- Utility Functions ---

/**
 * Displays a success or error message in a specified element.
 * @param {HTMLElement} element - The HTML element to show the message in.
 * @param {string} message - The message text.
 * @param {boolean} isError - If true, styles the message as an error.
 */
function showMessage(element, message, isError = false) {
    element.textContent = message;
    if (isError) {
        element.classList.remove('text-white', 'text-green-300');
        element.classList.add('text-red-300'); // Use a lighter red for contrast on blue
    } else {
        element.classList.remove('text-red-300');
        // For newsletter (white text)
        element.classList.add('text-white');
        // For contact form (also white text on blue)
    }
    
    // Clear the message after 5 seconds
    setTimeout(() => {
        element.textContent = '';
    }, 5000);
}