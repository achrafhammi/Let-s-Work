package services

import (
	"context"
	"errors"
	"github.com/Nerzal/gocloak/v13"
	"os"
)

type IdentityManager struct {
	url          string
	realm        string
	clientId     string
	clientSecret string
}

func NewIdentityManager() *IdentityManager {
	return &IdentityManager{
		url:          os.Getenv("KEYCLOAK_URL"),
		realm:        os.Getenv("KEYCLOAK_REALM"),
		clientId:     os.Getenv("KEYCLOAK_CLIENT_ID"),
		clientSecret: os.Getenv("KEYCLOAK_CLIENT_SECRET"),
	}
}

func (im *IdentityManager) LoginKeycloak(ctx context.Context, username, password string) (string, error) {
	client := gocloak.NewClient(im.url)
	token, err := client.Login(ctx, im.clientId, im.clientSecret, im.realm, username, password)
	if err != nil {
		return "", errors.New(err.Error())
	}

	return token.AccessToken, nil
}

func (im *IdentityManager) RegisterKeycloak(ctx context.Context, user gocloak.User, password string) error {
	client := gocloak.NewClient(im.url)
	clientToken, _ := client.LoginClient(ctx, im.clientId, im.clientSecret, im.realm)
	userID, err := client.CreateUser(ctx, clientToken.AccessToken, im.realm, user)
	if err != nil {
		return err
	}

	// Set the user's password
	err = client.SetPassword(ctx, clientToken.AccessToken, userID, im.realm, password, false)

	if err != nil {
		return err
	}

	return nil
}

func (im *IdentityManager) LogoutKeycloak(ctx context.Context, token string) error {
	client := gocloak.NewClient(im.url)
	return client.Logout(ctx, im.clientId, im.clientSecret, im.realm, token)
}
