let currentPage = 0; // Keep track of the current page
const size = 3;

const isAdmin = document.querySelector('meta[name="is-admin"]').getAttribute('content') === 'true';
console.log(isAdmin);
function loadPage(page) {
    if (page < 0) return; // Prevent going to a negative page

    fetch(`http://localhost:8082/crossfit-community/events/all?page=${page}&size=${size}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json(); // Parse the response as JSON
        })
        .then(data => {
            console.log(data); // Log the data to check its structure
            updateEvents(data);
            currentPage = page;
            updatePaginationControls(data.page); // Pass the pagination metadata
        })
        .catch(error => console.error('Error fetching events:', error));
}

function updateEvents(data) {
    const container = document.getElementById('events-container');
    console.log('isAdmin:', isAdmin); // Debugging line

    if (!container) {
        console.error('Element with ID "events-container" not found.');
        return;
    }

    if (!data || !data.content) {
        console.error('Data or data.content is undefined');
        container.innerHTML = '<p>No events found.</p>';
        return;
    }

    console.log('Data content:', data.content); // Log content to verify it

    const eventsHtml = data.content.map(event => `
        <div class="col-lg-4 mb-4"> <!-- Use Bootstrap column classes for responsive grid -->
            <div class="single_event position-relative">
                <div class="event_video">
                    <iframe src="${event.videoUrl.replace('watch?v=', 'embed/')}" frameborder="0" allowfullscreen></iframe>
                </div>
                <div class="event_details">
                    <div class="d-flex mb-4">
                        <div class="date">
                            <span>${new Date(event.date).getDate()}</span>
                            <span>${new Date(event.date).toLocaleString('default', { month: 'short' })}</span>
                        </div>
                        <div class="time-location">
                            <p><span class="ti-time mr-2"></span><span>${new Date(event.date).toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' })}</span></p>
                            <p><span class="ti-location-pin mr-2"></span><span>${event.address}</span></p>
                        </div>
                    </div>
                    <p>${event.eventName}</p>
                    <p>${event.description}</p>
                    ${isAdmin ? `<button class="btn btn-danger btn-sm mt-2" onclick="deleteEvent(${event.id})">Delete</button>` : ''}
                </div>
            </div>
        </div>
    `).join('');

    container.innerHTML = `
        <div class="row">
            ${eventsHtml}
        </div>
    `;
}
// Function to handle event deletion
function deleteEvent(eventId) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch(`http://localhost:8082/crossfit-community/events/delete/${eventId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        }
    })
        .then(response => {
            if (response.ok) {
                alert('Event deleted successfully');
                loadPage(currentPage); // Reload the page after deletion
            } else {
                alert('Failed to delete event');
            }
        })
        .catch(error => console.error('Error deleting event:', error));
}


function updatePaginationControls(pageData) {
    const prevButton = document.getElementById('prev-page');
    const nextButton = document.getElementById('next-page');

    prevButton.disabled = currentPage <= 0;
    nextButton.disabled = currentPage >= pageData.totalPages - 1;
}

// Initial page load
loadPage(currentPage);