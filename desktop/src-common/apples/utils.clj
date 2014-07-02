(ns apples.utils
  (:require [clojure.java.io :refer :all]))

(def file-name "score.csv")

(defn check! [file]
  (-> file
    as-file
    .exists))

(defn add-score-to-file [score file]
  (spit file (str score ",") :append true))

(defn read-score-file [file]
  (-> file
    slurp
    (clojure.string/split #",")))

(defn get-top-ten-scores [file]
  (if (check! file) 
    (->> (read-score-file file)
      (sort-by read-string >)
      (take 10))
    nil))

(defn delete-file-by-name [file]
  (delete-file file))