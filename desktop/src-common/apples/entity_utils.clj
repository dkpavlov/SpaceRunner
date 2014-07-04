(ns apples.entity-utils
  (:require [play-clj.g2d :refer :all]))

(def enemies-types [{:name "fish" :positions [0 50 100 150] :w 60 :h 35} 
                    {:name "fly" :positions [0 50 100 150] :w 65 :h 30} 
                    {:name "slime" :positions [0] :w 42 :h 22} 
                    {:name "snail" :positions [0] :w 49 :h 26} 
                    {:name "hill_large" :positions [0] :w 41 :h 121}])

(def enemies-start-x 700)
(def floor-x-x 70)
(def player-width 72)
(def player-hight 97)

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
  [enemies-count]
  (for [x (range enemies-count)]
    (let [main-type (rand-nth enemies-types)
          type (:name main-type)
          y (+ 70 (rand-nth (:positions main-type)))
          x (+ enemies-start-x (* x 50))
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
             :h height)))))

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

(defn create-player 
  []
  (-> "p_1.png"
    texture
    (assoc :x 50 
           :y floor-x-x 
           :jump 0 
           :walk 1 
           :player? true 
           :image 1 
           :frame 1
           :w 50 
           :h 80)))

(defn touch? 
  [player enemy]
  (let [p-x (:x player)
        p-y (:y player)
        p-w (:w player)
        p-h (:h player)
        e-x (:x enemy)
        e-y (:y enemy)
        e-w (:w enemy)
        e-h (:h enemy)]
    (if (or 
          (> e-x (+ p-x p-w))
          (< (+ e-x e-w) p-x)
          (< (+ p-y p-h) e-y)
          (< (+ e-y e-h) p-y))
      false
      true)))

(defn game-over 
  [player enemies]
  (some true?
    (for [e enemies]
      (touch? player e))))
  
    
   