(ns justlink.core
  (:use
    compojure.core
    justlink.views
    justlink.controllers
    hiccup.bootstrap.middleware
    ring.middleware.params))

(defroutes handler
  (GET "/" [] (login))
  (POST "/login" req ((wrap-params process-login) req))
  (GET "/test" [] (str "testing")))

(def app
  (wrap-bootstrap-resources handler))
