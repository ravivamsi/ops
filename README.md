# Health Dashboard

A Spring Boot application with Thymeleaf UI for monitoring application health and managing operations.

## Features

### Health Dashboard
- **Customizable Health Dashboard**: Groups contain multiple applications, and applications contain multiple health check endpoints
- **Hierarchical Navigation**: View groups → applications → health checks
- **Real-time Health Monitoring**: Automatic health checks every 5 minutes
- **Manual Health Checks**: Perform health checks on-demand
- **Health Status Indicators**: Visual status indicators (Healthy, Unhealthy, Unknown)

### Operations Page
- **Application Selection**: Dropdown to select applications
- **Configurable Operations**: Each application can have different sets of operations
- **REST API Execution**: Execute operations using Unirest for HTTP calls
- **Operation Results**: View operation execution results in modal dialogs

### Administration
- **Group Management**: Create, view, and delete application groups
- **Application Management**: Add applications to groups
- **Health Check Configuration**: Configure health check endpoints
- **Operation Configuration**: Set up operations with different HTTP methods

## Technology Stack

- **Spring Boot 3.2.0**: Main framework
- **Thymeleaf**: Server-side templating engine
- **H2 Database**: In-memory database for development
- **Spring Data JPA**: Data access layer
- **Unirest**: HTTP client for REST API calls
- **Bootstrap 5**: UI framework
- **Font Awesome**: Icons

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ops
   ```

2. **Build the application**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Main Dashboard: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:healthdb`
     - Username: `sa`
     - Password: `password`

## Application Structure

### Core Components

#### Entities
- `Group`: Application groups (e.g., Web Services, Internal Apps)
- `App`: Applications within groups
- `HealthCheck`: Health check endpoints for applications
- `Operation`: REST operations for applications

#### Services
- `GroupService`: Group management
- `AppService`: Application management
- `HealthCheckService`: Health check monitoring with Unirest
- `OperationService`: Operation execution

#### Controllers
- `DashboardController`: Health dashboard views
- `OperationsController`: Operations page
- `AdminController`: Administration interface
- `HomeController`: Main entry point

### Database Schema

The application uses H2 in-memory database with the following tables:
- `groups`: Application groups
- `apps`: Applications
- `health_checks`: Health check endpoints
- `operations`: REST operations

## Usage

### Health Dashboard

1. **View Groups**: The main dashboard shows all application groups
2. **Group Details**: Click on a group to see its applications
3. **Application Health**: Click on an application to view its health checks
4. **Manual Health Checks**: Use "Check Now" buttons to perform health checks manually

### Operations

1. **Select Application**: Choose an application from the dropdown
2. **View Operations**: See available operations for the selected application
3. **Execute Operations**: Click "Execute" to run operations
4. **View Results**: Results are displayed in modal dialogs

### Administration

1. **Manage Groups**: Add, view, and delete application groups
2. **Manage Applications**: Add applications to groups
3. **Configure Health Checks**: Set up health check endpoints
4. **Configure Operations**: Define REST operations with HTTP methods

## Sample Data

The application comes with sample data including:
- 3 groups: Web Services, Internal Applications, Microservices
- 4 applications: User Service, Payment Service, Inventory App, Order Service
- Health checks for each application
- Sample operations for testing

## Configuration

### Application Properties

Key configuration options in `application.properties`:
- Server port: 8080
- H2 database configuration
- JPA settings
- Thymeleaf configuration
- Logging levels

### Health Check Configuration

Health checks are automatically performed every 5 minutes. You can modify the schedule in `HealthCheckService.java`.

## Development

### Adding New Features

1. **New Entity**: Create entity class and repository
2. **Service Layer**: Implement business logic in service classes
3. **Controller**: Add REST endpoints or view controllers
4. **Templates**: Create Thymeleaf templates for UI

### Customizing Health Checks

Health checks use Unirest to make HTTP requests. Modify the `performHealthCheck` method in `HealthCheckService` to customize health check logic.

### Adding Operations

Operations support all HTTP methods (GET, POST, PUT, DELETE, PATCH). Configure operations through the admin interface or add them programmatically.

## Troubleshooting

### Common Issues

1. **Health checks failing**: Ensure target URLs are accessible
2. **Database issues**: Check H2 console for data integrity
3. **Template errors**: Verify Thymeleaf syntax in templates

### Logs

Application logs are available at DEBUG level for troubleshooting:
- Spring Web: `logging.level.org.springframework.web=DEBUG`
- Application: `logging.level.com.ops=DEBUG`

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.
