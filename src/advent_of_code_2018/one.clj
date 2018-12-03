(ns advent-of-code-2018.one
  (:require [advent-of-code-2018.utils :as utils]
            [clojure.string :as string]))

(defn add-current [val current]
  (println current)
  (let [multi (-> (first current) (#(if (= \+ %) 1 -1)))
        number (-> (rest current) (string/join) (Integer/parseInt))]
    (+ val (* multi number))))

(defn go-1 []
  (->> (utils/load-file-lines "resources/1.txt" #"\r\n")
       (reduce add-current 0)))

(defn first-freq-twice [{total :total freq :freq :as m} current]
  (let [multi (-> (first current) (#(if (= \+ %) 1 -1)))
        number (-> (rest current) (string/join) (Integer/parseInt))
        new-total (+ total (* multi number))]
    (when (contains? freq new-total) (println "done: " new-total) (throw Exception))
    (assoc m :total new-total
             :freq (-> (conj freq new-total) (set)))))

(defn go-2 []
  (->> (utils/load-file-lines "resources/1.txt" #"\r\n")
       (repeat 1000)
       (flatten)
       (reduce first-freq-twice {:total 0 :freq #{0}}))
  nil)