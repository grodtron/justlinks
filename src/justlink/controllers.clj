(ns justlink.controllers
  (:use justlink.model)
  (:require [crypto.password.bcrypt :as password]))

(defn process-login [{params :params}]
  (let [email (params "user")
        pass (params "pass")]
    (let [user (first (get-user email))]
      (str (password/check
        pass
        (user :passhash))))))
