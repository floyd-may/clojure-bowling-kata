(ns bowling-kata.core
  (:gen-class))

(defrecord ClosedFrame [first-roll second-roll])
(defrecord OpenFrame [roll])

(defprotocol Game
  (frames [this])
  (add-roll [this roll]))

(declare append-roll-to-open-frame)

(defn- append-frame-to-game [game roll]
  (reify Game
    (frames [this] (conj (frames game) (->OpenFrame roll)))
    (add-roll [_ next-roll] #(append-roll-to-open-frame game roll next-roll))))

(defn- append-roll-to-open-frame [game first-roll second-roll]
  (let [initial-frames (frames game)
        new-frame (->ClosedFrame first-roll second-roll)
        all-frames (conj initial-frames new-frame)]
    (reify Game
      (frames [this] all-frames)
      (add-roll [_ _] (+ 0 0)))))

(defn new-game []
  (reify Game
    (frames [this] [])
    (add-roll [this roll] #(append-frame-to-game this roll))))

