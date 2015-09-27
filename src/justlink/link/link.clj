(ns justlink.link.link
  (:require [clj-http.client :as http]
            [net.cgrand.enlive-html :as enlive]
            [clojure.string :as string])
  (:use justlink.utils.macros))

; perform a synchronous HTTP request for the desired page
(defn fetch-page [url]
  (http/get url))

(defmacro html-meta [data meta-tag & names]
  ;(if-let [meta-name
  ;        (get meta-tag :name
  ;             (get meta-tag :property))]
  (list
    'if-let (vector 'meta-name (list 'get meta-tag :name
                                     (list 'get meta-tag :property)))
    ; (if-let [keypath 
    ;          (case meta-name
    ;            "twitter:card"   '("twitter" "card")
    ;            "twitter:player" '("twitter" "player" "url")
    ;            nil)]
    (list 'if-let (vector 'keypath
                          (reduce concat
                                  '(case meta-name)
                                  (concat
                                    (map
                                      (fn [n]
                                        (if (vector? n)
                                          [(str (first n)) (second n)]
                                          [(str n) (string/split (str n) #":")]))
                                      names)
                                    '((nil)))))
          (list 'assoc-in data 'keypath (list meta-tag :content))
          data)
    data))


; TODO - other important stuff, check rel-canonical, title, etc.
(defn parse-page [result]
  (if (not= (result :status) 200)
    {:status (result :status)}
    {:status 200,
     :data 
     (reduce
       (fn
         ([data] data)
         ([data metatag]
          (html-meta data (metatag :attrs)
                     ; -- Twitter meta
                     twitter:card
                     twitter:description
                     twitter:title
                     twitter:image
                     [twitter:player ["twitter" "player" "url"]]
                     twitter:player:width
                     twitter:player:height
                     twitter:player:stream
                     ; -- Open Graph
                     ; TODO TODO TODO
                     )))
       {} ; start with empty map
       (enlive/select
         (enlive/html-snippet (result :body))
         [:head [:meta (enlive/attr? :content)]]))}))

(defn get-page-info [url]
  (try
    (-> url fetch-page parse-page)
    (catch java.net.UnknownHostException e {:status 404}))) ; return nil if the page does not exist
