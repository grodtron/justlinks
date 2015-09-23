(ns justlink.core
  (:use
    compojure.core
    compojure.route
    justlink.views
    justlink.controllers
    ring.middleware.params
    ring.middleware.session))

(defroutes app-routes
  (GET "/" req (index req))
  (-> (POST "/login" req (process-login req))
      wrap-params)

  (GET "/test-index" req
       (index-view {:name "Gordon Bailey (test)"}
                   '({:name "Justyna (test)" :id 1}
                    {:name "foofoo (test)" :id 2}
                    {:name "Someone Else" :id 3})))

  (-> (GET "/debug/set-session" req {:body (str req), :session "this is a test"})
      (wrap-params))
  (-> (GET "/debug/view-session" req {:body (str req)})
      (wrap-params))
  (-> (GET "/debug/delete-session" req {:body (str req), :session nil})
      (wrap-params))
  (resources "/static"))

; *ONE* wrap-session call only! Each call has its own session store (it seems)
(def app (wrap-session app-routes))

