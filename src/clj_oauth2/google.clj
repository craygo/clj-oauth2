(ns clj-oauth2.google
  )

(def all-scopes {:spreadsheet "https://spreadsheets.google.com/feeds"
                 :docs "https://docs.google.com/feeds"})

(def config {:host "https://accounts.google.com"
             :endpoint-auth "/o/oauth2/auth"
             :endpoint-token "/o/oauth2/token"
             :client-id "613920579553-uhrr1pe3j4gmgn5komnk7t8jsffb90pn.apps.googleusercontent.com"
             :client-secret "0IHK6Koe-F-B1KtqXAYpBiPL"
             :all-scopes all-scopes
             :scopes [:spreadsheet]
             :redirect-uri "urn:ietf:wg:oauth:2.0:oob"
             :response-type "code"})

