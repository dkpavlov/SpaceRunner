(ns apples.entity-utils
  (:require [play-clj.g2d :refer :all]))

(def enemies-types [{:name "fish" :positions [0 50 100 150]} 
                    {:name "fly" :positions [0 50 100 150]} 
                    {:name "slime" :positions [0]} 
                    {:name "snail" :positions [0]} 
                    {:name "hill_large" :positions [0]}])

(def enemies-start-x 700)
(def floor-x-x 70)

(defn get-player 
  [entities]
  (some #(if (:player? %) %) entities))

(defn get-enemy 
  [entities]
  (keep #(if (:enemy? %) %) entities))

(defn get-grass 
  [entities]
  (keep #(if (:grass? %) %) entities))

(defn slide-left
  [x y type phase]
  (let [next-phase (if (= phase 1)
                     2
                     1)]
    (-> (str type "_" next-phase ".png")
      (texture)
      (assoc :x (dec x) :y y :phase next-phase :type type :enemy? true))))

(defn generate-new-enemys 
  [enemies-count]
  (for [x (range enemies-count)]
    (let [main-type (rand-nth enemies-types)
          type (:name main-type)
          y (+ 70 (rand-nth (:positions main-type)))
          x (+ enemies-start-x (* x 50))]
    (-> (str type "_1.png" )
      (texture)
      (assoc :x x :y y :phase 1 :type type :enemy? true)))))

(defn move-enemy 
  [entities]
  (flatten
  (for [e entities]
    (let [x (:x e)
          y (:y e)
          type (:type e)
          phase (:phase e)]
      (if (< x -70)
        (generate-new-enemys 1)
        (assoc e :x (- (:x e) 2)))))))

;;Problem with to much ISeq
    
   