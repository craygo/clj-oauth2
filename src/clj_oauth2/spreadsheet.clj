(ns clj-oauth2.spreadsheet
  (:require [clj-http.client :as client]))

(defn get-all-spreadsheets [access-token]
  (->
    (client/get 
      (str "https://spreadsheets.google.com/feeds/spreadsheets/private/full"
           "?"
           "access_token=" access-token
           "&"
           "alt=json")
      {:as :json})
    :body
    ))

(defn get-spreadsheet-link-by-name [all-spreadsheets name]
  (->>
    all-spreadsheets
    :feed
    :entry
    (filter #(= (-> % :title :$t) name))
    first
    :link
    (filter #(= (-> % :rel) "http://schemas.google.com/spreadsheets/2006#worksheetsfeed"))
    first
    :href
    ))

;(get-spreadsheet-link-by-name "Gumscraped")

(def sheet-url "https://spreadsheets.google.com/feeds/worksheets/tmHaGP5N7gm9zlgqiWspuHg/private/full")

(defn get-spreadsheet [sheet-url access-token]
  (->
    (client/get 
      (str sheet-url
           "?"
           "access_token=" access-token
           "&"
           "alt=json")
      {:as :json})
    :body))

;(get-spreadsheet sheet-url)

(defn get-listfeed-url [spreadsheet]
  (->>
    spreadsheet
    :feed
    :entry
    first
    :link
    (filter #(= (-> % :rel) "http://schemas.google.com/spreadsheets/2006#listfeed"))
    first
    :href
    ))

(def worksheet-listfeed "https://spreadsheets.google.com/feeds/list/tmHaGP5N7gm9zlgqiWspuHg/od6/private/full")

(defn get-listfeed [worksheet-listfeed access-token]
  (->
    (client/get 
      (str worksheet-listfeed
           "?"
           "access_token=" access-token
           "&"
           "alt=json")
      {:as :json})
    :body
    :feed
    :entry))
