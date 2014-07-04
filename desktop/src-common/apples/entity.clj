(ns apples.entity
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]))

(def enemies-types [{:name "fish" :positions [50 100 150 250] :w 60 :h 35} 
                    {:name "fly" :positions [50 100 150 250] :w 65 :h 30} 
                    {:name "slime" :positions [0] :w 42 :h 22} 
                    {:name "snail" :positions [0] :w 49 :h 26} 
                    {:name "hill_large" :positions [0] :w 41 :h 121}])

(def enemies-start-x 700)
(def floor-x 70)

(defn get-player 
  [entities]
  (some #(if (:player? %) %) entities))

(defn get-enemy 
  [entities]
  (keep #(if (:enemy? %) %) entities))

(defn get-grass 
  [entities]
  (keep #(if (:map? %) %) entities))

(defn slide-left
  [x y type phase]
  (let [next-phase (if (= phase 1)
                     2
                     1)]
    (-> (str type "_" next-phase ".png")
      (texture)
      (assoc :x (dec x) 
             :y y 
             :phase next-phase 
             :type type 
             :enemy? true))))

(defn generate-new-enemys
  [generation]
  (let [enemies-to-display (if (= generation 5)
                             2
                             1)
        next-generation (if (= generation 5)
                          0
                          (inc generation))]
    (for [x (range enemies-to-display)]
      (let [main-type (rand-nth enemies-types)
            type (:name main-type)
            y (+ 70 (rand-nth (:positions main-type)))
            x (+ enemies-start-x (* x 250))
            width (:w main-type)
            height (:h main-type)]
      (-> (str type "_1.png" )
        (texture)
        (assoc :x x 
               :y y 
               :phase 1 
               :type type 
               :enemy? true
               :w width
               :h height
               :gen (inc generation)))))))

(defn move-enemy 
  [entities]
  (flatten
  (for [e entities]
    (let [x (:x e)
          y (:y e)
          type (:type e)
          generation (:gen e)]
      (if (< x -70)
        (generate-new-enemys generation)
        (assoc e :x (- (:x e) 2)))))))

(defn create-player 
  []
  (-> "p_1.png"
    texture
    (assoc :x 50 
           :y floor-x 
           :jump 0 
           :walk 1 
           :player? true 
           :image 1 
           :frame 1
           :w 50 
           :h 80)))

(defn touch? 
  [{p-x :x p-y :y p-w :w p-h :h}
   {e-x :x e-y :y e-w :w e-h :h}]
  (if (or 
        (> e-x (+ p-x p-w))
        (< (+ e-x e-w) p-x)
        (< (+ p-y p-h) e-y)
        (< (+ e-y e-h) p-y))
    false
    true))

(defn game-over 
  [player enemies]
  (some true?
    (for [e enemies]
      (touch? player e))))
 
(defn jump [entity]
  (if (= (:jump entity) 0)
    (-> (texture "p_jump.png")
      (assoc :x 50 
             :y floor-x 
             :jump 1 
             :player? true 
             :w 50 
             :h 80))
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
      (and (= phase 2) (> y floor-x)) 
      (assoc entity :y (- y 4))
      
      (and (= phase 2) (= y floor-x)) 
      (-> (texture "p_1.png")
        (assoc :x 50 
               :y floor-x 
               :jump 0 
               :walk 0 
               :player? true 
               :image 1 
               :frame 1 
               :w 50 
               :h 80))
      
      (and (< y 390) (= phase 1)) 
      (assoc entity :y (+ y 4))
      
      (and (= y 390) (= phase 1)) 
      (assoc entity :jump 2)
      
      :else (if (> (/ frame 5) 1)
              (if (= image 10)
                (-> (texture "p_1.png")
                  (assoc :x 50 
                         :y floor-x 
                         :jump 0 
                         :walk 1 
                         :player? true 
                         :image 1 
                         :frame 1 
                         :w 60 
                         :h 80))
                (-> (str "p_" (inc image) ".png")
                  texture
                  (assoc :x 50 
                         :y floor-x 
                         :jump 0 
                         :walk 1 
                         :player? true 
                         :image (inc image) 
                         :frame 1 
                         :w 50 
                         :h 80)))
              (assoc entity :frame (inc frame))))))