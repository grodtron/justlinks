(ns justlink.views
  (:use hiccup.page
        ring.util.response))

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

(defn navbar [links]
  [:nav {:class "navbar navbar-inverse navbar-fixed-top"}
   [:div {:class "container"}
    [:div {:class "navbar-header"}
     [:a {:class "navbar-brand" :href "/"} "JustLinks!"]]
    [:div {:class "collapse navbar-collapse"}
     [:ul {:class "nav navbar-nav navbar-right"}
      (map #(vector :li %) links)]]]])

; TODO - factor out into utils or something
; (key-case
;   map
;   (:key  function1)
;   (:key2 function2))
;
; Similar to defroutes: Takes the first entry for which the
; given key is in the given map, and returns the value
(defmacro key-case [key-map & key-cases]
  (reduce
    (fn
      ([] nil) ; if there's nothing
      ([v]  ; if there's only one thing
       (let [k (first v)
             f (second v)
             m key-map]
         (list 'if (list 'contains? m k) f)))
      ([v1 v2]
       (let [k (first  v2)
             f (second v2)
             m key-map]
         (list 'if (list 'contains? m k) f v1))))
    nil
    (reverse key-cases)))

(defn link-box [link]
  (key-case link
    (:href         [:a {:href (link :href)} (link :name)])
    (:iframe-href  [:iframe {:src (link :iframe-href)}  ])
    (:name         [:p (link :name)] )))

(defn index-view [user friends]
  (response
    (html5
      [:head
       [:title "Please Log In!"]
       include-bootstrap-css
       include-main-css]
      [:body
       (navbar '([:a {:href "#"} "Register"]))
       [:div {:class "container-fluid" :id "body"}
        [:div {:class "row"}
         [:div {:class "col-xs-2"}
          [:ul {:class "nav navbar-nav"}
            (map
              #(vector :li
                       [:a
                        {:href (str "#" (% :id))}
                        (% :name)])
              friends)]]
         [:div {:class "col-xs-6"}
            [:h1 "Welcome, " (user :name) "!"]
            (link-box {:name "This is a test"})
            (link-box {:name "Some Link" :href "#"})

            ]]]])))

(defn login-view []
  (response
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
       include-bootstrap-js])))
