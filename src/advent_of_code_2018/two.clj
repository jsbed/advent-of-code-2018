(ns advent-of-code-2018.two
  (:require [advent-of-code-2018.utils :as utils]
            [clojure.string :as string]))

(defn count-for-word [max-count word]
  (-> (frequencies word)
      (vals)
      (#(if (not (nil? (some #{max-count} %)))
          1
          0))))

(defn remove-index-from-words [index words]
  (map #(-> (utils/drop-nth index %) (string/join)) words))

(defn go-1 []
  (let [words (utils/load-file-lines "resources/2.txt" #"\r\n")
        twos (reduce #(+ %1 (count-for-word 2 %2)) 0 words)
        threes (reduce #(+ %1 (count-for-word 3 %2)) 0 words)]
    (* twos threes)))

(defn go-2 []
  (let [words (utils/load-file-lines "resources/2.txt" #"\r\n")
        letters (-> (first words) (count))]
    (doseq [index (range letters)]
      (let [removed (-> (remove-index-from-words index words)
                        (frequencies))]
        (when (contains? (set (vals removed)) 2) (println removed))))))