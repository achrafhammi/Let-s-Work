package main

import (
	"auth-service/handlers"
	"auth-service/middleware"
	"fmt"
	"github.com/gofiber/fiber/v2"
	"log"
	"os"
)

func main() {

	fmt.Print("i'm working")
	//err := godotenv.Load()
	//if err != nil {
	//	log.Fatal("Error loading .env file")
	//}

	app := fiber.New()

	// Routes
	app.Post("/login", handlers.Login)
	app.Post("/signup", handlers.Signup)
	app.Post("/logout", handlers.Logout)

	app.Get("/hello-world", handlers.Hello_World)

	app.Get("/protected", JWTMiddleware(), handlers.Protected)

	log.Fatal(app.Listen(os.Getenv("SERVER_URL")))
}

// JWTMiddleware checks if the request is authenticated and authorized
func JWTMiddleware() fiber.Handler {
	return func(c *fiber.Ctx) error {
		// Assuming you're using Keycloak's JWT
		token := c.Get("Authorization")
		if token == "" {
			return c.Status(fiber.StatusUnauthorized).SendString("Missing or invalid JWT")
		}

		_, err := middleware.ValidateJWT(c.Context(), token)
		if err != nil {
			return c.Status(fiber.StatusUnauthorized).SendString("Invalid token")
		}

		return c.Next()
	}
}
