services:
  # MySQL Database
  mysql:
    image: mysql:latest
    container_name: mysql-container
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: portfolio_db
    ports:
      - "3306:3306" # Exposes MySQL on port 3306 of the host
    volumes:
      - mysql-data:/var/lib/mysql # Persistent storage for MySQL data
    networks:
      - my-network

  # Spring Boot Backend
  backend:
    build:
      context: . # Ensure this matches the Dockerfile location for your Spring Boot app
      dockerfile: Dockerfile
    container_name: backend-app
    environment:
      SPRING_ACTIVE_PROFILE: prod
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/portfolio_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
      SPRING_JPA_HIBERNATE_DDL_AUTO: update
      SERVER_PORT: 8080
      APIKEY: ${APIKEY}
      #Important
      #api key need to be kept so make sure you create a env file or just add in here with tag 'APIKEY'
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    networks:
      - my-network

networks:
  my-network:
    driver: bridge

volumes:
  mysql-data:
