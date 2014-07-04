(ns apples.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.g2d-physics :refer :all]
            [play-clj.ui :refer :all]
            [apples.utils :as u]
            [apples.map :as m]
            [apples.entity :as e]))
    
(declare apples main-screen menu-screen score-screan)            
            
(defscreen menu-screen
  :on-show
  (fn [screen entities]
    (update! screen :camera (orthographic) :renderer (stage))
    (let [top-scores (u/get-top-ten-scores u/file-name)
          start-game (-> "For new game, press -- SPACE --"
                       (label (color :white))
                       (assoc  :x 10))
          game-title (-> "-- Spce runner --"
                       (label (color :white) :set-font-scale 3)
                       (assoc :x 180 :y 560))
          top-scores-labels (for [x (range (count top-scores))]
                              (-> (nth top-scores x)
                                (label (color :white))
                                (assoc :x 320 :y (- 400 (* x 30)))))]
      (into [start-game game-title] top-scores-labels)))
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
    (let [game-over (-> "-- Game  Over --"
                      (label (color :white))
                      (assoc :x 290 :y 300))
          new-game (-> "-- Press SPACE --" 
                     (label (color :white))
                     (assoc :x 283 :y 270))]
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
    (-> entities
      first
      :score
      (u/add-score-to-file u/file-name)))
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
    (let [player (e/create-player)
          enemy (e/generate-new-enemys 1)
          map (m/create-map)]
      (into [player enemy] map)))
  :on-render
  (fn [screen entities]
    (clear! 125 206 253 0)
    (render! screen entities)
    (let [player (e/get-player entities)
          enemy (e/get-enemy entities)
          map (e/get-grass entities)]
      (if (e/game-over player enemy)
        (set-screen! apples end-screen)
        (into [(e/move player) (e/move-enemy enemy)] (m/slide-map map)))))
  :on-key-down
  (fn [screen entities]
    (let [player (e/get-player entities)
          non-player (rest entities)]
      (cond
        (= (:key screen) (key-code :space))
        (into [(e/jump player)] non-player)
        (= (:key screen) (key-code :control-left))
        (into [(e/squat player)] non-player)
        :else nil)))
  :on-begin-contact
  (fn [screen entities]
    (let [entity (first-entity screen entities)])
    (set-screen! apples end-screen)))
    
(defgame apples
  :on-create
  (fn [this]
    (set-screen! this menu-screen)))
