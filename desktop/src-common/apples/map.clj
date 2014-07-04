(ns apples.map
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.g2d-physics :refer :all]))

(def cloud-y [270 540 310 410])

(defn create-map-grass []
  (for [x (range 12)]
    (-> "grass.png"
      texture
      (assoc :x (* x 70) 
             :y 0 
             :map? true))))

(defn create-map-clouds []
  (for [x (range 3)]
    (-> "cloud.png"
      texture
      (assoc :x (* x 233) 
             :y (rand-nth cloud-y) 
             :map? true))))

(defn create-map []
  (concat (create-map-grass) (create-map-clouds)))

(defn slide-map [entityies]
  (for [e entityies]
    (let [old-x (:x e)]
      (if (< old-x -140) 
        (assoc e :x 696)
        (assoc e :x (- old-x 2))))))