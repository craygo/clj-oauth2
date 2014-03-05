(defproject craygo/clj-oauth2 "0.1.1"
  :description "oauth2 basic functions"
  :url "https://github.com/craygo/clj-oauth2"
  :scm {:name "git"
        :url "http://www.eclipse.org/legal/epl-v10.html"}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.9.0"]]
  :deploy-repositories [["clojars" {:creds :gpg}]])
