# SmartFinance REST API

SmartFinance is a feature-rich REST API designed to help users manage their finances efficiently. 
It combines expense tracking, investment management in currencies and cryptocurrencies, and live rate checking for both. 
The application utilizes external APIs to provide accurate and up-to-date data:

- **CoinGecko API**: For cryptocurrency rates.  
- **CurrencyAPI.com**: For currency exchange rates.  

The keys for these APIs are configured in the `application.properties` file.

---

## Features

- **Expense Tracking**: Record and manage income and expenses to monitor your financial status.  
- **Investments**: Seamlessly invest in currencies and cryptocurrencies.  
- **Live Rates**: Check the latest exchange rates for currencies and cryptocurrencies.  
- **Frontend Interface**: A user-friendly Vaadin-based frontend for interaction.

---

## Technologies Used

### Backend
- Spring Boot  
- Spring Security  
- Hibernate  
- PostgreSQL  

### Frontend
- Vaadin  

### API Documentation
- Swagger

---

## Prerequisites

To run the application locally, ensure the following are installed on your system:
- Java Development Kit (JDK) 21 or later  
- Gradle 8.11 or later  
- PostgreSQL  

---

## Setup and Running the Application

1. **Clone the Repository**  
   ```
   git clone https://github.com/opach16/SmartFinance.git
   ```
   
2. **Configure the Database**  
Update the `application.properties` file with your PostgreSQL credentials:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/SmartFinanceDB
spring.datasource.username=<your-database-username>
spring.datasource.password=<your-database-password>
```

3. **Add API Keys**  
Include your API keys for CoinGecko and CurrencyAPI.com in the `application.properties` file:
```
coingecko.api.key=<your-coingecko-api-key>
currencyapi.api.key=<your-currencyapi-key>
```

4. **Build and Run the Backend**  
Use Gradle to build and start the backend application:
```
./gradlew build
./gradlew bootRun
```

5. **Clone the Frontend Repository**
   ```
   git clone https://github.com/opach16/SmartFinance_view.git
   ```
   
6. **Run the Frontend**  
Navigate to the Vaadin frontend directory and start the frontend application:
```
./gradlew build
./gradlew bootRun
```

7. **Access the App**  
- **Frontend**: http://localhost:8081/
- **API Documentation**: http://localhost:8080/swagger-ui/index.html


## Contact

For any questions or support, contact:
- **Author**: Konrad
- **Email**: [opach16@outlook.com]

# Screenshots
<img width="426" height="280" alt="Screenshot 2025-01-30 at 11 38 12" src="https://github.com/user-attachments/assets/e960739d-d94e-4006-bb9c-2da96f069ce2" />
<img width="426" height="280" alt="Screenshot 2025-01-30 at 11 36 56" src="https://github.com/user-attachments/assets/33734752-a4d8-4317-9c8f-09d9e70aa50d" />
<img width="426" height="280" alt="Screenshot 2025-01-30 at 11 36 46" src="https://github.com/user-attachments/assets/f482ce2f-2822-4b9c-a429-38741139f225" />
<img width="426" height="280" alt="Screenshot 2025-01-30 at 11 37 14" src="https://github.com/user-attachments/assets/12f770c9-71fd-4fda-8778-25e6c300f0d8" />

