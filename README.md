
# FarEastCryptoBot

FarEastCryptoBot is a Spring Boot application designed to interact with users via Telegram, providing cryptocurrency-related functionalities. The bot is capable of executing commands such as starting a session and retrieving the current Bitcoin price.

## Features

- **Start Command**: Initializes a session for a new user and sends a welcome message.
- **Get Price Command**: Fetches the current Bitcoin price from Binance and sends it to the user.
- **Scheduling**: Utilizes Spring's scheduling capabilities for periodic tasks.

## Project Structure

- **CryptoBotApplication**: The main entry point of the application. It initializes the Spring Boot application and enables scheduling.
- **CryptoBot**: A Telegram bot implementation that registers and processes commands.
- **Commands**: 
  - `StartCommand`: Handles the `/start` command to welcome new users.
  - `GetPriceCommand`: Handles the command to fetch and display the current Bitcoin price.
- **Client**: 
  - `BinanceClient`: Connects to the Binance API to retrieve cryptocurrency prices.
- **Configuration**: Manages message text configurations used across commands.
- **Utilities**: Provides helper functions for executing answers and mapping data.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- A Telegram bot token and username
- Binance API endpoint for price retrieval

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/FarEastCryptoBot.git
   cd FarEastCryptoBot
   ```

2. **Configure Application Properties**:
   Create an `application.yml` file in `src/main/resources` with the following properties:
   ```properties
   telegram.bot.token=YOUR_TELEGRAM_BOT_TOKEN
   telegram.bot.username=YOUR_TELEGRAM_BOT_USERNAME
   binance.api.getPrice=YOUR_BINANCE_API_ENDPOINT
   ```

3. **Build the Project**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

## Usage

Once the application is running, you can interact with the bot on Telegram using the following commands:

- **/start**: Initializes your session and sends a welcome message.
- **/getprice**: Retrieves and displays the current Bitcoin price.

## Logging

The application uses SLF4J for logging. Logs are generated for key operations, such as API calls and command processing, to help with debugging and monitoring.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any improvements or bug fixes.



## Contact

For any questions or support, please contact [Ozsfag3154artem@gmail.com](mailto:Ozsfag3154artem@gmail.com).
```