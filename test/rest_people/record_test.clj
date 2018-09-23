(ns rest-people.record-test
  (:require [clojure.test :refer :all]
            [rest-people.record :refer :all])
  (:import (java.time LocalDate)))

(deftest test-parse-record-spaces
  (testing "Test splitting record strings delimited by spaces"
    (is (= (parse-record "carow deborah female copper 1938-12-09")
           {:last-name "carow" :first-name "deborah" :gender "female"
            :favorite-color "copper" :date-of-birth (LocalDate/parse "1938-12-09")}))
    (is (= (parse-record " mcmorris hortensia female persian 1944-03-09")
           {:last-name "mcmorris" :first-name "hortensia" :gender "female"
            :favorite-color "persian" :date-of-birth (LocalDate/parse "1944-03-09")}))
    (is (= (parse-record "egar olen male gray   2001-12-12")
           {:last-name "egar" :first-name "olen" :gender "male"
            :favorite-color "gray" :date-of-birth (LocalDate/parse "2001-12-12")}))
    (is (= (parse-record "li jo female burgundy 1874-08-05      ")
           {:last-name "li" :first-name "jo" :gender "female"
            :favorite-color "burgundy" :date-of-birth  (LocalDate/parse "1874-08-05")}))))

(deftest test-parse-record-pipes
  (testing "Test splitting record strings delimited by pipes"
    (is (= (parse-record "declue | ellen | female | red-violet | 1962-07-25")
           {:last-name "declue" :first-name "ellen" :gender "female"
            :favorite-color "red-violet" :date-of-birth  (LocalDate/parse "1962-07-25")}))
    (is (= (parse-record "diez | jeremy | male | navy | 2004-03-20")
           {:last-name "diez" :first-name "jeremy" :gender "male"
            :favorite-color "navy" :date-of-birth  (LocalDate/parse "2004-03-20")}))
    (is (= (parse-record "kring  |   jadwiga | female | beige | 1988-12-26")
           {:last-name "kring" :first-name "jadwiga" :gender "female"
            :favorite-color "beige" :date-of-birth  (LocalDate/parse "1988-12-26")}))
    (is (= (parse-record "  westlie | tommy | male | peach | 1968-07-04  ")
        {:last-name "westlie" :first-name "tommy" :gender "male"
         :favorite-color "peach" :date-of-birth  (LocalDate/parse "1968-07-04")}))))

(deftest test-parse-record-commas
  (testing "Test splitting record strings delimited by commas"
    (is (= (parse-record "isadore, eulalia, female, blue-violet, 1956-07-21")
           {:last-name "isadore" :first-name "eulalia" :gender "female"
            :favorite-color "blue-violet" :date-of-birth  (LocalDate/parse "1956-07-21")}))
    (is (= (parse-record "swarm , ilse, female, salmon, 1988-05-15")
           {:last-name "swarm" :first-name "ilse" :gender "female"
            :favorite-color "salmon" :date-of-birth  (LocalDate/parse "1988-05-15")}))
    (is (= (parse-record "   jauhar, willie, female, pink, 1992-03-29")
           {:last-name "jauhar" :first-name "willie" :gender "female"
            :favorite-color "pink" :date-of-birth  (LocalDate/parse "1992-03-29")}))
    (is (= (parse-record "geringer,monty,male,mauve,1977-06-23")
           {:last-name "geringer" :first-name "monty" :gender "male"
            :favorite-color "mauve" :date-of-birth  (LocalDate/parse "1977-06-23")}))))

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
