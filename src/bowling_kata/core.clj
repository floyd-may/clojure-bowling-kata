(ns bowling-kata.core
  (:gen-class))

(defrecord OpenFrame [roll])

(defprotocol Game
  (frames [this])
  (score [this]))


(defn roll [roll]
  (reify Game
    (frames [_] [(->OpenFrame roll)])
    (score [_] 0)))

; let's remember dispatch on type
