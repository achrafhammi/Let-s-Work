package handlers

import (
	"auth-service/services"
	"github.com/Nerzal/gocloak/v13"
	"github.com/gofiber/fiber/v2"
)

// Login handles user login
func Login(c *fiber.Ctx) error {
	im := services.NewIdentityManager()
	type LoginRequest struct {
		Username string `json:"username"`
		Password string `json:"password"`
	}

	var request LoginRequest
	if err := c.BodyParser(&request); err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
			"error": "cannot parse JSON",
		})
	}

	token, err := im.LoginKeycloak(c.Context(), request.Username, request.Password)
	if err != nil {
		return c.Status(fiber.StatusUnauthorized).JSON(fiber.Map{
			"error":   "invalid credentials",
			"message": err.Error(),
		})
	}

	return c.JSON(fiber.Map{
		"token": token,
	})
}

// Signup handles user registration
func Signup(c *fiber.Ctx) error {
	im := services.NewIdentityManager()
	type SignupRequest struct {
		Username string `json:"username"`
		Password string `json:"password"`
		Email    string `json:"email"`
	}

	var request SignupRequest
	if err := c.BodyParser(&request); err != nil {
		return c.Status(fiber.StatusBadRequest).JSON(fiber.Map{
			"error": "cannot parse JSON",
		})
	}

	user := gocloak.User{
		Username: &request.Username,
		Email:    &request.Email,
		Enabled:  gocloak.BoolP(true),
	}

	err := im.RegisterKeycloak(c.Context(), user, request.Password)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).JSON(fiber.Map{
			"error":   "unable to create user",
			"message": err.Error(),
		})
	}

	return c.SendStatus(fiber.StatusCreated)
}

// Logout handles user logout
func Logout(c *fiber.Ctx) error {
	im := services.NewIdentityManager()
	token := c.Get("Authorization")
	if token == "" {
		return c.Status(fiber.StatusBadRequest).SendString("Missing token")
	}

	// Call the LogoutKeycloak function
	err := im.LogoutKeycloak(c.Context(), token)
	if err != nil {
		return c.Status(fiber.StatusInternalServerError).SendString("Failed to logout")
	}

	return c.SendString("Logged out successfully")
}

// Protected handles a protected route example
func Protected(c *fiber.Ctx) error {
	return c.SendString("Access granted")
}
