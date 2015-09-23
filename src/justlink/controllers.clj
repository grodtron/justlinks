(ns justlink.controllers
  (:use justlink.model
        justlink.views
        ring.util.response)
  (:require [crypto.password.bcrypt :as password]))

(defn home-page [user]
  (let [friends (get-friends (user :id))]
    (index-view user friends)
    ))

(defn index [{session :session}]
  (if
    (contains? session :user)
    (home-page (session :user))
    (login-view)))

(defn process-login [{session :session, params :params}]
  (let [email (params "user")
        pass (params "pass")]
    (let [user (first (get-user email))]
      (if (password/check pass (user :passhash))
        (-> (redirect "/")
            (assoc :session (assoc session :user user))) ; valid
        (redirect "/invalid") ; not valid
        ))))
