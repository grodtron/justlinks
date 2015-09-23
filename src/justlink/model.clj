(ns justlink.model
  (:require [clojure.java.jdbc :as sql])
  (:require justlink.jdbc.json))

(def db {:subprotocol "postgresql"
         :subname "//localhost:5432/jlink"
         :user "postgres"
         :password "postgres"})

(defn get-user [email]
  (sql/query db ["SELECT * FROM users WHERE email=?" email]))

(defn get-friends [user-id]
  (sql/query db [(str
          "SELECT name, id FROM friends JOIN users ON id=a WHERE b=?"
          " UNION "
          "SELECT name, id FROM friends JOIN users ON id=b WHERE a=?")
                 user-id user-id]))

(defn are-friends? [user1 user2]
  (not (empty?
         (sql/query db
                    "SELECT * FROM friends WHERE a=? AND b=?"
                    ; user id a is always greater than user id b
                    (max user1 user2)
                    (min user1 user2)))))

(defn send-link! [to-user from-user link-data]
  (if (are-friends? to-user from-user)
    (sql/insert! db
                 :links
                 {:from_user from-user
                  :to_user   to-user
                  :data link-data})))


(defn get-unread-links [user-id] '())
