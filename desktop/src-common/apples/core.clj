(ns apples.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.g2d-physics :refer :all]
            [play-clj.ui :refer :all]
            [apples.utils :refer :all]
            [apples.map :refer :all]
            [apples.entity-utils :refer :all]
            [apples.entity :refer :all]))
    
(declare apples main-screen menu-screen score-screan)            
            
(defscreen menu-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (let [start-game (assoc (label "For new game, press -- SPACE --" (color :white)) :x 10)
          game-title (assoc (label "-- Spce runner --" (color :white)) :x 360 :y 580)
          top-scores (get-top-ten-scores file-name)
          top-scores-as-labels (for [x (range (count top-scores))]
                                 (assoc (label (nth top-scores x) (color :white)) :x 390 :y (- 500 (* x 30))))]
      (into [start-game game-title] top-scores-as-labels)))
  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))
  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :space))
      (set-screen! apples main-screen score-screan)
      :else nil))
  :on-resize
  (fn [screen entities]
    (height! screen 600)))

(defscreen end-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (clear! 0 0 0 1)
    (let [game-over (assoc (label "-- Game  Over --" (color :white)) :x 350 :y 300)
          new-game (assoc (label "-- Press SPACE --" (color :white)) :x 343 :y 270)]
      [game-over new-game]))
  :on-render
  (fn [screen entities]
    (clear! 0 0 0 1)
    (render! screen entities)
    entities)
  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :space))
      (set-screen! apples menu-screen)))
  :on-resize
  (fn [screen entities]
    (height! screen 600)))

(defscreen score-screan
  :on-show 
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (assoc (label "SCORE: 0" (color :green)) :x 500 :score 0))
  :on-render
  (fn [screen entities]
    (render! screen)
    (let [score (inc (:score (first entities)))]
      (assoc 
        (label (str "SCORE: " score) (color :green)) :x 500 :score score)))
  :on-hide 
  (fn [screen entities]
    (add-score-to-file (:score (first entities)) file-name))
  :on-key-down
  (fn [screen entities]
    (cond 
      (= (:key screen) (key-code :escape))
      (set-screen! apples end-screen)
      :else nil))
  :on-resize
  (fn [screen entities]
    (height! screen 600)))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (let [player (assoc (texture "p_1.png") :x 50 :y floor-x :jump 0 :walk 1 :player? true :image 1 :frame 1)
          enemy (assoc (texture "hill_large_1.png") :x 701 :y floor-x :enemy? true :phase 1 :type "hill_large")
          grass (create-map-texture)]
      (into [player enemy] grass)))
  :on-render
  (fn [screen entities]
    (clear! 255 255 255 1)
    (render! screen entities)
    (let [player (move (get-player entities))
          enemy (move-enemy (get-enemy entities))
          grass (slide-map (get-grass entities))]
       (into [player enemy] grass)))
  :on-key-down
  (fn [screen entities]
    (cond
      (= (:key screen) (key-code :space))
      (let [player (jump (first entities))
            hill (rest entities)]
          (into [player] hill))
      :else nil))
  :on-begin-contact
  (fn [screen entities]
    (let [entity (first-entity screen entities)])
    (set-screen! apples end-screen)))
    
(defgame apples
  :on-create
  (fn [this]
    (set-screen! this menu-screen)))
