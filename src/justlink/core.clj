(ns justlink.core
  (:use
    compojure.core
    compojure.route
    justlink.views
    justlink.controllers
    ring.middleware.params))

(defroutes app
  (GET "/" [] (login))
  (POST "/login" req ((wrap-params process-login) req))
  (GET "/test" [] (str "testing"))
  (resources "/static"))

