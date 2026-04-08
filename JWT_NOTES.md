# JWT Authentication Notes

## What is JWT?
JWT (JSON Web Token) is a secure way to transmit information between client and server as a JSON object.

---

## Why JWT is Used?
- Stateless authentication
- Secure communication
- Used in REST APIs
- No need to store session data on server

---

## Structure of JWT
JWT consists of 3 parts:
1. Header
2. Payload
3. Signature

Example:
Header.Payload.Signature

---

## JWT Flow
1. User sends login request
2. Server verifies credentials
3. Server generates JWT token
4. Token is sent to client
5. Client sends token in request headers
6. Server validates token before giving response

---

## Advantages
- Scalable (stateless)
- Secure (signed tokens)
- Efficient for APIs

---

## Disadvantages
- Cannot easily revoke token
- Token size is larger than session ID

---

## Implementation in Project
- JwtUtil → generates token
- SecurityConfig → secures endpoints
- AuthController → handles login requests

---

## Conclusion
JWT helps in building secure and scalable backend systems for authentication.