(ns bowling-kata.core
  (:gen-class))

(definterface Frame
  (rolls [])
  (score [subsequent-frames])
  (isFull []))

(defrecord SimpleFrame [m-rolls]
  Frame
  (rolls [this] (:m-rolls this))
  (score [this subsequent-frames] (reduce + (:m-rolls this)))
  (isFull [this] (= 2 (count (:m-rolls this)))))

(defrecord SpareFrame [m-rolls]
  Frame
  (rolls [this] (:m-rolls this))
  (score [this subsequent-frames]
    (if (seq subsequent-frames)
        (+ 10 (-> subsequent-frames (first) (.rolls) (first)))
        10))
  (isFull [this] (= 2 (count (:m-rolls this)))))

(defrecord StrikeFrame []
  Frame
  (rolls [this] [10])
  (score [this subsequent-frames]
    (+
      10
      (->> 
        subsequent-frames
        (map #(.rolls %))
        flatten
        (reduce +))))
  (isFull [this] true))

(defrecord FinalFrame [m-rolls]
  Frame
  (rolls [this] (:m-rolls this))
  (score [this subsequent-frames] (reduce + (:m-rolls this)))
  (isFull [this] (or
    (= 3 (count (:m-rolls this)))
    (and
      (> 10 (.score this []))
      (<= 2 (count (:m-rolls this)))))))

(defn create-frame [pins]
  (if (= 10 pins)
    (->StrikeFrame)
    (->SimpleFrame [pins])))

(defn create-multi-roll-frame [existing-frame pins]
  (let [first-roll (first (.rolls existing-frame))]
    (if (= 10 (+ first-roll pins))
      (->SpareFrame [first-roll pins])
      (->SimpleFrame [first-roll pins]))))

(defn roll-2 [state pins]
  (let [
        last-frame (last state)
        frame-count (count state)]
  (if (= 9 frame-count)
    (conj state (->FinalFrame [pins]))
      (if (and last-frame (not (.isFull last-frame)))
        (conj (subvec state 0 (- frame-count 1))
              (create-multi-roll-frame last-frame pins))
        (conj state (create-frame pins))))))

