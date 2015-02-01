(ns bowling-kata.core-test
  (:require [speclj.core :refer :all]
            [bowling-kata.core :refer :all :as core]))

(defn roll-game [rolls]
  (let [initial-state (core/new-game)
        reduce-fn #(add-roll %1 %2)]
    (reduce reduce-fn initial-state rolls)))
  
(describe "gutter game"
  (it "should represent first roll as an open frame"
    (should= [(core/->OpenFrame 0)]
             (frames ((add-roll (core/new-game) 0)))))
  (it "should represent two rolls as a closed frame"
    (should= [(core/->ClosedFrame 0 0)]
             (frames (roll-game [0 0])))))
  ;(it "should be represented as ten frames of two gutterballs"
  ;  (should= (vec (take 10 (repeat (core/->ClosedFrame 0 0))))
  ;           (frames (reduce #(add-roll %1 %2) (core/new-game) (take 20 (repeat 0)))))))
