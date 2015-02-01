(ns bowling-kata.core-test
  (:require [speclj.core :refer :all]
            [bowling-kata.core :refer :all :as core]))

(describe "gutter game"
  (describe "first roll"
    (with game (core/roll 0))
    (it "should create an open frame"
        (should= [(core/->OpenFrame 0)]
                 (frames @game)))
    (it "should score zero"
        (should= 0 (score @game)))))
               
