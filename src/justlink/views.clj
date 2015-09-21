(ns justlink.views
  (:use hiccup.page
        hiccup.bootstrap.page))

(defn index []
  (html5
    [:head
     [:title "Welcome to JLinks!!"]
     (include-bootstrap)]
    [:body
     [:h1 "Hello World!"]]))

(defn login []
  (html5
    [:head
     [:title "Please Log In!"]
     (include-bootstrap)]
    [:body
     [:div {:class "container"}
      [:form {:action "/login"
              :method "POST"
              :class "form-signin"}
       [:div {:class "form-group"}
        [:label {:for "user"} "Username"]
        [:input {:name "user"
                 :type "text"
                 :class "form-control"}]]
       [:div {:class "form-group"}
        [:label {:for "pass"} "Password"]
        [:input {:name "pass"
                 :type "password"
                 :class "form-control"}]]
       [:button {:type "submit" :class "btn btn-default"} "Login!"]]]]))
