# AeroPorto - Flight Simulation and Airport Management System

AeroPorto is a Java EE web application that allows users to search for airports on an interactive map, save their favorite airports with personal comments, and simulate flight bookings. The project is structured using the MVC (Model-View-Controller) architecture, creating a clear separation between the Servlets (web traffic control) and the logic classes (database interaction).


1. Database Structure (Relational Model)

The system uses a MySQL database composed of 5 main tables to manage data and security:

• users: Stores user registration credentials (username, unique email, BCrypt-hashed password, first name, last name) and includes a google_id field set to NULL for future implementation (Social Login).
• poi (Points of Interest): Stores airport data (OpenStreetMap ID, name, lat/lon geographic coordinates, and type) to speed up map loading without making constant, slow requests to external APIs.
• user_poi: A many-to-many junction table that links users to their favorite airports. It handles the "Like" status (is_liked) and personal text comments.
• prenotazione (Booking): Logs simulated flights by connecting the user to two different airports (departure and arrival) on a specific calendar date. A unique constraint blocks duplicate trips on the same day.
• token: Stores temporary codes for password recovery. These tokens expire and are automatically deleted every 5 minutes by a database event scheduler (token_event).

E-R (Entity-Relationship) Model Relations
1. USER (1,N) → MAKES → (1,1) BOOKING: A user can make multiple bookings, but each booking belongs to only one user.
2. BOOKING (1,1) → DEPARTS FROM / ARRIVES AT → (1,N) POI: Each booking requires exactly two airports (one departure, one arrival). An airport can appear in many different bookings.
3. USER (1,N) → SAVES → (1,1) USER_POI: A user can add multiple airports to their favorites. Each favorite record belongs to only one user.
4. USER_POI (1,1) → RELATES TO → (1,N) POI: Each comment or "Like" is associated with one specific airport.



2. Application Features

Authentication and Security
• Login and Registration: On their first visit, users are sent to login.jsp. New users can create an account via signin.jsp, which then redirects them to the login page.
• SessionFilter: All private pages inside the /protected_jsp/ folder are protected by a session filter. If an unauthenticated user tries to bypass this by typing the URL directly into the browser, they are instantly kicked back to the login page.
• Password Encryption: Passwords are secure because the BCrypt library (PasswordHash.java) automatically hashes them before they ever enter the database.
• Password Recovery: If a user forgets their password, the system generates a unique UUID token valid for 5 minutes. This token is passed directly into the URL parameters to automatically pre-fill the fields on resetPassword.jsp.

Interactive Map and Search (map.jsp)
• Map Integration: Uses the OpenLayers library to display a dynamic, interactive map.
• City Search: When a user types a city name, the system queries the OpenStreetMap APIs (Overpass and Nominatim) to get the coordinates and local airports, dropping custom pins (markers) on the map.
• Control Panel: Clicking on a pin opens a panel with multiple options:
    • Users can type a custom comment or click a "Like" button, saving the data through the PoiServlet.
    • Users can click "Departure" or "Arrival" buttons, which automatically type that airport's name into the sidebar booking form.

Booking Management and Emails
• After filling out the departure, arrival, and date fields, the user can click "Prenota" (Book) to send the data to the PrenotazioneServlet.
• On success, the booking form resets, a confirmation alert pops up, and the system sends a background summary email to the user with full flight details. If a user tries to book a duplicate trip, the backend catches the SQL error code 1062 and handles it safely without crashing.

Profile Menu
Clicking on the icon displaying the user's first initial opens a dropdown menu with three dedicated areas:
1. Profile (profile.jsp): Allows updating the username, first name, and last name.
2. Favorites (perferiti.jsp): Displays all saved airports and comments, with an option to remove them via the CancellaPerferitiServlet.
3. Bookings (prenotazione.jsp): Shows a clean history of all booked flights with a cancellation option via the CancellaPrenotazioneServlet.



3. Technologies Used

• Backend: Java EE (Servlets, JSP, Filters)
• Database: MySQL (with Relational Constraints and Event Scheduler active for tokens)
• Security: BCrypt (Password hashing)
• Frontend: HTML5, CSS, OpenLayers API (JavaScript for map rendering)
• External APIs: OpenStreetMap (Nominatim for geocodifica, Overpass for airport data)



4. Conclusions and Future Developments

Due to limited time, it was not possible to complete every single feature originally planned, but the application still provides a solid, organized, and secure foundation. 

For future versions of the software, development will focus on four specific technical improvements:
1. Direct Flight Editing: Add a function to modify a flight date directly from the booking table without having to delete and re-book the entire trip.
2. Password Reset Privacy: Hide the email input field on the reset page using an <input type="hidden"> tag for better privacy, requiring users to type or paste only the token code manually.
3. Social Login: Enable quick login with Google by utilizing the google_id column, which is currently set to NULL by default for all users.
4. Open-Access Map: Allow visitors to explore the map freely in read-only mode right when they open the site, requiring a login only when they try to save a favorite or make a booking.
