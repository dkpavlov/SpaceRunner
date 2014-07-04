(ns apples.test.entity
  (:use [apples.entity] :reload)
  (:use [clojure.test]))

(def rec-1 {:x 0 :y 0 :w 10 :h 10})
(def rec-2 {:x 5 :y 5 :w 15 :h 15})
(def rec-3 {:x 5 :y 20 :w 5 :h 5})
(def rec-4 {:x 20 :y 15 :w 5 :h 5})
(def rec-5 {:x 20 :y 20 :w 5 :h 5})
(def rec-6 {:x 25 :y 15 :w 5 :h 5})

(def one-player [{:player? true}
                 {:map? true}
                 {:map? true}
                 {:enemy? true}
                 {:map? true}
                 {:enemy? true}])

(deftest no-interception
  (is (not (touch? rec-1 rec-3))))

(deftest full-interception
  (is (touch? rec-1 rec-2)))

(deftest corner-interception
  (is (touch? rec-2 rec-4)))

(deftest edge-interception
  (is (touch? rec-4 rec-5))
  (is (touch? rec-4 rec-6)))

(deftest player
  (is (= (get-player one-player) {:player? true}))
  (is (= (count (get-player one-player)) 1)))

(deftest map 
  (is (= (count (get-grass one-player)) 3))
  (is (coll? (get-grass one-player))))

(deftest enemy 
  (is (= (count (get-enemy one-player)) 2))
  (is (coll? (get-enemy one-player))))