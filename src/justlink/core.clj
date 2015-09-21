(ns justlink.core
  (:use
    compojure.core
    justlink.views))

(defroutes app
  (GET "/" [] (index))
  (GET "/test" [] (str "testing")))
