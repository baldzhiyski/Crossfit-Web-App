# Crossfit-Web-App

Welcome to the CrossFit Web App repository! This project is designed to provide a comprehensive platform for managing CrossFit training schedules, events, user profiles, and more. Whether you're a CrossFit enthusiast, coach, or gym owner, this application aims to enhance your experience with intuitive features and seamless functionality. With this custom app I am trying to enhance my developer skills.

## Functionality
- The coaches and the admins are added with static data , using json files . Each coach and admin has an active account with the corresponding role. Each coach can also participate in a training session with the others , having unlimited membership until leaving the work.


## Features
- **User Registration and Activation**: Users can register and activate their accounts via email.
- **Membership Management**: Users can purchase membership and view their membership in the profile page.
- **Edit Profile**: Each user can edit their profile details 
- **Training Enrollment**: Users can enroll in trainings, view their enrolled trainings, and cancel them if the cancellation is made more than 3 hours before the training starts. Each user has different number of trainings per week , depends on the membership !
- **Comment Section** : Users can display comments and their opinions for the different trainings .
- **Dynamic Scheduling**: The application automatically generates a new training schedule each week. Trainings in the past are automatically removed from the user's list.
- **Event Management**: After registration , violation or cancelled training from the coach , the users will receive an email
- **Email Management**: For this project I use mailHog.
- **Geolocation**: Users can choose their nationality during registration, with support for multiple countries through a REST API.
- **Validation**: Comprehensive validation during registration,creating event and other actions  to ensure data integrity via custom annotations .
- **Admin Features**: Admins can block and enable user accounts, with notifications sent via email.
- **Cloud Storage**: Integration with Cloudinary for storing and managing user photos.
- **Testing**: Includes both unit and integration tests with 65% line coverage.
- **Swagger Documentation**: API documentation available through Swagger for the Crossfit-Events - REST
- **Docker Hosting**: The entire application is hosted in Docker for ease of deployment and scalability.
- **Monitoring** : Used Prometheus with Grafana to track the interest of the users for the different types of trainings and of the blog 
- **Pages** : There are more than 20 Pages
- **Exceptions** : Global handler 
- **Security** : Spring security
- **Internationalization**: The main page, login, and registration pages are internationalized in two languages: German and English.

## Main focus 
- **BackEnd , Spring**

## Technologies Used

- **Backend**: Java, Spring Boot, Hibernate ORM
- **Frontend**: HTML, CSS, JavaScript, Bootstrap
- **Database**: MySQL
- **API Integration**: REST  for the crossfit-events , crossfit-trainings , countries and exchange rates
- **Tools**: Git, GitHub, IntelliJ IDEA, Maven (for dependency management), Docker, Cloudinary , MailHog, Grafana, Prometheus, Thymeleaf with Pagination, Redis , ReCaptcha 


## Installation
You can run the docker-compose file in the crossfit-client directory
To set up the project locally using Docker, follow these steps:

### Pull the Docker Images

Navigate to the crossfit-client modul -> docker -> docker-compose and run this file .
Each module is an existing image in my docker repository
```bash
docker pull baldzhiyski/crossfit-web-app-client:latest
docker pull baldzhiyski/crossfit-web-app-events:latest
docker pull baldzhiyski/crossfit-web-apptrainings:latest
