(ns apples.map
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.g2d-physics :refer :all]))


(defn create-map-texture []
  (for [x (range 11)]
    (assoc (texture "grass.png") :x (* x 70) :y 0 :grass? true)))

(defn slide-map [entityies]
  (for [e entityies]
    (let [old-x (:x e)]
      (if (< old-x -70) 
        (assoc e :x 696)
        (assoc e :x (- old-x 2))))))