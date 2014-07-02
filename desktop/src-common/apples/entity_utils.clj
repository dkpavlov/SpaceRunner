(ns apples.entity-utils)

(defn get-player
  [entities]
  (some #(if (:player? %) %) entities))

(defn get-grass
  [entities]
  (keep #(if (:grass? %) %) entities))

(defn get-enemy
  [entities]
  (some #(if (:enemy? %) %) entities))