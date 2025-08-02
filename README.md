# Health Dashboard

A Spring Boot application with Thymeleaf UI for monitoring application health and managing operations with comprehensive security features.

## Features

### Health Dashboard
- **Customizable Health Dashboard**: Groups contain multiple applications, and applications contain multiple health check endpoints
- **Hierarchical Navigation**: View groups → applications → health checks
- **Real-time Health Monitoring**: Automatic health checks every 5 seconds
- **Manual Health Checks**: Perform health checks on-demand with visual feedback
- **Color-Coded Health Status**: 
  - 🟢 **Light Green**: All health checks pass
  - 🟠 **Reddish Yellow**: Any health check fails
  - ⚫ **Gray**: No health checks configured
- **Health Status Indicators**: Visual status indicators (Healthy, Unhealthy, Unknown)
- **Auto-refresh**: Dashboard refreshes every 30 seconds

### Operations Page
- **Application Selection**: Dropdown to select applications
- **Configurable Operations**: Each application can have different sets of operations
- **REST API Execution**: Execute operations using Unirest for HTTP calls
- **Operation Results**: View operation execution results in modal dialogs
- **Security Toggle**: Operations require security code (3 letters + 12 numbers) to enable
- **Visual Feedback**: Disabled operations are grayed out until security code is entered

### Administration
- **Group Management**: Create, view, edit, and delete application groups
- **Application Management**: Add, edit, and delete applications to groups
- **Health Check Configuration**: Configure health check endpoints with full CRUD operations
- **Operation Configuration**: Set up operations with different HTTP methods and full CRUD operations
- **Security Toggle**: All administration functions require security code (3 letters + 12 numbers) to enable
- **Visual Protection**: All admin forms and buttons are disabled until security code is entered

### Security Features
- **Operations Security**: Toggle to enable/disable operation execution
- **Administration Security**: Toggle to enable/disable all admin functions
- **Code Validation**: Requires exactly 3 letters followed by 12 numbers (e.g., ABC123456789012)
- **Visual Feedback**: Disabled elements are grayed out and non-interactive
- **Consistent Security**: Same security mechanism across all protected areas

## Technology Stack

- **Spring Boot 3.2.0**: Main framework
- **Thymeleaf**: Server-side templating engine
- **H2 Database**: In-memory database for development
- **Spring Data JPA**: Data access layer
- **Unirest 3.14.5**: HTTP client for REST API calls
- **Bootstrap 5**: UI framework
- **Font Awesome**: Icons
- **jQuery**: Client-side JavaScript functionality

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
- `Group`: Application groups (e.g., Web Services, Internal Apps) with health status
- `App`: Applications within groups with health status
- `HealthCheck`: Health check endpoints for applications
- `Operation`: REST operations for applications

#### Services
- `GroupService`: Group management
- `AppService`: Application management
- `HealthCheckService`: Health check monitoring with Unirest and scheduled execution
- `OperationService`: Operation execution

#### Controllers
- `DashboardController`: Health dashboard views with health status calculation
- `OperationsController`: Operations page with security
- `AdminController`: Administration interface with full CRUD operations
- `HomeController`: Main entry point

### Database Schema

The application uses H2 in-memory database with the following tables:
- `groups`: Application groups
- `apps`: Applications
- `health_checks`: Health check endpoints
- `operations`: REST operations

## Usage

### Health Dashboard

1. **View Groups**: The main dashboard shows all application groups with color-coded health status
2. **Group Details**: Click on a group to see its applications with individual health status
3. **Application Health**: Click on an application to view its health checks
4. **Manual Health Checks**: Use "Check Now" buttons to perform health checks manually with visual feedback
5. **Health Status**: 
   - **Groups**: Green if all apps healthy, Reddish-yellow if any app unhealthy
   - **Applications**: Green if all health checks pass, Reddish-yellow if any fail

### Operations

1. **Security Code**: Enter the security code (3 letters + 12 numbers) to enable operations
2. **Select Application**: Choose an application from the dropdown
3. **View Operations**: See available operations for the selected application
4. **Execute Operations**: Click "Execute" to run operations (only when enabled)
5. **View Results**: Results are displayed in modal dialogs

### Administration

1. **Security Code**: Enter the security code (3 letters + 12 numbers) to enable admin functions
2. **Manage Groups**: Add, view, edit, and delete application groups
3. **Manage Applications**: Add, edit, and delete applications to groups
4. **Configure Health Checks**: Set up health check endpoints with full CRUD operations
5. **Configure Operations**: Define REST operations with HTTP methods and full CRUD operations

### Security Code Format

All protected functions require a security code with the following format:
- **Format**: Exactly 3 letters followed by exactly 12 numbers
- **Example**: `ABC123456789012`
- **Validation**: Real-time JavaScript validation
- **Scope**: Each page requires separate code entry

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

Health checks are automatically performed every 5 seconds. The system validates health check responses using the following criteria:

1. **HTTP Status Code**: Must be exactly 200 OK
2. **Response Content**: Must contain "healthy" status value in JSON response
3. **Validation Logic**: 
   - Checks for "status", "health", or "state" fields containing "healthy"
   - If no specific fields found, looks for "healthy" anywhere in response
   - Only marks as HEALTHY if both 200 OK and "healthy" status are present

You can modify the schedule in `HealthCheckService.java` by changing the `@Scheduled(fixedRate = 5000)` annotation.

### Security Configuration

Security toggles are implemented client-side with JavaScript:
- **Code Validation**: Regex pattern `/^[A-Za-z]{3}\d{12}$/`
- **Visual Feedback**: Disabled elements have 50% opacity
- **Button States**: "Enter Code" button changes color on success
- **Form Protection**: All inputs and buttons disabled until code entered

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

### Security Implementation

Security toggles are implemented using:
- **CSS Classes**: `.admin-disabled` and `.operations-disabled`
- **JavaScript**: jQuery for dynamic enable/disable functionality
- **Visual Feedback**: Color changes and opacity adjustments
- **Form Protection**: Disabled inputs and buttons

## Troubleshooting

### Common Issues

1. **Health checks failing**: Ensure target URLs are accessible
2. **Database issues**: Check H2 console for data integrity
3. **Template errors**: Verify Thymeleaf syntax in templates
4. **Security code not working**: Ensure format is exactly 3 letters + 12 numbers
5. **Operations not executing**: Check if security toggle is enabled

### Logs

Application logs are available at DEBUG level for troubleshooting:
- Spring Web: `logging.level.org.springframework.web=DEBUG`
- Application: `logging.level.com.ops=DEBUG`
- Health Check Service: `logging.level.com.ops.service.HealthCheckService=DEBUG`

Health check logs show:
- When health checks are performed
- Response times and HTTP status codes
- Validation results and status changes
- Error messages for failed health checks

### Security Troubleshooting

- **Code not accepted**: Verify format is exactly 3 letters + 12 numbers
- **Functions still disabled**: Check browser console for JavaScript errors
- **Visual issues**: Ensure jQuery is loaded properly

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License.
