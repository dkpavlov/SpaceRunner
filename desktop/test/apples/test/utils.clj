(ns apples.test.utils
  (:use [apples.utils] :reload)
  (:use [clojure.test]))

(def test-file-name "test-score.csv")

(deftest create-score-file
  
  (is (false? (check! test-file-name)))
  
  (add-score-to-file 1001 test-file-name)
  (add-score-to-file 1002 test-file-name)
  (add-score-to-file 1000 test-file-name)
  
  (is (= (read-score-file test-file-name) '("1001" "1002" "1000")))
  (is (delete-file-by-name test-file-name)))

(deftest get-top-ten-scores-test 
  
  (is (false? (check! test-file-name)))
  
  (add-score-to-file 1001 test-file-name)
  (add-score-to-file 1002 test-file-name)
  (add-score-to-file 1000 test-file-name)
  
  (is (= (get-top-ten-scores test-file-name) '("1002" "1001" "1000")))
  
  (is (delete-file-by-name test-file-name)))

(deftest get-top-ten-scores-limit-results-test
  (is (false? (check! test-file-name)))
  
  (add-score-to-file 1001 test-file-name)
  (add-score-to-file 1002 test-file-name)
  (add-score-to-file 1000 test-file-name)
  (add-score-to-file 1021 test-file-name)
  (add-score-to-file 1032 test-file-name)
  (add-score-to-file 100 test-file-name)
  (add-score-to-file 1301 test-file-name)
  (add-score-to-file 12002 test-file-name)
  (add-score-to-file 1300 test-file-name)
  (add-score-to-file 1401 test-file-name)
  (add-score-to-file 1502 test-file-name)
  (add-score-to-file 1600 test-file-name)
  (add-score-to-file 1301 test-file-name)
  (add-score-to-file 1502 test-file-name)
  (add-score-to-file 1600 test-file-name)
  (add-score-to-file 1701 test-file-name)
  (add-score-to-file 1702 test-file-name)
  (add-score-to-file 1800 test-file-name)
  
  (is (= 10 (count (get-top-ten-scores test-file-name))))
  
  (is (delete-file-by-name test-file-name)))