(ns apples.entity
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.g2d-physics :refer :all]))

(def floor-x 70)
 
(defn jump [entity]
  (if (= (:jump entity) 0)
    (assoc (texture "p_jump.png") :x 50 :y floor-x :jump 1 :player? true :w 50 :h 80)
    entity))

(defn squat [entity] 
  (assoc entity :jump 2))

(defn move [entity]
  (let [y (:y entity)
        phase (:jump entity)
        walk (:walk entity)
        frame (:frame entity)
        image (:image entity)]
    (cond
      (and (= phase 2) (> y floor-x)) (assoc entity :y (- y 4))
      (and (= phase 2) (= y floor-x)) (assoc (texture "p_1.png") :x 50 :y floor-x :jump 0 :walk 0 :player? true :image 1 :frame 1 :w 50 :h 80)
      (and (< y 390) (= phase 1)) (assoc entity :y (+ y 4))
      (and (= y 390) (= phase 1)) (assoc entity :jump 2)
      :else (if (> (/ frame 5) 1)
              (if (= image 10)
                (assoc (texture "p_1.png") :x 50 :y floor-x :jump 0 :walk 1 :player? true :image 1 :frame 1 :w 60 :h 80)
                (assoc (texture (str "p_" (inc image) ".png")) :x 50 :y floor-x :jump 0 :walk 1 :player? true :image (inc image) :frame 1 :w 50 :h 80))
              (assoc entity :frame (inc frame))))))