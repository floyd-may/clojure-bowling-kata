(ns bowling-kata.core
  (:gen-class))

(defprotocol Frame
  (is-closed [this]))

(defrecord Game [frames add-roll-fn])

(defrecord OpenFrame [roll]
  Frame
  (is-closed [this] false))

(defrecord ClosedFrame [first-roll second-roll]
  Frame
  (is-closed [this] true))

(defrecord SpareFrame [first-roll second-roll]
  Frame
  (is-closed [this] true))

(defrecord StrikeFrame []
  Frame
  (is-closed [this] true))

(defrecord FinalFrame [first-roll second-roll third-roll]
  Frame
  (is-closed [this] true))

(declare -add-frame)

(defn create-closed-frame [first-roll second-roll]
  (if (= 10 (+ first-roll second-roll))
      (->SpareFrame first-roll second-roll)
      (->ClosedFrame first-roll second-roll)))

(defn create-new-frame [roll]
  (if (= 10 roll)
      (->StrikeFrame)
      (->OpenFrame roll)))

(defn -close-frame [frames first-roll second-roll]
  (let [new-frames (conj frames (create-closed-frame first-roll second-roll))]
    (->Game new-frames
            (partial -add-frame new-frames))))

(defn -add-frame [frames roll]
  (let [new-frame (create-new-frame roll)
        new-frames (conj frames new-frame)]
    (->Game new-frames
            (if (is-closed new-frame)
                (partial -add-frame new-frames)
                (partial -close-frame frames roll)))))

(defn new-game [first-roll]
  (-add-frame [] first-roll))

