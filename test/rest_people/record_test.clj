(ns rest-people.record-test
  (:require [clojure.test :refer :all]
            [rest-people.record :refer :all])
  (:import (java.time LocalDate)))

(deftest test-split-record-spaces
  (testing "Test splitting record strings delimited by spaces"
    (is (= (split-record "carow deborah female copper 1938-12-09")
           ["carow" "deborah" "female" "copper" "1938-12-09"]))
    (is (= (split-record " mcmorris hortensia female persian 1944-03-09")
           ["mcmorris" "hortensia" "female" "persian" "1944-03-09"]))
    (is (= (split-record "egar olen male gray   2001-12-12")
           ["egar" "olen" "male" "gray" "2001-12-12"]))
    (is (= (split-record "li jo female burgundy 1874-49-05      ")
           ["li" "jo" "female" "burgundy" "1874-49-05"]))))

(deftest test-split-record-pipes
  (testing "Test splitting record strings delimited by pipes"
    (is (= (split-record "declue | ellen | female | red-violet | 1962-07-25")
           ["declue" "ellen" "female" "red-violet" "1962-07-25"]))
    (is (= (split-record "diez | jeremy | male | navy | 2004-03-20")
           ["diez" "jeremy" "male" "navy" "2004-03-20"]))
    (is (= (split-record "kring  |   jadwiga | female | beige | 1988-12-26")
           ["kring" "jadwiga" "female" "beige" "1988-12-26"]))
    (is (= (split-record "  westlie | tommy | male | peach | 1968-07-04  "))
        ["westlie" "tommy" "male" "peach" "1968-07-04"])))

(deftest test-split-record-commas
  (testing "Test splitting record strings delimited by commas"
    (is (= (split-record "isadore, eulalia, female, blue-violet, 1956-07-21")
           ["isadore" "eulalia" "female" "blue-violet" "1956-07-21"]))
    (is (= (split-record "swarm , ilse, female, salmon, 1988-05-15")
           ["swarm" "ilse" "female" "salmon" "1988-05-15"]))
    (is (= (split-record "   jauhar, willie, female, pink, 1992-03-29")
           ["jauhar" "willie" "female" "pink" "1992-03-29"]))
    (is (= (split-record "geringer,monty,male,mauve,1977-06-23")
           ["geringer" "monty" "male" "mauve" "1977-06-23"]))))

(deftest test-record->str
  (testing "Test converting records to a String"
    (is (= (record->str {:last-name "glunz"
                         :first-name "margart"
                         :gender "female"
                         :favorite-color "aquamarine"
                         :date-of-birth (LocalDate/parse "1990-01-01")})
           "margart glunz is a female who was born on 01/01/1990 and whose favorite color is aquamarine"))
    (is (= (record->str {:last-name "b"
                         :first-name "a"
                         :gender "male"
                         :favorite-color "red"
                         :date-of-birth (LocalDate/parse "1974-08-14")})
           "a b is a male who was born on 08/14/1974 and whose favorite color is red"))))
