(ns justlink.views
  (:use hiccup.page))


(defn index []
  (html5
         [:head
          [:title "Welcome to JLinks!!"]]
    [:body
     [:h1 "Hello World!"]]))
