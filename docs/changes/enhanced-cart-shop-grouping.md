# Enhanced Cart Shop Grouping

Fixed shop grouping in `GET /api/v1/carts/enhanced`.

- Items are now grouped by the real shop id from the product service.
- Removed random `UUID` shop ids and synthetic `"Shop <uuid>"` names.
- Real shop names are passed through when provided.
- Added a regression test for same-shop grouping.
