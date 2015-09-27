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
                    ["SELECT * FROM friends WHERE a=? AND b=?"
                    ; user id a is always greater than user id b
                    (max user1 user2)
                    (min user1 user2)]))))

; Gets the id of an existing entry in the 'links' table for a given url
; returns nil if there is no such entry
(defn get-link-id [url]
  (let [result
        (first (sql/query db ["SELECT id FROM links WHERE url=?" url]))]
    (if (nil? result)
      nil
      (result :id))))

(defn get-or-create-link! [url]
  (let [id (get-link-id url)]
    (if (not (nil? id))
      id ; if it's not nil, just return it!
      ; in this case, the id is nil, so we have to create a new entry .... this is
      ; where it gets interesting :P


(defn send-link! [to-user from-user link-data]
  (if (are-friends? to-user from-user)
    (sql/insert! db
                 :links
                 {:from_user from-user
                  :to_user   to-user
                  :data link-data})))


(defn get-unread-links [user-id] '())
