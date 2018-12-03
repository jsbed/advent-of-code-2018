(ns advent-of-code-2018.three
  (:require [advent-of-code-2018.utils :as utils]
            [clojure.set :as set]))

(defn get-all-pixels [[id left-x top-y width height]]
  (for [x (range left-x (+ left-x width))
        y (range top-y (+ top-y height))]
    {:id id :x x :y y}
    #_{:x x :y y}))

(defn parse [word]
  (->> (utils/get-regex-groups #"(\d+) @ (\d+),(\d+): (\d+)x(\d+)" word)
       (map #(Integer/parseInt %))))

(defn accumulate-pixels [value item]
  (->> (parse item)
       (get-all-pixels)
       (concat value)))

(defn more-than-one [[_key val]]
  (< 1 val))

(defn get-all-more-than-one-ids [{:keys [pixels ids] :as value}
                                 {:keys [id x y]}]
  (let [item {:x x :y y}]
    (if (contains? pixels item)
      (let [updated-ids (-> (get pixels item) (conj id) (set))]
        (assoc value :pixels (assoc pixels item updated-ids)
                     :ids (set/union ids updated-ids)))
      (assoc value :pixels (assoc pixels item #{id})))))

(defn go-1 []
  (->> (utils/load-file-lines "resources/3.txt" #"\r\n")
       (reduce accumulate-pixels [])
       (#(do (println "done") %))
       (frequencies)
       (filter more-than-one)
       (count)))

(defn go-2 []
  (->> (utils/load-file-lines "resources/3.txt" #"\r\n")
       (reduce accumulate-pixels [])
       (#(do (println "done") %))
       (reduce get-all-more-than-one-ids {:ids #{} :pixels {}})
       (:ids)
       (set/difference (set (range 1 1228)))))