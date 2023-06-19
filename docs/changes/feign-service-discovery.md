# Feign Service Discovery (not merged)

Proposed removing hardcoded Feign URLs in favour of Eureka resolution.

- Would drop `url = http://localhost:8086` / `:8095` from the clients.
- Blocked: Eureka is disabled for local development.
- Other services still depend on the fixed ports.
- Revisit once discovery is enabled across environments.
