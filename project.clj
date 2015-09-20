(defproject justlink "0.0.1"
  :dependencies
    [[org.clojure/clojure "1.7.0"]
     [org.clojure/java.jdbc "0.4.2"]
     [org.postgresql/postgresql "9.4-1201-jdbc41"]
     [compojure "1.1.6"]
     [ring/ring-core "1.2.1"]
     [ring/ring-jetty-adapter "1.2.1"]]
  :plugins [[lein-ring "0.8.10"]]

  :ring {:handler restful-clojure.handler/app
         :nrepl {:start? true
                 :port 9998}}
  )
