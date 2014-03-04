(ns clj-oauth2.core
  (:require [clj-http.client :as client :refer [post]]
            [cheshire.core :refer [parse-string]]
            [clojure.string :refer [join]]
            [clj-oauth2.google]
            [clj-oauth2.spreadsheet :refer :all]
            )
  (:import [java.net URLEncoder]))

(defn url-encode [s]
  (URLEncoder/encode s))

(defn scope [all-scopes scopes]
  (join " " (map second (select-keys all-scopes scopes))))

(defn request-auth-url [config]
  (let [{:keys [host endpoint-auth all-scopes scopes redirect-uri response-type client-id client-secret]} config]
    (str host endpoint-auth
         "?"
         "scope=" (url-encode (scope all-scopes scopes))
         "&"
         "redirect_uri=" (identity redirect-uri)
         "&"
         "response_type=" response-type
         "&"
         "client_id=" client-id
         )))

(defn get-token-request-params [config code]
  (let [{:keys [client-id client-secret response-type all-scopes scopes redirect-uri]} config]
    {:form-params {
                   :code code
                   :client_id client-id
                   :client_secret client-secret
                   ;:response_type response-type
                   ;:scope (url-encode (scope all-scopes scopes))
                   :redirect_uri redirect-uri
                   :grant_type "authorization_code"}
     :as :json
     :throw-entire-message? true}))

(defn get-tokens [config code]
  (let [{:keys [host endpoint-token]} config
        res (post (str host endpoint-token) (get-token-request-params config code))]
    (:body res)
    ))

(request-auth-url clj-oauth2.google/config)

(def access-code "4/7pEcljZxZtCFMiBNbsAq6_25Gog-.wpquKpaIssIRgrKXntQAax0cploWiQI")

;(get-tokens clj-oauth2.google/config access-code)

(def tokens {:access_token "ya29.1.AADtN_VtNYtEkix7fimg6puesYp2SOiVX5oonzraCABCTHizwsvLwKg2MKey", :token_type "Bearer", :expires_in 3600, :refresh_token "1/5D8sHH0p6Y6uHpcXvD5dwiZtlSLJOLKw7LgTwIgtNXE"})

#_(let [at (:access_token tokens)]
  (->
    (get-all-spreadsheets at)
    (get-spreadsheet-link-by-name "Gumscraped")
    (get-spreadsheet at)
    get-listfeed-url
    (get-listfeed at)
    count
    ))
