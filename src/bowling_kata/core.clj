(ns bowling-kata.core
  (:gen-class))

(defrecord OpenFrame [roll])
(defrecord ClosedFrame [first-roll second-roll])
(defrecord EmptyFrame [])

(defprotocol Game
  (closed-frames [this])
  (open-frame [this])
  (score [this]))

(defmulti append-roll (fn [game pins] (type (open-frame game))))
(defmethod append-roll EmptyFrame [game pins]
  (reify Game
    (closed-frames [_] (closed-frames game))
    (open-frame [_] (->OpenFrame pins))
    (score [_] 0)))
(defmethod append-roll OpenFrame [game pins]
  (let [first-roll (:roll (open-frame game))
        frames (closed-frames game)]
    (reify Game
      (closed-frames [_] (conj frames (->ClosedFrame first-roll pins)))
      (open-frame [_] (->EmptyFrame))
      (score [_] 0))))

(def empty-game 
  (reify Game
    (closed-frames [_] [])
    (open-frame [_] (->EmptyFrame))
    (score [_] 0)))


