(ns bowling-kata.core-test
  (:require [speclj.core :refer :all]
            [bowling-kata.core :refer :all :as core]))

(describe "gutter game"
  (describe "first roll"
    (with game (core/append-roll core/empty-game 0))
    (it "should create an open frame"
        (should= (core/->OpenFrame 0)
                 (open-frame @game)))
    (it "should score zero"
        (should= 0 (score @game))))
  (describe "second roll"
    (with game (reduce core/append-roll core/empty-game [0 0]))
    (it "should create a closed frame"
        (should= [(core/->ClosedFrame 0 0)]
                 (closed-frames @game)))
    (it "should score zero"
        (should= 0 (score @game)))))
  ;(describe "third roll"
  ;  (with game (reduce core/roll [0 0]))
  ;  (it "should create a closed frame and an open frame"
  ;      (should= [(core/->ClosedFrame 0 0)]
  ;               (closed-frames @game))
  ;      (should= (core/->OpenFrame 0)
  ;               (open-frame @game)))
  ;  (it "should score zero"
  ;      (should= 0 (score @game)))))
    
               
