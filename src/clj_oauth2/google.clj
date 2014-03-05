(ns clj-oauth2.google
  )

(def all-scopes {:spreadsheet "https://spreadsheets.google.com/feeds"
                 :docs "https://docs.google.com/feeds"})

(def config {:host "https://accounts.google.com"
             :endpoint-auth "/o/oauth2/auth"
             :endpoint-token "/o/oauth2/token"
             :client-id "client-id"
             :client-secret "client-secret"
             :all-scopes all-scopes
             :scopes [:spreadsheet]
             :redirect-uri "urn:ietf:wg:oauth:2.0:oob"
             :response-type "code"})

