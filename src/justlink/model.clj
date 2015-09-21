(ns justlink.model
  (:require [clojure.java.jdbc :as sql]))

(def db {:subprotocol "postgresql"
         :subname "//localhost:5432/jlink"
         :user "postgres"
         :password "postgres"})

(defn get-user [email]
  (sql/query db ["SELECT * FROM users WHERE email=?" email]))
