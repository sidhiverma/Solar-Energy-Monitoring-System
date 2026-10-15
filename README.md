# Solar Energy Monitoring System

## 1. Project Overview

The Solar Energy Monitoring System is a Java-based web application that demonstrates how household solar energy production, energy consumption, battery levels, and system alerts can be monitored through a web dashboard.

The application provides separate views for administrators and homeowners. Administrators can manage users, registered devices, and battery alert settings, while homeowners can view energy information and device status.

## 2. Objectives

- Demonstrate a web-based solar energy monitoring system.
- Provide user and device management functionality.
- Display energy production and consumption information.
- Demonstrate battery alerts using a configurable threshold.
- Store application data in a database.
- Provide a responsive interface for the dashboard.

## 3. Technologies Used

- **Programming language:** Java 21
- **Framework:** Spring Boot
- **Web layer:** Spring MVC
- **Frontend templates:** Thymeleaf
- **Database access:** Spring Data JPA
- **Database:** H2 Database (file-based)
- **Frontend:** HTML and CSS
- **Charts:** Chart.js
- **Development environment:** IntelliJ IDEA

## 4. Main Features

### Admin Dashboard
- View the administration dashboard.
- View registered users and devices.
- Configure the battery alert threshold.

### User Management
- Register users with a name, email address, and role.
- Edit user details.
- Delete user records.

### Device Management
- Register a device with an ID, location, and status.
- Edit device details.
- Delete device records.

### Homeowner Dashboard
- View solar energy production and consumption.
- View battery percentage and system status.
- View the energy balance.
- View a production-versus-consumption chart.
- Display battery alerts based on the configured threshold.
- View registered devices and user records.

### Database Persistence
User records, device records, system settings, and energy readings are stored in the configured database.

## 5. Application Pages

With the application running locally, these pages are available:

- Main Dashboard: `http://localhost:8080/`
- Admin Dashboard: `http://localhost:8080/admin`
- User Management: `http://localhost:8080/admin/users`
- Device Management: `http://localhost:8080/admin/devices`
- Homeowner Dashboard: `http://localhost:8080/homeowner`

## 6. How to Run the Project

1. Install JDK 21.
2. Open the project in IntelliJ IDEA.
3. Allow Maven to download the project dependencies.
4. Run `SolarenergymonitoringApplication.java`.
5. Open `http://localhost:8080/homeowner` in your browser.

The application uses H2 Database, configured in
`src/main/resources/application.properties`.
No separate database server is required.

The application creates or updates its database tables
using the configured JPA settings.

## 7. Project Limitations

This project is a college prototype. The displayed energy readings are simulated demonstration data and are not collected from physical solar panels, smart meters, or battery sensors. The application does not currently provide live hardware monitoring.

## 8. Future Scope

- Integration with physical solar energy monitoring devices.
- Automatic collection of real-time energy readings.
- Historical reports and energy usage comparisons.
- Improved authentication and role-based access control.
- Additional notifications and energy-saving recommendations.

## 9. Team Members

- Parisha Dhawan — Team Leader
- Surbhi Tiwari — Team Member
- Sidhi Verma — Team Member

## 10. Conclusion

The project demonstrates a Java-based web application combining database connectivity, user and device management, energy visualization, and configurable battery alerts. It provides a foundation for further development into a hardware-connected solar monitoring solution.