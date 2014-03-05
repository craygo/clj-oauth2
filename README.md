# clj-oauth2

A Clojure library designed to provide a minimal setup to do 
an OAuth2 authentication and get a token.

Does not assume you are using it from a webapp.
Produces an authorization url.
The user can hit the url, authenticate and get back an access code that 
this lib will exchange for an access token and refresh token.

When using it from a webapp you can redirect the user to the authorization url and 
provide a redirect url back to your webapp.

Uses clj-http (could probably do with just clj-http-lite)

## Usage
Provides 3 functions and 1 macro.
All take a config for the OAuth2 provider, Google is provided as an example.

request-auth-url - creates the authorization url
get-tokens       - gets access- and refresh tokens
refresh-tokens   - use the refresh_token to get a fresh access_token

The macro with-tokens provides convenience to use the tokens to access oauth protected resources.
It will execute the body and if the access_token has expired it will auto-refresh it.

```clj
(require '[clj-oauth2.core :refer [request-auth-url get-tokens with-tokens]])
(require '[clj-oauth2.google])
(require '[clj-google-spreadsheet.core :refer [get-all-spreadsheets]])

(def google-config (merge clj-oauth2.google/config
                          {:client-id "your-client-id-here"
                           :client-secret "your-client-secret-here"}))

(def tokens (atom {}))

(request-auth-url google-config)
; copy url into browser, authenticate, authorize app and copy the resulting access code

(reset! tokens (get-tokens google-config "your-access-code-here"))

(with-tokens at tokens google-config
  (->
    (get-all-spreadsheets at)
    :feed :entry first :id :$t))
```

## License

Copyright © 2014 Harry Binnendijk

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
