(ns justlink.views
  (:use hiccup.page))

(def include-bootstrap-css
  [:link {:type "text/css"
          :rel "stylesheet"
          :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"}])
(def include-bootstrap-js
  [:script {:type "text/javascript"
            :src  "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"}])

(def include-main-css
  [:link {:type "text/css"
          :rel "stylesheet"
          :href "/static/css/main.css"}])

(defn index []
  (html5
    [:head
     [:title "Welcome to JLinks!!"]
     (include-bootstrap-css)]
    [:body
     [:h1 "Hello World!"]]))

(defn navbar [links]
  [:nav {:class "navbar navbar-inverse navbar-fixed-top"}
   [:div {:class "container"}
    [:div {:class "navbar-header"}
     [:a {:class "navbar-brand" :href "/"} "JustLinks!"]]
    [:div {:class "collapse navbar-collapse"}
     [:ul {:class "nav navbar-nav navbar-right"}
      (map #(vector :li %) links)]]]])

(defn login []
  (html5
    [:head
     [:title "Please Log In!"]
     include-bootstrap-css
     include-main-css]
    [:body
     (navbar '([:a {:href "#"} "Register"]))
     [:div {:class "container" :id "body"}
      [:div {:class "panel panel-default"}
       [:div {:class "panel-body"}
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
         [:button {:type "submit" :class "btn btn-default"} "Login!"]]]]]
     include-bootstrap-js]))
