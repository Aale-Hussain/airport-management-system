 AeroPorto - Flight Simulation and Airport Management System

AeroPorto is a Java EE-based web application that allows users to explore airports globally on an interactive map, manage a personal list of favorite locations with custom feedback, and simulate flight bookings. The system is architected using the MVC (Model-View-Controller) pattern, ensuring a strict separation between web traffic management (Servlets) and core database operations.



1. Database Architecture

The application runs on a MySQL database consisting of five core tables designed to enforce data integrity and security constraints:

• users: Manages user profiles, credentials, and authentication states. It supports standard registration fields alongside a google_id column to handle OAuth2 Social Login profiles.
• poi (Points of Interest): Caches airport details (OpenStreetMap ID, name, precise lat/lon coordinates, and classification) to optimize map performance and minimize redundant external API latency.
• user_poi: A many-to-many junction table linking users to their saved airports. It tracks the "Like" status (is_liked) and holds custom user-submitted comments.
• prenotazione (Booking): Records simulated flight itineraries by mapping a user to separate departure and arrival airport entities on a chosen calendar date. A composite unique constraint prevents duplicate itinerary entries on identical dates.
• token: Handles temporary security credentials for password self-service. Tokens are time-sensitive and are flushed automatically every 5 minutes by a dedicated MySQL event scheduler (token_event).

Entity-Relationship (E-R) Logic
1. USER (1,N) → MAKES → (1,1) BOOKING: A user can manage multiple flight bookings; each booking record is strictly tied to one individual user account.
2. BOOKING (1,1) → DEPARTS FROM / ARRIVES AT → (1,N) POI: Every flight record requires two valid airport points. A specific airport may be referenced across multiple bookings.
3. USER (1,N) → SAVES → (1,1) USER_POI: Users can bookmark multiple airports. Each bookmark entry belongs exclusively to its creator.
4. USER_POI (1,1) → RELATES TO → (1,N) POI: Every comment or preference entry maps back to a single airport entity.



2. System Features

Authentication Architecture
The system supports two secure authentication paths, linking accounts natively via the user's unique email address:
• Native Form Authentication: Users can register (signin.jsp) and log in (login.jsp) using standard credentials. Passwords are safe because they are processed through the BCrypt framework (PasswordHash.java) before being written to the database. In this workflow, the google_id field defaults to NULL.
• Google OAuth2 Sign-In: Users can bypass passwords by authenticating directly with Google. If logging in for the first time via Google, the system checks the email address: if no account exists, it automatically provisions a new row and maps the unique Google identifier to the google_id column. For subsequent logins, the system validates the incoming Google token and signs the user in instantly.
• Session Filtering Security: A robust SessionFilter intercepts incoming requests for assets residing within the protected domain (/protected_jsp/). Unauthenticated traffic is automatically intercepted and routed back to the login portal.
• Self-Service Password Reset: Employs an automated, short-lived secure UUID validation workflow. The system tokenizes recovery links to safely transition users through the verification and password change screens.

Interactive Mapping & Spatial Queries (map.jsp)
• Dynamic Mapping Canvas: Integrates the OpenLayers JavaScript API to render responsive geographical map layers directly in the browser viewport.
• Spatial Search Integration: Geocoding and point-of-interest queries interact seamlessly with external OpenStreetMap endpoints (Nominatim and Overpass API) to locate urban environments and map nearby aviation hubs dynamically.
• Contextual Action Modals: Clicking on specific map markers activates custom panels allowing users to:
    • Save locations to their account and append text reviews via the PoiServlet.
    • Assign selected airports directly into the active departure or arrival slots of the booking form.

Flight Lifecycle Management
• Simulation Engine: Processes active itineraries through the PrenotazioneServlet upon field completion. The application smoothly intercepts duplicate data requests to maintain standard database integrity without user-facing interruptions.
• Automated Notification: Once an itinerary is written to the database, a background process formats and transmits a transaction confirmation email detailing the flight schedule to the user.
• Account Controls: A contextual dropdown menu tied to the active session gives users access to:
    • Profile Management (profile.jsp): Updates core identity fields like username and names.
    • Saved Locations (perferiti.jsp): Provides tabular views of bookmarked hubs with inline purging controls driven by the CancellaPerferitiServlet.
    • Flight Ledger (prenotazione.jsp): Tracks booking logs and handles record deletions via the CancellaPrenotazioneServlet.



3. Technology Stack

• Core Platform: Java EE (Servlets, JSP, Security Filters)
• Persistence Layer: MySQL (Relational Constraints, Foreign Keys, Scheduled Event Triggers)
• Security & Identity: BCrypt Password Hashing, Google OAuth2 API
• Presentation Layer: HTML5, CSS3, JavaScript, OpenLayers Client API
• External Services: OpenStreetMap Spatial Data Infrastructure (Nominatim & Overpass engines)



4. Future Enhancements

While the core MVC architecture, database framework, and security parameters are fully operational, the system lifecycle path includes the following scheduled updates:
1. Inline Flight Alterations: Implement direct date modifications within the active booking logs ledger to eliminate the current "delete-and-rebook" process.
2. Hardened Reset Workflow: Improve the password recovery form privacy mechanics by substituting explicit email parameters with hidden HTML elements (<input type="hidden">), keeping user verification constrained to the unique token string.
3. Public Map Visibility: Refactor access tiers to allow public, unauthenticated guests to use the map and city search filters freely, enforcing authentication checkpoints only when state-changing actions (booking flights, saving favorites) are attempted.
