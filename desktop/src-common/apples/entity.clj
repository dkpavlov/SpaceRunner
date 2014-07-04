(ns apples.entity
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.g2d-physics :refer :all]))

(def floor-x 70)

(defn slide-enemie [enemie]
  (cond 
    (> 0 (:x enemie)) (assoc (texture "snail1.png") :x 800 :y 0 :type "enemie" :phase 1)
    (= 1 (:phase enemie)) (assoc (texture "snail2.png") :x (- 3 (:x enemie)) :y 0 :type "enemie" :phase 2) 
    (= 2 (:phase enemie)) (assoc (texture "snail1.png") :x (- 3 (:x enemie)) :y 0 :type "enemie" :phase 1)
    :else enemie))

(defn slide-hill [entities]
  (for [e entities]
    (if (> 0 (:x e))
      (assoc (texture "hill_large.png") :x 800 :y floor-x :enemy? true)
      (assoc e :x (- (:x e) 3)))))
  

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