# Batch Product Lookup

Reduce duplicate product-service calls during cart operations.

- One variant-detail fetch per request, reused for the summary refresh.
- Removes a redundant Feign round-trip after add/update/remove.
- Empty-cart short-circuit retained.
- Status: open, pending cache-vs-batch discussion.
