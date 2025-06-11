document.addEventListener('DOMContentLoaded', () => {
    const eventForm = document.getElementById('eventForm');
   
    const API_BASE_URL = 'http://localhost:8080/api/events';

    // Fetch and display events

    // Create a new event
    eventForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const event = {
            title: document.getElementById('title').value,
            description: document.getElementById('description').value,
            startTime: document.getElementById('startTime').value,
            endTime: document.getElementById('endTime').value,
            location: document.getElementById('location').value,
            organizerName: document.getElementById('organizerName').value,
            maxParticipants: parseInt(document.getElementById('maxParticipants').value)
        };

        try {
            const response = await fetch(API_BASE_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(event)
            });

            if (response.ok) {
                alert('Event created successfully!');
                eventForm.reset();
                fetchEvents();
            } else {
                alert('Failed to create event');
            }
        } catch (error) {
            console.error('Error creating event:', error);
        }
    });


    // Initial fetch of events
    fetchEvents();
});