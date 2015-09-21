(ns justlink.core
  (:use
    compojure.core
    justlink.views
    hiccup.bootstrap.middleware))

(defroutes handler
  (GET "/" [] (login))
  (POST "/login" [] "This is you trying to log in")
  (GET "/test" [] (str "testing")))

(def app
  (wrap-bootstrap-resources handler))
