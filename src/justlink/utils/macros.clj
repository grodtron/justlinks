(ns justlink.utils.macros)

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
      ([] key-map) ; if there's nothing
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
    key-map
    (reverse key-cases)))
