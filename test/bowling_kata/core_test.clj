(ns bowling-kata.core-test
  (:require [speclj.core :refer :all]
            [bowling-kata.core :refer :all :as core]))

(describe "SimpleFrame record"
  (let [test-cases [[[]  []  0 false]
                    [[9] [9] 9 false]
                    [[7 2] [7 2] 9 true]]]
    (for [[ctor-args rolls score isfull] test-cases]
      (let [frame (core/->SimpleFrame ctor-args)]
        (describe (str "with rolls " rolls)
          (it (str "returns " rolls " from rolls")
            (should= rolls (.rolls frame)))
          (it (str "returns " score " from score")
            (should= score (.score frame [])))
          (it (str "returns " isfull " from isFull")
            (should= isfull (.isFull frame))))))))

(describe "SpareFrame record"
  (with frame (core/->SpareFrame [5 5]))

  (it "adds the next roll to its score"
    (should= 13 (.score @frame [(core/->SimpleFrame [3 4])])))

  (it "doesn't blow up on no subsequent frames"
    (should= 10 (.score @frame []))))

(describe "StrikeFrame record"
  (with frame (core/->StrikeFrame))

  (it "adds the next two rolls to its score"
    (should= 15 (.score @frame [(core/->SimpleFrame [2 3])])))

  (it "handles zero subsequent rolls"
    (should= 10 (.score @frame [])))

  (it "handles one subsequent roll"
    (should= 11 (.score @frame [(core/->SimpleFrame [1])]))))

(describe "FinalFrame record"
  (let [test-cases [[[]  []  0 false]
                    [[9] [9] 9 false]
                    [[7 2] [7 2] 9 true]
                    [[7 3] [7 3] 10 false]
                    [[7 3 4] [7 3 4] 14 true]
                    [[10 10 10] [10 10 10] 30 true]
                    ]]
    (for [[ctor-args rolls score isfull] test-cases]
      (let [frame (core/->FinalFrame ctor-args)]
        (describe (str "with rolls " rolls)
          (it (str "returns " rolls " from rolls")
            (should= rolls (:m-rolls frame)))

          (it (str "returns " score " from score")
            (should= score (.score frame [])))

          (it (str "returns " isfull " from isFull")
            (should= isfull (.isFull frame))))))))

(describe "roll-2 function"
  (let [test-cases [[[8]  [(core/->SimpleFrame [8])]]
                    [[8 1]  [(core/->SimpleFrame [8 1])]]
                    [[8 1 7]  [(core/->SimpleFrame [8 1])
                               (core/->SimpleFrame [7])]]
                    [[8 2]  [(core/->SpareFrame [8 2])]]
                    [[10]  [(core/->StrikeFrame)]]
                    ]]
    (for [[rolls expected-state] test-cases]
      (let [actual-state (reduce core/roll-2 [] rolls)]
        (it (str "with rolls " rolls)
          (should=
            expected-state
            actual-state))))))

(def gutter-game
  (vec
    (take
      10
      (repeat (core/->SimpleFrame [0 0])))))

(describe "roll-2 function, final frame"
  (let [first-rolls (vec (take 9 gutter-game))
        test-cases [[[7] (core/->FinalFrame [7])]
                    [[7 2] (core/->FinalFrame [7 2])]]]
    (for [[rolls expected-frame] test-cases]
      (let [game-state (reduce core/roll-2 first-rolls rolls)]

        (it (str "with rolls " rolls) 
          (should=
            expected-frame
            (last game-state)))))))

(run-specs)
