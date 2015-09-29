; Fetching and parsing links
(ns justlink.link.link
  (:require [clj-http.client :as http]
            [net.cgrand.enlive-html :as enlive]
            [clojure.string :as string])
  (:use justlink.utils.macros))

; perform a synchronous HTTP request for the desired page
(defn fetch-page [url]
  (http/get url))

; Macro to extract desired meta properties into a dictionary
; see parse-page for use
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

(defn extract-social-metadata [init-map parsed-body]
  (reduce
    (fn
      ([data] data)
      ([data metatag]
       (html-meta data (metatag :attrs)
                  ; -- non fb, twitter, etc.
                  description
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
                  og:title
                  og:type
                  og:url
                  [og:image ["og" "image" "url"]]
                  og:image:type
                  og:image:width
                  og:image:height
                  og:description
                  ; TODO TODO TODO what else... ?
                  )))
    init-map
    (enlive/select parsed-body
                   [:head [:meta (enlive/attr? :content)]])))

; TODO FIXME something like this
(defn extract-basic-metadata [init-map parsed-body]
  (assoc init-map "title"
         (-> parsed-body
             (enlive/select [:head :title]) ; Select the <title> element
             first                          ; The first one
             :content                       ; No wait, just its content
             first)))                       ; Actually just the first part of its content!

(defn get-canonical [parsed-body]
  (-> (first (enlive/select parsed-body
                            [:head [:link (enlive/attr= :rel "canonical")]]))
      :attrs
      :href))

(defn parse-page [result]
  (if (not= (result :status) 200)
    {:status (result :status)}
    (let [parsed-data (enlive/html-snippet (result :body))]
      (->
        {:status 200,
         :data
         (-> {}
             (extract-social-metadata parsed-data)  ; twitter:foobar, og:foobar, etc.
             (extract-basic-metadata  parsed-data))} ; title, etc.
        (#(if-let [canonical (get-canonical parsed-data)]
           (assoc % :canonical canonical)
           %))))))

(defn get-page-info [url]
  (try
    (-> url fetch-page parse-page)
    (catch java.net.UnknownHostException e {:status 404}))) ; return nil if the page does not exist
