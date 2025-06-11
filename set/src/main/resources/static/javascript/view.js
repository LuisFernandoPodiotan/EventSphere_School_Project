document.addEventListener('DOMContentLoaded', () => {
    const eventsContainer = document.getElementById('events-container');
    const searchInput = document.getElementById('search-input');

    // Ensure the search input exists before adding event listeners
    if (searchInput) {
        searchInput.addEventListener('input', searchEvents);
    }

    // Fetch and display events based on search query
    async function searchEvents() {
        const query = searchInput.value.trim();
        if (query === '') {
            fetchEvents();
            return;
        }

        try {
            const response = await fetch(`/api/events/search?query=${encodeURIComponent(query)}`);
            if (!response.ok) {
                throw new Error('Failed to search events');
            }
            const events = await response.json();
            displayEvents(events);
        } catch (error) {
            console.error('Error searching events:', error);
            eventsContainer.innerHTML = '<p>Unable to search events. Please try again later.</p>';
        }
    }

    // Function to fetch all events (default)
    async function fetchEvents() {
        try {
            const response = await fetch('/api/events');
            if (!response.ok) {
                throw new Error('Failed to fetch events');
            }
            const events = await response.json();
            displayEvents(events);
        } catch (error) {
            console.error('Error fetching events:', error);
            eventsContainer.innerHTML = '<p>Unable to load events. Please try again later.</p>';
        }
    }

    // Function to display events with Join Button
    function displayEvents(events) {
        eventsContainer.innerHTML = ''; // Clear previous events

        if (!events || events.length === 0) {
            eventsContainer.innerHTML = '<p>No events found.</p>';
            return;
        }

        events.forEach(events => {
            const eventCard = document.createElement('div');
            eventCard.classList.add('event-card');

            eventCard.innerHTML = `
                <div class="event-title">${events.title}</div>
                <div class="event-details">
                    <p><strong>Date:</strong> ${new Date(events.eventDate).toLocaleDateString()}</p>
                    <p><strong>Location:</strong> ${events.location}</p>
                    <p>${events.description}</p>
                </div>
                <button class="join-btn" data-event-id="${events.id}">Join Event</button>
            `;

            eventsContainer.appendChild(eventCard);
        });

        // Attach event listeners to Join buttons
        document.querySelectorAll('.join-btn').forEach(button => {
            button.addEventListener('click', () => {
                const eventsId = button.getAttribute('data-event-id');
                joinEvent(eventsId, button);
            });
        });
    }

    // Function to Join an Event
	async function joinEvent(eventId, button) {
	    try {
	        const response = await fetch(`/api/events/${eventId}/join`, {
	            method: 'POST',
	            headers: { 'Content-Type': 'application/json' }
	        });

	        if (!response.ok) {
	            throw new Error('Failed to join event');
	        }

	        // Update UI
	        button.textContent = "Joined ✔";
	        button.disabled = true;
	        button.style.backgroundColor = "gray";
	        alert("You have successfully joined the event!");

	    } catch (error) {
	        console.error("Error joining event:", error);
	        alert("Unable to join event. Please try again.");
	    }
	}

    // Initial fetch of all events
    fetchEvents();
});
