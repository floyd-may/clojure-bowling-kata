(ns bowling-kata.core-test
  (:require [speclj.core :refer :all]
            [bowling-kata.core :refer :all :as core])
  (:import [bowling_kata.core ClosedFrame
                              OpenFrame
                              StrikeFrame
                              EmptyFrame
                              SpareFrame]))

(describe "gutter game"
  (describe "first roll"
    (with game (core/append-roll core/empty-game 0))
    (it "should create an open frame"
        (should= []
                 (:closed-frames @game))
        (should= (OpenFrame. 0)
                 (:open-frame @game)))
    (it "should score zero"
        (should= 0 (score @game))))
  (describe "second roll"
    (with game (reduce core/append-roll core/empty-game [0 0]))
    (it "should create a closed frame"
        (should= [(ClosedFrame. 0 0)]
                 (:closed-frames @game))
        (should= (EmptyFrame.)
                 (:open-frame @game)))
    (it "should score zero"
        (should= 0 (score @game))))
  (describe "third roll"
    (with game (core/roll-game [0 0 0]))
    (it "should create a closed frame and an open frame"
        (should= [(ClosedFrame. 0 0)]
                 (:closed-frames @game))
        (should= (OpenFrame. 0)
                 (:open-frame @game)))
    (it "should score zero"
        (should= 0 (score @game))))
  (describe "complete game"
    (with game (core/roll-game (repeat 20 0)))
    (it "should create ten closed frames"
        (should= (vec (repeat 10 (ClosedFrame. 0 0)))
                 (:closed-frames @game))
        (should= (EmptyFrame.)
                 (:open-frame @game)))
    (it "should score zero"
        (should= 0 (score @game)))))

(describe "all ones"
  (with game (core/roll-game (repeat 20 1)))
  (it "should have ten closed frames"
    (should= (vec (repeat 10 (ClosedFrame. 1 1)))
             (:closed-frames @game)))
  (it "should score 20"
    (should= 20 (score @game))))

(describe "leading strike"
  (with game (core/roll-game [10 3 4]))
  (it "should have two closed frames"
    (should=
          [(StrikeFrame.) (ClosedFrame. 3 4)]
          (:closed-frames @game)))
  (it "should score 24"
    (should= 24 (score @game))))

(describe "leading spare"
  (with game (core/roll-game [5 5 3 4]))
  (it "should have two closed frames"
    (should=
          [(SpareFrame. 5 5) (ClosedFrame. 3 4)]
          (:closed-frames @game)))
  (it "should score 20"
    (should= 20 (score @game))))

