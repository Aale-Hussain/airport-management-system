✈ AeroPorto — Flight Simulation & Airport Management System

AeroPorto is a full-stack Java EE web application that simulates flight booking and airport management using real-world geographic data.

Users can explore airports on an interactive map, save favorite locations, and simulate flight bookings between destinations.

The application follows the MVC architecture, separating presentation (JSP), business logic (Servlets), and persistence (MySQL).

---

## 🚀 Key Features

- 🔐 Secure user authentication (Email/Password + Google OAuth2)
- 🗺 Interactive airport map using OpenLayers + OpenStreetMap APIs
- ⭐ Save favorite airports and add personal notes
- ✈ Flight booking simulation with departure and arrival selection
- 📧 Automatic email confirmation after booking
- 🔒 Session-based access control for protected pages
- 🔁 Password recovery system with secure token validation

---

## 🧠 Architecture Overview

- MVC pattern (JSP → Servlets → MySQL)
- Layered backend design (Controller / Service / DAO style separation)
- Secure session management with authentication filters
- Integration with external APIs for geolocation and mapping

---

## 🛠 Tech Stack

- Java EE (Servlets, JSP)
- MySQL (Relational database)
- HTML5, CSS3, JavaScript
- OpenLayers (interactive maps)
- OpenStreetMap APIs (Nominatim, Overpass)
- Google OAuth2 authentication
- BCrypt password hashing

---

## 🔐 Security Highlights

- Passwords hashed using BCrypt
- OAuth2 authentication via Google Identity Services
- Session filtering for protected resources
- Secure token-based password reset system

---

## 📈 Future Improvements

- Real-time flight tracking simulation
- Role-based admin dashboard
- Booking modification system (edit instead of delete/recreate)
- Public read-only map access for non-authenticated users

---

## 👨‍💻 Project Highlights

This project demonstrates:

- Full-stack Java EE development
- Secure authentication systems (OAuth2 + session-based security)
- REST API integration (OpenStreetMap services)
- Relational database design and management
- Interactive geospatial UI development

---

## 📌 Purpose

Built as a portfolio project to demonstrate backend development skills, secure authentication flows, and integration of real-world mapping APIs in a Java EE architecture.
