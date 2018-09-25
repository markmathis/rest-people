(ns rest-people.core-test
  (:require [clojure.test :refer :all]
            [rest-people.core :refer :all]
            [clojure.string :as str])
  (:import (java.io File)
           (java.time LocalDate)))

(defn temp-file []
  (File/createTempFile "core" nil))

(deftest test-read-file
  (let [file-1 (temp-file)
        _ (spit file-1 (str/join "\n" "lines"))
        file-2 (temp-file)
        _ (spit file-2 (str/join "\n" "testing"))]
    (try
      (is (= (read-record-files file-1 file-2)
             ["l" "i" "n" "e" "s" "t" "e" "s" "t" "i" "n" "g"]))
      (finally (do (.delete file-1) (.delete file-2))))))

(deftest test-display
  (let [empty-display (with-out-str (display-records [] :last-name))
        single-display (with-out-str (display-records [{:last-name "carow" :first-name "deborah" :gender "female"
                                                        :favorite-color "copper" :date-of-birth (LocalDate/parse "1938-12-09")}] :last-name))]
    (is (= empty-display ""))
    (is (= single-display "deborah carow is a female who was born on 12/09/1938 and whose favorite color is copper\n----\n"))))
