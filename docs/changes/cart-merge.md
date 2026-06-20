# Cart Merge

Implemented guest-to-user cart merge in `mergeCart`.

- Source cart items merged into the authenticated customer's cart.
- Quantities summed for matching product variants.
- Merged quantity capped at `MAX_ITEM_QUANTITY`; overflow surfaces a warning.
- Source cart deleted after a successful merge.
- Resolves the previously stubbed `UnsupportedOperationException`.
