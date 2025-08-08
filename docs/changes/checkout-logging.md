# Checkout Logging Cleanup

Replaced raw console printing in `checkout` with SLF4J logging.

- Promotion summaries now use `log.info`.
- Promotion failures now use `log.warn`.
- No behavioural change to the promotion fallback path.
