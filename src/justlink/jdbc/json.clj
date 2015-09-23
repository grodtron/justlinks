; see http://hiim.tv/clojure/2014/05/15/clojure-postgres-json/
(ns justlink.jdbc.json
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.data.json :as json])
  (:import org.postgresql.util.PGobject))

(defn value-to-json-pgobject [value]
  (doto (PGobject.)
    (.setType "jsonb")
    (.setValue (json/write-str value))))

(extend-protocol jdbc/ISQLValue
  clojure.lang.IPersistentMap
  (sql-value [value] (value-to-json-pgobject value))

  clojure.lang.IPersistentVector
  (sql-value [value] (value-to-json-pgobject value)))

(extend-protocol jdbc/IResultSetReadColumn
  PGobject
  (result-set-read-column [pgobj metadata idx]
    (let [type  (.getType  pgobj)
          value (.getValue pgobj)]
      (case type
        "jsonb" (json/read-str value :key-fn keyword)
        "citext" (str value)
        :else value))))
