FROM golang:1.23 AS builder

WORKDIR /app

COPY go.mod go.sum ./

RUN go mod download

COPY ./ ./

ENV HOST=0.0.0.0

RUN CGO_ENABLED=0 GOOS=linux go build -o auth-service ./

FROM alpine:latest

WORKDIR /root/

COPY --from=builder /app/auth-service ./

EXPOSE 3000

CMD ["./auth-service"]