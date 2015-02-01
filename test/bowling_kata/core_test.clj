(ns bowling-kata.core-test
  (:require [speclj.core :refer :all]
            [bowling-kata.core :refer :all :as core]))

(describe "new-game function"
  (it "should create a new game with an open frame"
      (should= 
              [(core/->OpenFrame 0)]
              (:frames (core/new-game 0))))
  (it "should create a new game with a strike frame"
      (should= 
              [(core/->StrikeFrame)]
              (:frames (core/new-game 10)))))


(defn roll-game [rolls]
  (let [[first-roll & remaining-rolls] rolls
        initial-state (core/new-game first-roll)
        reduce-fn #((:add-roll-fn %1) %2)]
    (reduce reduce-fn initial-state remaining-rolls)))

(describe "game add-roll-fn"
  (it "should create frame from open frame"
    (should= [(core/->ClosedFrame 0 0)]
             (:frames (roll-game [0 0]))))
  (it "should create open frame after initial strike"
    (should= [(core/->StrikeFrame) (core/->OpenFrame 0)]
             (:frames (roll-game [10 0]))))
  (it "should create new open frame after closed frame"
    (should= [(core/->ClosedFrame 0 0) (core/->OpenFrame 0)]
             (:frames (roll-game [0 0 0]))))
  (it "should create 10 frames of gutterballs"
    (should= (vec (take 10 (repeat (core/->ClosedFrame 0 0))))
             (:frames (roll-game (take 20 (repeat 0))))))
  (it "should insert spare frame"
    (should= [
                (core/->ClosedFrame 0 0)
                (core/->ClosedFrame 0 0)
                (core/->ClosedFrame 0 0)
                (core/->SpareFrame 3 7)
             ]
             (:frames (roll-game [0 0 0 0 0 0 3 7]))))
  (it "should insert strike frame"
    (should= [
                (core/->ClosedFrame 0 0)
                (core/->ClosedFrame 0 0)
                (core/->StrikeFrame)
                (core/->ClosedFrame 0 0)
             ]
             (:frames (roll-game [0 0 0 0 10 0 0])))))
  
