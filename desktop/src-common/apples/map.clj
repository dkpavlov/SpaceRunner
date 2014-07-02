(ns apples.map
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.g2d-physics :refer :all]))

(def start-map 
  '((assoc (texture "grass.png") :x 0 :y 0) 
    (assoc (texture "grass.png") :x 70 :y 0)
    (assoc (texture "grass.png") :x 140 :y 0)
    (assoc (texture "grass.png") :x 210 :y 0)
    (assoc (texture "grass.png") :x 280 :y 0)
    (assoc (texture "grass.png") :x 350 :y 0)
    (assoc (texture "grass.png") :x 420 :y 0)
    (assoc (texture "grass.png") :x 490 :y 0)
    (assoc (texture "grass.png") :x 560 :y 0)
    (assoc (texture "grass.png") :x 630 :y 0)
    (assoc (texture "grass.png") :x 700 :y 0)
    (assoc (texture "grass.png") :x 770 :y 0)))

(defn create-map-texture []
  (for [x (range 11)]
    (assoc (texture "grass.png") :x (* x 70) :y 0 :grass? true)))

(defn slide-map [entityies]
  (for [e entityies]
    (let [old-x (:x e)]
      (if (< old-x -70) 
        (assoc (texture "grass.png") :x 698 :y 0 :grass? true)
        (assoc (texture "grass.png") :x (dec old-x) :y 0 :grass? true)))))