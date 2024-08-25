package middleware

import (
	"context"
	"github.com/Nerzal/gocloak/v13"
	"os"
)

var (
	client = gocloak.NewClient(os.Getenv("KEYCLOAK_URL"))
	realm  = os.Getenv("KEYCLOAK_REALM")
)

func ValidateJWT(ctx context.Context, token string) (*gocloak.IntroSpectTokenResult, error) {
	rptResult, err := client.RetrospectToken(ctx, token, os.Getenv("KEYCLOAK_CLIENT_ID"), os.Getenv("KEYCLOAK_CLIENT_SECRET"), realm)
	if err != nil || !*rptResult.Active {
		return nil, err
	}
	return rptResult, nil
}
