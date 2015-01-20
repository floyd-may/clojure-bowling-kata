(ns bowling-kata.core-test
  (:require [speclj.core :refer :all]
            [bowling-kata.core :refer :all :as core]))

(describe "game state management"
  (it "should create a frame"
    (should= 
              [(core/->Frame 0)]
              (core/roll [] 0))))
