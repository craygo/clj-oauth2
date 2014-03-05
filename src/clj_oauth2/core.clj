(ns clj-oauth2.core
  (:require [clj-http.client :refer [post]]
            [clojure.string :refer [join]])
  (:import [java.net URLEncoder]))

(defn url-encode [s]
  (URLEncoder/encode s))

(defn- scope [all-scopes scopes]
  (join " " (map second (select-keys all-scopes scopes))))

(defn- get-token-request-params [config code]
  (let [{:keys [client-id client-secret response-type all-scopes scopes redirect-uri]} config]
    {:form-params {:code code
                   :client_id client-id
                   :client_secret client-secret
                   :redirect_uri redirect-uri
                   :grant_type "authorization_code"}
     :as :json
     :throw-entire-message? true}))

(defn- refresh-token-request-params [config refresh-token]
  (let [{:keys [client-id client-secret response-type all-scopes scopes redirect-uri]} config]
    {:form-params {:refresh_token refresh-token
                   :client_id client-id
                   :client_secret client-secret
                   :grant_type "refresh_token"}
     :as :json
     :throw-entire-message? true}))

(defn request-auth-url 
  "Generate the request authorization url from the config map"
  [{:keys [host endpoint-auth all-scopes scopes redirect-uri response-type client-id]}]
    (str host endpoint-auth
         "?" "scope=" (url-encode (scope all-scopes scopes))
         "&" "redirect_uri=" redirect-uri
         "&" "response_type=" response-type
         "&" "client_id=" client-id))

(defn get-tokens 
  "Get the tokens from the OAuth2 provider given the config and the access code"
  [config code]
  (let [{:keys [host endpoint-token]} config
        res (post (str host endpoint-token) (get-token-request-params config code))]
    (:body res)))

(defn refresh-tokens 
  "Refresh the access token"
  [config refresh-token]
  (let [{:keys [host endpoint-token]} config
        res (post (str host endpoint-token) (refresh-token-request-params config refresh-token))]
    (:body res)))

(defmacro  with-tokens 
  "Executes API call in the body with the tokens passed.
  Expects a tokens-atom as a map with keys :access_token and :refresh_token.
  Binds the access-token to the access-token-name you pass it.
  Will refresh the access_token on time-out and set the new value in the tokens-atom."
  [access-token-name tokens-atom config & body]
  `(try
    (let [~access-token-name (:access_token @~tokens-atom)]
      ~@body)
    (catch Exception e# ; TODO check for 401 invalid token in exception
      (let [new-tokens# (refresh-tokens ~config (:refresh_token @~tokens-atom))
            access_token# (:access_token new-tokens#)]
        (when access_token#
          (swap! ~tokens-atom assoc :access_token access_token#)
          (let [~access-token-name (:access_token @~tokens-atom)]
            ~@body))))))
