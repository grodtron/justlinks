(defproject justlink "0.0.1"
  :dependencies
    [[org.clojure/clojure "1.7.0"]
     [org.clojure/java.jdbc "0.4.2"]
     [org.clojure/data.json "0.2.6"]
     [org.postgresql/postgresql "9.4-1201-jdbc41"]
     [compojure "1.4.0"]
     [hiccup "1.0.5"]
     [ring "1.4.0"]
     [crypto-password "0.1.3"]]
  :plugins [[lein-ring "0.9.6"]]

  :ring {:handler justlink.core/app
         :nrepl {:start? true
                 :port 9998}}
  )
