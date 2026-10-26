# Ashok Jewellers — MVP specification

## Brand
- Name: Ashok Jewellers
- Existing lotus/gold logo on burgundy
- Balanced luxury: burgundy, cream, gold
- Public website in English and Kannada; remembers language selection
- Owner app English only in v1

## Website
- Public design catalogue with categories, product ID, weight, purity, photos and availability
- Search and category filter
- Manual published-rate display for 22K, 24K and silver
- Shop location, opening hours, phone and WhatsApp enquiry
- Unavailable designs remain visible
- No online checkout or automatic stock deduction

## Android owner app
- Native Android / Kotlin / Compose
- Local-first design catalogue
- Multiple photo URLs/paths per design in the full design; MVP supports a photo URL field
- Manual daily rates and local rate history
- Multi-line manual bills, customer details and receipt/GST-invoice selection
- Multiple payments and outstanding balance
- Bill search, sales/collection summary
- PDF generation and Android sharing
- PIN screen scaffold; production authentication/biometric integration still needed
- Cloud backup/sync is not connected in this starter

## Financial rules
- Never recalculate a saved bill when rates change
- Payment entries are separate from bills
- Balance = current bill total - net recorded payments
- Bill edits create change-history entries
- Cancelled bills should remain in history
- GST invoice template must be validated before real use

## Not in MVP
- Cloud synchronisation between website and app
- Secure cloud backup/restore
- Automatic market-rate fetching
- Jewellery price calculation
- Stock quantity management
- Multi-user staff access
- Direct thermal printer integration
