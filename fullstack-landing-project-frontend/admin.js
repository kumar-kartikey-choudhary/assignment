/**
 * Admin Panel JavaScript
 * * Handles tab switching, CRUD operations for Projects and Clients,
 * and the AI Chatbot interface.
 */

document.addEventListener('DOMContentLoaded', () => {
    // Escape utility function for safe rendering (assuming this exists or needs to be added)
    // NOTE: This function is required by the render* functions and was not provided.
    const escapeHTML = (str) => {
        if (!str) return '';
        return str.replace(/&/g, '&amp;')
                  .replace(/</g, '&lt;')
                  .replace(/>/g, '&gt;')
                  .replace(/"/g, '&quot;')
                  .replace(/'/g, '&#039;');
    };

    // --- Tab Switching Logic ---
    const tabs = document.querySelectorAll('.tab-button');
    const panels = document.querySelectorAll('.tab-panel');

    tabs.forEach(tab => {
        tab.addEventListener('click', () => {
            // Deactivate all tabs
            tabs.forEach(t => {
                t.classList.remove('border-blue-500', 'text-blue-600');
                t.classList.add('border-transparent', 'text-gray-500', 'hover:text-gray-700', 'hover:border-gray-300');
                t.setAttribute('aria-current', 'false');
            });

            // Deactivate all panels
            panels.forEach(p => {
                p.classList.add('hidden');
            });

            // Activate the clicked tab
            tab.classList.add('border-blue-500', 'text-blue-600');
            tab.classList.remove('border-transparent', 'text-gray-500', 'hover:text-gray-700', 'hover:border-gray-300');
            tab.setAttribute('aria-current', 'page');

            // Activate the corresponding panel
            const targetPanelId = tab.id.replace('tab-', 'panel-');
            const targetPanel = document.getElementById(targetPanelId);
            if (targetPanel) {
                targetPanel.classList.remove('hidden');
            }
        });
    });

    // --- Project Management ---
    const projectForm = document.getElementById('project-form');
    const projectList = document.getElementById('project-list');
    const projectsLoading = document.getElementById('projects-loading');
    const formTitle = document.getElementById('form-title');
    const cancelEditBtn = document.getElementById('cancel-edit-btn');
    const projectIdField = document.getElementById('projectId');

    // The projectIdField holds a value to indicate if we're updating. 
    // In the new backend, we use the project name for PUT/DELETE, so we'll 
    // use a temporary hidden field called 'currentProjectName' for update context.
    // Assuming the Project form has 'projectName' as an input.

    // --- Configuration ---
    // Assuming your Spring Boot backend runs on http://localhost:9090
    const API_BASE_URL = 'http://localhost:9090/api';


    // --- Project Management ---
    // GET all projects: /projects
    const PROJECTS_API_URL = `${API_BASE_URL}/projects`;
    // POST project: /admin/project
    const PROJECT_CREATE_URL = `${API_BASE_URL}/admin/project`;
    // PUT/DELETE project: /admin/project/{projectName}
    const PROJECT_BASE_UPDATE_DELETE_URL = `${API_BASE_URL}/admin/project`;


    /**
     * Renders the list of projects.
     * @param {Array} projects - An array of project objects.
     */
    const renderProjects = (projects) => {
        if (projects.length === 0) {
            projectList.innerHTML = '<p class="text-gray-500">No projects found. Add one above!</p>';
            return;
        }

        // Use project.name for identifying the item to edit/delete
        projectList.innerHTML = projects.map(project => `
            <div class="flex items-center justify-between p-4 border rounded-md shadow-sm" data-name="${escapeHTML(project.name)}">
                <div>
                    <h4 class="text-lg font-semibold">${escapeHTML(project.name)}</h4>
                    <p class="text-sm text-gray-600">${escapeHTML(project.category)}</p>
                    <p class="text-sm text-gray-500">${escapeHTML(project.description)}</p>
                </div>
                <div class="flex-shrink-0 flex space-x-2">
                    <button class="edit-project-btn bg-yellow-500 text-white px-3 py-1 rounded-md text-sm font-medium hover:bg-yellow-600">Edit</button>
                    <button class="delete-project-btn bg-red-600 text-white px-3 py-1 rounded-md text-sm font-medium hover:bg-red-700">Delete</button>
                </div>
            </div>
        `).join('');
    };

    /**
     * Fetches all projects from the API and renders them.
     */
    const loadProjects = async () => {
        try {
            const response = await fetch(PROJECTS_API_URL); // GET /projects
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const projects = await response.json();
            renderProjects(projects);
            projectsLoading.classList.add('hidden');
        } catch (error) {
            console.error('Error fetching projects:', error);
            projectList.innerHTML = '<p class="text-red-500">Error loading projects. Please try again.</p>';
            projectsLoading.classList.add('hidden');
        }
    };

    /**
     * Resets the project form to its default state.
     */
    const resetProjectForm = () => {
        projectForm.reset();
        // Clear the ID/name fields used for context in update/create logic
        projectIdField.value = ''; // Used to determine if it's an update

        formTitle.textContent = 'Add New Project';
        cancelEditBtn.classList.add('hidden');
    };

    // Handle Project Form Submission (Add/Update)
    projectForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const formData = new FormData(projectForm);
        const project = Object.fromEntries(formData.entries());
        // Assume 'projectName' is the name attribute of the project name input field
        const projectName = document.getElementById('projectName').value;
        const isUpdate = !!projectIdField.value; // Check if projectIdField is populated for an update

        const method = isUpdate ? 'PUT' : 'POST';
        // If it's an update, use the current name in the path. Otherwise, use the POST URL.
        const url = isUpdate ? `${PROJECT_BASE_UPDATE_DELETE_URL}/${projectName}` : PROJECT_CREATE_URL;

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                },
                // Send the whole project object as body for both POST and PUT
                body: JSON.stringify(project),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
            }

            resetProjectForm();
            loadProjects(); // Reload the list
        } catch (error) {
            console.error('Error saving project:', error);
            alert(`Error saving project: ${error.message}`);
        }
    });

    // Handle Project List Clicks (Edit/Delete)
    projectList.addEventListener('click', async (e) => {
        const target = e.target;
        // Use data-name for identification
        const projectItem = target.closest('[data-name]');
        if (!projectItem) return;

        const name = projectItem.dataset.name; // Use the project name for API calls

        // Handle Delete
        if (target.classList.contains('delete-project-btn')) {
            if (!confirm('Are you sure you want to delete this project?')) {
                return;
            }
            try {
                // DELETE /admin/project/{projectName}
                const response = await fetch(`${PROJECT_BASE_UPDATE_DELETE_URL}/${name}`, {
                    method: 'DELETE',
                });
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                loadProjects(); // Reload the list
            } catch (error) {
                console.error('Error deleting project:', error);
                alert('Error deleting project. Please try again.');
            }
        }

        // Handle Edit
        if (target.classList.contains('edit-project-btn')) {
            try {
                // Fetching by Name (Assuming the backend supports /projects/{projectName} or similar)
                const response = await fetch(`${PROJECTS_API_URL}/${name}`);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const project = await response.json();

                // Populate the form
                // Populate projectIdField to mark the form as an 'Edit'
                projectIdField.value = 'editing';
                document.getElementById('projectName').value = project.name;
                document.getElementById('imageUrl').value = project.imageUrl;
                document.getElementById('description').value = project.description;
                document.getElementById('category').value = project.category;

                formTitle.textContent = 'Edit Project';
                cancelEditBtn.classList.remove('hidden');
                projectForm.scrollIntoView({ behavior: 'smooth' });
            } catch (error) {
                console.error('Error fetching project for edit:', error);
                alert('Error fetching project details. Please try again. (Check if a GET by name endpoint is available)');
            }
        }
    });

    // Handle Cancel Edit
    cancelEditBtn.addEventListener('click', resetProjectForm);


    // --- Client Management ---
    const clientForm = document.getElementById('client-form');
    const clientList = document.getElementById('client-list');
    const clientsLoading = document.getElementById('clients-loading');
    const clientFormTitle = document.getElementById('client-form-title');
    const clientCancelEditBtn = document.getElementById('client-cancel-edit-btn');
    const clientIdField = document.getElementById('clientId'); // Used to signal 'Edit' mode

    // --- Client Management ---
    // GET all clients: /clients
    const CLIENTS_API_URL = `${API_BASE_URL}/clients`;
    // POST client: /admin/client
    const CLIENT_CREATE_URL = `${API_BASE_URL}/admin/client`;
    // PUT/DELETE client: /admin/client/{clientName}
    const CLIENT_BASE_UPDATE_DELETE_URL = `${API_BASE_URL}/admin/client`;
    /**
     * Renders the list of clients.
     * @param {Array} clients - An array of client objects.
     */
    const renderClients = (clients) => {
        if (clients.length === 0) {
            clientList.innerHTML = '<p class="text-gray-500">No clients found. Add one above!</p>';
            return;
        }

        // Use client.name for identifying the item to edit/delete as per the new API
        clientList.innerHTML = clients.map(client => `
            <div class="flex items-center justify-between p-4 border rounded-md shadow-sm" data-name="${escapeHTML(client.name)}">
                <div class="flex items-center space-x-4">
                    <img src="${escapeHTML(client.avatarUrl)}" alt="${escapeHTML(client.name)}" class="w-16 h-16 rounded-full object-cover" onerror="this.src='https://placehold.co/100x100';">
                    <div>
                        <h4 class="text-lg font-semibold">${escapeHTML(client.name)}</h4>
                        <p class="text-sm text-gray-600">${escapeHTML(client.company)}</p>
                        <p class="text-sm text-gray-500 italic">"${escapeHTML(client.testimonial)}"</p>
                    </div>
                </div>
                <div class="flex-shrink-0 flex space-x-2">
                    <button class="edit-client-btn bg-yellow-500 text-white px-3 py-1 rounded-md text-sm font-medium hover:bg-yellow-600">Edit</button>
                    <button class="delete-client-btn bg-red-600 text-white px-3 py-1 rounded-md text-sm font-medium hover:bg-red-700">Delete</button>
                </div>
            </div>
        `).join('');
    };

    /**
     * Fetches all clients from the API and renders them.
     */
    const loadClients = async () => {
        try {
            const response = await fetch(CLIENTS_API_URL); // GET /clients
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const clients = await response.json();
            renderClients(clients);
            clientsLoading.classList.add('hidden');
        } catch (error) {
            console.error('Error fetching clients:', error);
            clientList.innerHTML = '<p class="text-red-500">Error loading clients. Please try again.</p>';
            clientsLoading.classList.add('hidden');
        }
    };

    /**
     * Resets the client form to its default state.
     */
    const resetClientForm = () => {
        clientForm.reset();
        clientIdField.value = ''; // Clear ID/context field
        clientFormTitle.textContent = 'Add New Client';
        clientCancelEditBtn.classList.add('hidden');
    };

    // Handle Client Form Submission (Add/Update)
    clientForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        // --- START OF MODIFIED CLIENT SUBMISSION LOGIC ---
        
        // Use document.getElementById to reliably get values from the form inputs
        const clientNameInput = document.getElementById('clientName');
        const imageUrlInput = document.getElementById('imageUrl');
        const descriptionInput = document.getElementById('description');
        const designationInput = document.getElementById('designation');
    

        // Manually construct the client object using the DTO field names 
        // (uuid, imageUrl, name, description, designation).
        // Fields not collected by the form (like uuid) are set to null.
        const client = {
            uuid: null, 
            // DTO Field: imageUrl (maps to HTML ID: avatarUrl)
            imageUrl: imageUrlInput ? imageUrlInput.value : '', 
            
            // DTO Field: name (maps to HTML ID: clientName)
            name: clientNameInput ? clientNameInput.value : '',
            
            // DTO Field: description (maps to HTML ID: testimonial)
            description: descriptionInput ? descriptionInput.value : '',
            
            // DTO Field: designation (maps to HTML ID: designation)
            designation: designationInput ? designationInput.value : '', 

        };
        
        const clientName = client.name;
        const isUpdate = !!clientIdField.value; // Check if clientIdField is populated for an update
        
        // --- END OF MODIFIED CLIENT SUBMISSION LOGIC ---

        const method = isUpdate ? 'PUT' : 'POST';
        // If it's an update, use the current name in the path. Otherwise, use the POST URL.
        const url = isUpdate ? `${CLIENT_BASE_UPDATE_DELETE_URL}/${clientName}` : CLIENT_CREATE_URL;

        try {
            const response = await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(client),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
            }

            resetClientForm();
            loadClients(); // Reload the list
        } catch (error) {
            console.error('Error saving client:', error);
            alert(`Error saving client: ${error.message}`);
        }
    });

    // Handle Client List Clicks (Edit/Delete)
    clientList.addEventListener('click', async (e) => {
        const target = e.target;
        // Use data-name for identification
        const clientItem = target.closest('[data-name]');
        if (!clientItem) return;

        const name = clientItem.dataset.name; // Use the client name for API calls

        // Handle Delete
        if (target.classList.contains('delete-client-btn')) {
            if (!confirm('Are you sure you want to delete this client?')) {
                return;
            }
            try {
                // DELETE /admin/client/{clientName}
                const response = await fetch(`${CLIENT_BASE_UPDATE_DELETE_URL}/${name}`, {
                    method: 'DELETE',
                });
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                loadClients(); // Reload the list
            } catch (error) {
                console.error('Error deleting client:', error);
                alert('Error deleting client. Please try again.');
            }
        }

        // Handle Edit
        if (target.classList.contains('edit-client-btn')) {
            try {
                // Assuming a GET by client name is available on the /clients endpoint, 
                // similar to the project logic, since no specific GET by name endpoint was provided.
                const response = await fetch(`${CLIENTS_API_URL}/${name}`);
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const client = await response.json();

                // Populate the form
                // Populate clientIdField to mark the form as an 'Edit'
                clientIdField.value = 'editing';
                document.getElementById('clientName').value = client.name;
                document.getElementById('imageUrl').value = client.imageUrl;
                document.getElementById('description').value = client.description;
                document.getElementById('designation').value = client.designation;

                // NOTE: If you add a designation field to your HTML, you must populate it here too:
                // document.getElementById('designation').value = client.designation;

                clientFormTitle.textContent = 'Edit Client';
                clientCancelEditBtn.classList.remove('hidden');
                clientForm.scrollIntoView({ behavior: 'smooth' });
            } catch (error) {
                console.error('Error fetching client for edit:', error);
                alert('Error fetching client details. Please try again. (Check if a GET by name endpoint is available)');
            }
        }
    });

    // Handle Cancel Edit
    clientCancelEditBtn.addEventListener('click', resetClientForm);


    // --- Initial Data Load ---
    loadProjects();
    loadClients();
});