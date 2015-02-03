(ns bowling-kata.core
  (:gen-class))

(defprotocol Frame
  (rolls [this]))

(defrecord OpenFrame [roll]
  Frame
  (rolls [this] [roll]))
(defrecord ClosedFrame [first-roll second-roll]
  Frame
  (rolls [this] [first-roll second-roll]))
(defrecord EmptyFrame []
  Frame
  (rolls [this] []))
(defrecord StrikeFrame []
  Frame
  (rolls [this] [10]))

(defrecord Game [closed-frames open-frame])

(defmulti append-roll (fn [game pins] (type (:open-frame game))))
(defmethod append-roll EmptyFrame [game pins]
  (if (= 10 pins)
      (->Game (conj (:closed-frames game) (->StrikeFrame))
              (->EmptyFrame))
      (->Game (:closed-frames game) (->OpenFrame pins))))
(defmethod append-roll OpenFrame [game pins]
  (let [first-roll (:roll (:open-frame game))
        frames (:closed-frames game)]
    (->Game (conj frames (->ClosedFrame first-roll pins)) (->EmptyFrame))))

(def empty-game (->Game [] (->EmptyFrame)))

(defmulti frame-score (fn [frame later-frames] (type frame)))
(defmethod frame-score ClosedFrame [frame later-frames]
  (+ (:first-roll frame) (:second-roll frame)))
(defmethod frame-score StrikeFrame [frame later-frames]
  (let [later-rolls (flatten (map rolls later-frames))]
    (+ 10 (reduce + (take 2 later-rolls)))))

(defn- -score [tmp-score game]
  (let [frames (:closed-frames game)]
    (if
      (seq frames)
      (let [[frame & later-frames] frames]
        (recur
          (+ tmp-score (frame-score frame later-frames))
          (->Game later-frames (:open-frame game))))
      tmp-score)))
          

(defn score [game]
  (-score 0 game))

(defn roll-game [rolls]
  (reduce append-roll empty-game rolls))

