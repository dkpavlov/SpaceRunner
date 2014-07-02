(ns apples.test.map
  (:use [apples.map] :reload)
  (:use [clojure.test]))

(deftest create-score-file
  (def temp-data (create-map-texture))
  (is (= temp-data start-map)))